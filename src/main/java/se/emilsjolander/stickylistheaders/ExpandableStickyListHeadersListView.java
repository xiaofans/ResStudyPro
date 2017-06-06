package se.emilsjolander.stickylistheaders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;

public class ExpandableStickyListHeadersListView extends StickyListHeadersListView {
    public static final int ANIMATION_COLLAPSE = 1;
    public static final int ANIMATION_EXPAND = 0;
    IAnimationExecutor mDefaultAnimExecutor = new IAnimationExecutor() {
        public void executeAnim(View target, int animType) {
            if (animType == 0) {
                target.setVisibility(0);
            } else if (animType == 1) {
                target.setVisibility(8);
            }
        }
    };
    ExpandableStickyListHeadersAdapter mExpandableStickyListHeadersAdapter;

    public interface IAnimationExecutor {
        void executeAnim(View view, int i);
    }

    public ExpandableStickyListHeadersListView(Context context) {
        super(context);
    }

    public ExpandableStickyListHeadersListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableStickyListHeadersListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ExpandableStickyListHeadersAdapter getAdapter() {
        return this.mExpandableStickyListHeadersAdapter;
    }

    public void setAdapter(StickyListHeadersAdapter adapter) {
        this.mExpandableStickyListHeadersAdapter = new ExpandableStickyListHeadersAdapter(adapter);
        super.setAdapter(this.mExpandableStickyListHeadersAdapter);
    }

    public View findViewByItemId(long itemId) {
        return this.mExpandableStickyListHeadersAdapter.findViewByItemId(itemId);
    }

    public long findItemIdByView(View view) {
        return this.mExpandableStickyListHeadersAdapter.findItemIdByView(view);
    }

    public void expand(long headerId) {
        if (this.mExpandableStickyListHeadersAdapter.isHeaderCollapsed(headerId)) {
            this.mExpandableStickyListHeadersAdapter.expand(headerId);
            List<View> itemViews = this.mExpandableStickyListHeadersAdapter.getItemViewsByHeaderId(headerId);
            if (itemViews != null) {
                for (View view : itemViews) {
                    animateView(view, 0);
                }
            }
        }
    }

    public void collapse(long headerId) {
        if (!this.mExpandableStickyListHeadersAdapter.isHeaderCollapsed(headerId)) {
            this.mExpandableStickyListHeadersAdapter.collapse(headerId);
            List<View> itemViews = this.mExpandableStickyListHeadersAdapter.getItemViewsByHeaderId(headerId);
            if (itemViews != null) {
                for (View view : itemViews) {
                    animateView(view, 1);
                }
            }
        }
    }

    public boolean isHeaderCollapsed(long headerId) {
        return this.mExpandableStickyListHeadersAdapter.isHeaderCollapsed(headerId);
    }

    public void setAnimExecutor(IAnimationExecutor animExecutor) {
        this.mDefaultAnimExecutor = animExecutor;
    }

    private void animateView(View target, int type) {
        if (type != 0 || target.getVisibility() != 0) {
            if ((1 != type || target.getVisibility() == 0) && this.mDefaultAnimExecutor != null) {
                this.mDefaultAnimExecutor.executeAnim(target, type);
            }
        }
    }
}
