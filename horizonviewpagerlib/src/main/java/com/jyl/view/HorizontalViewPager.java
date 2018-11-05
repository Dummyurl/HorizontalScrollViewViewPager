package com.jyl.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * HorizonScrollView 实现ViewPager
 * Created by JiYouLiang on 2018/10/28.
 */

public class HorizontalViewPager extends HorizontalScrollView implements View.OnTouchListener {
    private final int mScreenWidth;
    private LinearLayout mLayoutContainer;
    private float mDownX;
    private int mCurPage = 0;//当前页面
    private static final int MAX_ITEM_COUNT = 3;//最大item个数
    private ViewPagerAdapter mAdapter;
    private static final String TAG = "HorizontalViewPager";
    private OnPageChangeListener mListener;

    public HorizontalViewPager(Context context) {
        this(context, null, 0);
    }

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setHorizontalScrollBarEnabled(false);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mLayoutContainer = new LinearLayout(context);
        mLayoutContainer.setBackgroundColor(Color.WHITE);
        mLayoutContainer.setOrientation(LinearLayout.HORIZONTAL);
        mLayoutContainer.setHorizontalScrollBarEnabled(false);
        mLayoutContainer.setVerticalScrollBarEnabled(false);
        mLayoutContainer.setLayoutParams(lp);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        mScreenWidth = size.x;

        addView(mLayoutContainer);//使用LinearLayout管理所有tab，因为HorizontalScrollView只能有一个子控件

        setListener();
    }

    private void setListener() {
        setOnTouchListener(this);
    }


    private void initViewPager() {
        if (mAdapter == null || mAdapter.getCount() == 0) {
            Log.i(TAG, "Object mAdapter is null or mAdapter.getCount return 0");
            return;
        }
        mLayoutContainer.removeAllViews();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View item = mAdapter.getView(i);
            mLayoutContainer.addView(item);
        }
    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float moveX = event.getX() - mDownX;
                int offset = getWidth() / 6;
                if (Math.abs(moveX) > offset) {
                    if (moveX > 0) {
                        smoothToLastPage();
                    } else {
                        smoothToNextPage();
                    }
                } else {
                    smoothToCurPage();
                }
                return true;
        }
        return false;
    }

    /**
     * 滑动到下一页
     */
    private void smoothToNextPage() {
        ++mCurPage;
        if (mListener != null) {
            mListener.onPageSelected(mCurPage);
        }
        smoothToPage(mCurPage);
        Log.i(TAG, "smooth to next page");
    }

    /**
     * 上一页
     */
    private void smoothToLastPage() {
        --mCurPage;
        if (mListener != null) {
            mListener.onPageSelected(mCurPage);
        }
        smoothToPage(mCurPage);
        Log.i(TAG, "smooth to last page");
    }

    /**
     * 滑动到当前页
     */
    private void smoothToCurPage() {
        smoothToPage(mCurPage);
        Log.i(TAG, "smooth to current page");
    }

    private void smoothToPage(int position) {
        if (position < 0) {
            mCurPage = position = 0;//重置为第一页
        }
        if (position >= mLayoutContainer.getChildCount()) {
            mCurPage = position = mLayoutContainer.getChildCount() - 1;//重置为最后一页
        }
        Log.i(TAG, "smooth to page " + position);
        smoothScrollTo(position * mScreenWidth, 0);
    }


    public void setAdapter(ViewPagerAdapter adapter) {
        this.mAdapter = adapter;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        initViewPager();
    }

    /**
     * HorizontalViewPager Adapter
     *
     * @param <T>
     */
    public abstract static class ViewPagerAdapter<T> {

        /**
         * get item count
         *
         * @return
         */
        public abstract int getCount();

        /**
         * Get Model item by position
         *
         * @param position
         * @return
         */
        public abstract T getItem(int position);

        /**
         * Get item View by position
         *
         * @param position
         * @return
         */
        public abstract View getView(int position);

    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * Callback interface for responding to changing state of the selected page.
     */

    public interface OnPageChangeListener {
        /**
         * This method will be invoked when a new page becomes selected. Animation is not
         * necessarily complete.
         *
         * @param position Position index of the new selected page.
         */
        void onPageSelected(int position);
    }
}
