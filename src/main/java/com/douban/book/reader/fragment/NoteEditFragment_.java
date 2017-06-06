package com.douban.book.reader.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.douban.book.reader.content.page.Range;
import com.douban.book.reader.manager.AnnotationManager_;
import com.douban.book.reader.manager.UserManager_;
import java.util.UUID;
import org.androidannotations.api.builder.FragmentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class NoteEditFragment_ extends NoteEditFragment implements HasViews, OnViewChangedListener {
    public static final String RANGE_ARG = "range";
    public static final String UUID_ARG = "uuid";
    public static final String WORKS_ID_ARG = "worksId";
    private View contentView_;
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    public static class FragmentBuilder_ extends FragmentBuilder<FragmentBuilder_, NoteEditFragment> {
        public NoteEditFragment build() {
            NoteEditFragment_ fragment_ = new NoteEditFragment_();
            fragment_.setArguments(this.args);
            return fragment_;
        }

        public FragmentBuilder_ worksId(int worksId) {
            this.args.putInt("worksId", worksId);
            return this;
        }

        public FragmentBuilder_ range(Range range) {
            this.args.putParcelable("range", range);
            return this;
        }

        public FragmentBuilder_ uuid(UUID uuid) {
            this.args.putSerializable("uuid", uuid);
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
        return this.contentView_;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.contentView_ = null;
    }

    private void init_(Bundle savedInstanceState) {
        injectFragmentArguments_();
        this.mAnnotationManager = AnnotationManager_.getInstance_(getActivity());
        this.mUserManager = UserManager_.getInstance_(getActivity());
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static FragmentBuilder_ builder() {
        return new FragmentBuilder_();
    }

    private void injectFragmentArguments_() {
        Bundle args_ = getArguments();
        if (args_ != null) {
            if (args_.containsKey("worksId")) {
                this.worksId = args_.getInt("worksId");
            }
            if (args_.containsKey("range")) {
                this.range = (Range) args_.getParcelable("range");
            }
            if (args_.containsKey("uuid")) {
                this.uuid = (UUID) args_.getSerializable("uuid");
            }
        }
    }

    public void onViewChanged(HasViews hasViews) {
        init();
    }
}
