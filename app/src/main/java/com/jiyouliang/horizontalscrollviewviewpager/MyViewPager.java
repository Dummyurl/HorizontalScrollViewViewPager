package com.jiyouliang.horizontalscrollviewviewpager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by JiYouLiang on 2018/10/28.
 */

public class MyViewPager extends HorizontalScrollView implements View.OnTouchListener {
    private final int mScreenWidth;
    private LinearLayout mLayoutContainer;
    private static final int[] resIds = new int[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4, R.drawable.banner5};
    private float mDownX;
    private int mCurPage = 0;//当前页面
    private static final int MAX_ITEM_COUNT = 3;//最大item个数

    public MyViewPager(Context context) {
        this(context, null, 0);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setHorizontalScrollBarEnabled(false);
        FrameLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mLayoutContainer = new LinearLayout(context);
        mLayoutContainer.setBackgroundColor(Color.WHITE);
        mLayoutContainer.setOrientation(LinearLayout.HORIZONTAL);
        mLayoutContainer.setHorizontalScrollBarEnabled(false);
        mLayoutContainer.setVerticalScrollBarEnabled(false);
        mLayoutContainer.setLayoutParams(lp);
//        removeAllViews();
        addView(mLayoutContainer);//使用LinearLayout管理所有tab，因为HorizontalScrollView只能有一个子控件


        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        mScreenWidth = size.x;


        initViewPager();
        setListener();
    }

    private void setListener() {
        setOnTouchListener(this);
    }

    private void initViewPager() {
        mLayoutContainer.removeAllViews();
        System.out.println("屏幕宽度=" + mScreenWidth);
        for (int i = 0; i < resIds.length; i++) {
            ImageView iv = new ImageView(getContext());
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(mScreenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            iv.setLayoutParams(p);
            iv.setBackgroundResource(resIds[i]);

            mLayoutContainer.addView(iv);
        }
    }

    private static int dp2Pix(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        System.out.println("l=" + l + ",t=" + t + ",oldl=" + oldl + ",oldt=" + oldt);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("down");
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float moveX = event.getX() - mDownX;
                System.out.println("距离，" + moveX);
                int offset = getWidth() / 6;
                System.out.println("offset=" + offset);
                if (Math.abs(moveX) > offset) {
                    if (moveX > 0) {
                        smoothToLastPage();
                       /* if (mCurPage != 0) {
                            smoothToLastPage();
                        } else {
                            smoothToCurPage();
                        }*/
                    } else {
                        smoothToNextPage();
                       /* if (mCurPage < mLayoutContainer.getChildCount()-1) {
                            smoothToNextPage();
                        }else{
                            smoothToCurPage();
                        }*/
                    }
                } else {
                    smoothToCurPage();
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                System.out.println("cancel");
                break;
        }
        return false;
    }

    /**
     * 滑动到下一页
     */
    private void smoothToNextPage() {
        mCurPage += 1;
        smoothToPage(mCurPage);
        System.out.println("下一页");
    }

    /**
     * 上一页
     */
    private void smoothToLastPage() {
        mCurPage -= 1;
        smoothToPage(mCurPage);
        System.out.println("上一页");
    }

    /**
     * 滑动到当前页
     */
    private void smoothToCurPage() {
        System.out.println("当前页");
        smoothToPage(mCurPage);
    }

    private void smoothToPage(int position) {
        if (position < 0) {
            mCurPage = position = 0;//重置为第一页
        }
        if (position >= mLayoutContainer.getChildCount()) {
            mCurPage = position = mLayoutContainer.getChildCount() - 1;//重置为最后一页
        }
        System.out.println("滑动到页面=" + position);
        smoothScrollTo(position * mScreenWidth, 0);
    }
}
