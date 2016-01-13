package com.jiang.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuliao on 16/1/8.
 */
public class MyAdapterView extends FrameLayout {

    public MyAdapterView(Context context) {
        this(context, null);
    }

    public MyAdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }



    class RecycleBin {
        /**
         * The position of the first view stored in mActiveViews.
         */
        private int mFirstActivePosition;

        /**
         * Views that were on screen at the start of layout. This array is
         * populated at the start of layout, and at the end of layout all view
         * in mActiveViews are moved to mScrapViews. Views in mActiveViews
         * represent a contiguous range of Views, with position of the first
         * view store in mFirstActivePosition.
         */
        private View[] mActiveViews = new View[0];

        private ArrayList<View> mCurrentScrap = new ArrayList();
        /**
         * Fill ActiveViews with all of the children of the AbsListView.
         *
         * @param childCount
         *            The minimum number of views mActiveViews should hold
         * @param firstActivePosition
         *            The position of the first view that will be stored in
         *            mActiveViews
         */
        void fillActiveViews(int childCount, int firstActivePosition) {
            if (mActiveViews.length < childCount) {
                mActiveViews = new View[childCount];
            }
            mFirstActivePosition = firstActivePosition;
            final View[] activeViews = mActiveViews;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                AbsListView.LayoutParams lp = (AbsListView.LayoutParams) child.getLayoutParams();
                // Don't put header or footer views into the scrap heap
                if (lp != null) {
                    // Note: We do place AdapterView.ITEM_VIEW_TYPE_IGNORE in
                    // active views.
                    // However, we will NOT place them into scrap views.
                    activeViews[i] = child;
                }
            }
        }
        /**
         * Get the view corresponding to the specified position. The view will
         * be removed from mActiveViews if it is found.
         *
         * @param position
         *            The position to look up in mActiveViews
         * @return The view if it is found, null otherwise
         */
        View getActiveView(int position) {
            int index = position - mFirstActivePosition;
            final View[] activeViews = mActiveViews;
            if (index >= 0 && index < activeViews.length) {
                final View match = activeViews[index];
                activeViews[index] = null;
                return match;
            }
            return null;
        }

        /**
         * Put a view into the ScapViews list. These views are unordered.
         *
         * @param scrap
         *            The view to add
         */
        void addScrapView(View scrap) {
            ViewGroup.LayoutParams lp =  scrap.getLayoutParams();
            if (lp == null) {
                return;
            }
            // Don't put header or footer views or views that should be ignored
            // into the scrap heap
            mCurrentScrap.add(scrap);
        }
        /**
         * @return A view from the ScrapViews collection. These are unordered.
         */
        View getScrapView(int position) {
            ArrayList<View> scrapViews;
            scrapViews = mCurrentScrap;
            int size = scrapViews.size();
            if (size > 0) {
                return scrapViews.remove(size - 1);
            } else {
                return null;
            }
        }

    }



}
