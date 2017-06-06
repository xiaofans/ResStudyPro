package com.douban.book.reader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.douban.book.reader.R;
import com.douban.book.reader.entity.Annotation;
import com.douban.book.reader.entity.UserInfo;
import com.douban.book.reader.manager.AnnotationManager_;
import com.douban.book.reader.manager.UserManager_;
import com.douban.book.reader.view.NoteNavigationView;
import com.douban.book.reader.view.NotePrivacyInfoView;
import com.douban.book.reader.view.ParagraphView;
import com.douban.book.reader.view.UserAvatarView;
import java.util.UUID;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.BackgroundExecutor.Task;
import org.androidannotations.api.UiThreadExecutor;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class NoteDetailFragment_ extends NoteDetailFragment implements HasViews, OnViewChangedListener {
    public static final String ID_OR_UUID_ARG = "idOrUuid";
    public static final String NOTE_NAVIGATION_ENABLED_ARG = "noteNavigationEnabled";
    public static final String UUID_ARG = "uuid";
    private View contentView_;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public static class FragmentBuilder_ extends FragmentBuilder<FragmentBuilder_, NoteDetailFragment> {
        public NoteDetailFragment build() {
            NoteDetailFragment_ fragment_ = new NoteDetailFragment_();
            fragment_.setArguments(this.args);
            return fragment_;
        }

        public FragmentBuilder_ uuid(UUID uuid) {
            this.args.putSerializable("uuid", uuid);
            return this;
        }

        public FragmentBuilder_ idOrUuid(String idOrUuid) {
            this.args.putString(NoteDetailFragment_.ID_OR_UUID_ARG, idOrUuid);
            return this;
        }

        public FragmentBuilder_ noteNavigationEnabled(boolean noteNavigationEnabled) {
            this.args.putBoolean(NoteDetailFragment_.NOTE_NAVIGATION_ENABLED_ARG, noteNavigationEnabled);
            return this;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(this.onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public View findViewById(int id) {
        if (this.contentView_ == null) {
            return null;
        }
        return this.contentView_.findViewById(id);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (this.contentView_ == null) {
            this.contentView_ = inflater.inflate(R.layout.frag_note_detail, container, false);
        }
        return this.contentView_;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.contentView_ = null;
        this.mUserAvatar = null;
        this.mUserInfo = null;
        this.mNoteUuid = null;
        this.mNoteDetail = null;
        this.mQuotedText = null;
        this.mNoteCreatedDate = null;
        this.mPrivacyView = null;
        this.mDividerUnderPrivacyView = null;
        this.mNoteNavigationView = null;
        this.mLoadErrorViewBase = null;
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        injectFragmentArguments_();
        this.mUserManager = UserManager_.getInstance_(getActivity());
        this.mAnnotationManager = AnnotationManager_.getInstance_(getActivity());
        setHasOptionsMenu(true);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static FragmentBuilder_ builder() {
        return new FragmentBuilder_();
    }

    public void onViewChanged(HasViews hasViews) {
        this.mUserAvatar = (UserAvatarView) hasViews.findViewById(R.id.user_avatar);
        this.mUserInfo = (ParagraphView) hasViews.findViewById(R.id.user_info);
        this.mNoteUuid = (TextView) hasViews.findViewById(R.id.note_uuid);
        this.mNoteDetail = (ParagraphView) hasViews.findViewById(R.id.note_detail);
        this.mQuotedText = (ParagraphView) hasViews.findViewById(R.id.quoted_text);
        this.mNoteCreatedDate = (TextView) hasViews.findViewById(R.id.note_created_date);
        this.mPrivacyView = (NotePrivacyInfoView) hasViews.findViewById(R.id.note_privacy);
        this.mDividerUnderPrivacyView = hasViews.findViewById(R.id.divider_under_privacy_view);
        this.mNoteNavigationView = (NoteNavigationView) hasViews.findViewById(R.id.note_navigation);
        this.mLoadErrorViewBase = (ViewGroup) hasViews.findViewById(R.id.error_view_base);
        init();
    }

    private void injectFragmentArguments_() {
        Bundle args_ = getArguments();
        if (args_ != null) {
            if (args_.containsKey("uuid")) {
                this.uuid = (UUID) args_.getSerializable("uuid");
            }
            if (args_.containsKey(ID_OR_UUID_ARG)) {
                this.idOrUuid = args_.getString(ID_OR_UUID_ARG);
            }
            if (args_.containsKey(NOTE_NAVIGATION_ENABLED_ARG)) {
                this.noteNavigationEnabled = args_.getBoolean(NOTE_NAVIGATION_ENABLED_ARG);
            }
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share, menu);
        inflater.inflate(R.menu.edit, menu);
        inflater.inflate(R.menu.delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId_ = item.getItemId();
        if (itemId_ == R.id.action_share) {
            onMenuShareClicked();
            return true;
        } else if (itemId_ == R.id.action_edit) {
            onMenuEditClicked();
            return true;
        } else if (itemId_ != R.id.action_delete) {
            return super.onOptionsItemSelected(item);
        } else {
            onMenuDeleteClicked();
            return true;
        }
    }

    void updateUserInfo(final UserInfo userInfo) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateUserInfo(userInfo);
            }
        }, 0);
    }

    void updateAnnotationInfo(final Annotation annotation) {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.updateAnnotationInfo(annotation);
            }
        }, 0);
    }

    void dismissLoadingErrorView() {
        UiThreadExecutor.runTask("", new Runnable() {
            public void run() {
                super.dismissLoadingErrorView();
            }
        }, 0);
    }

    void loadData(Object idOrUuidToLoad) {
        final Object obj = idOrUuidToLoad;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadData(obj);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void loadUserInfo(int userId) {
        final int i = userId;
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.loadUserInfo(i);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void onMenuShareClicked() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.onMenuShareClicked();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }

    void deleteNote() {
        BackgroundExecutor.execute(new Task("", 0, "") {
            public void execute() {
                try {
                    super.deleteNote();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        });
    }
}
