package com.jyl.view;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by JiYouLiang on 2018/11/05.
 */

public class CircleIndicatorViewPager extends ConstraintLayout implements HorizontalViewPager.OnPageChangeListener {

    private static final String TAG = "CircleIndicatorPager";
    private HorizontalViewPager mHorizonViewPager;
    private HorizontalViewPager.ViewPagerAdapter mAdapter;
    private LinearLayout mCircleIndicatorWrapper;
    // default circle indicator background list
    private int[] mBgList = new int[]{R.drawable.bg_circle_indicator_unselected, R.drawable.bg_circle_indicator_selected};
    private HorizontalViewPager.OnPageChangeListener mListener;

    private static final int DEFAULT_INDICATOR_SPACE = 4;// 4dp
    private int mIndicatorSpace;

    public CircleIndicatorViewPager(Context context) {
        this(context, null, 0);
    }

    public CircleIndicatorViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicatorViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        removeAllViews();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicatorViewPager);
        int colorsId = a.getResourceId(R.styleable.CircleIndicatorViewPager_civpBackgroundList, 0);
        if (colorsId != 0) {
            TypedArray colorsTA = a.getResources().obtainTypedArray(colorsId);
            for (int i = 0; i < colorsTA.length(); i++) {
                mBgList[i] = colorsTA.getResourceId(i, 0);
            }
            colorsTA.recycle();
        }

        //indicator space attr
        mIndicatorSpace = a.getDimensionPixelSize(R.styleable.CircleIndicatorViewPager_civpSpace, getResources().getDimensionPixelSize(R.dimen.circle_indicator_margin));
        a.recycle();
    }

    private void initHorizontalViewPager() {
        if (null == mAdapter || mAdapter.getCount() == 0) return;
        mHorizonViewPager = new HorizontalViewPager(getContext());
        mHorizonViewPager.setId(R.id.horizonViewPagerId);
        FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        mHorizonViewPager.setLayoutParams(fp);
        addView(mHorizonViewPager);

        mHorizonViewPager.setOnPageChangeListener(this);
    }


    /**
     * init circle indicator
     */
    private void initCircleIndicator() {
        if (null == mAdapter || mAdapter.getCount() == 0) return;
        //circle Indicator wrapper
        mCircleIndicatorWrapper = new LinearLayout(getContext());
        mCircleIndicatorWrapper.setId(R.id.circleIndicatorWrapperId);
        mCircleIndicatorWrapper.setOrientation(LinearLayout.HORIZONTAL);
        mCircleIndicatorWrapper.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mCircleIndicatorWrapper.setLayoutParams(ip);

        addView(mCircleIndicatorWrapper);


        mCircleIndicatorWrapper.removeAllViews();
        //add Indicator of ImageView to the wrapper
        for (int i = 0; i < mAdapter.getCount(); i++) {
            ImageView iv = new ImageView(getContext());
            LinearLayout.LayoutParams ivP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ivP.setMargins(mIndicatorSpace, 0, mIndicatorSpace, 0);
            iv.setLayoutParams(ivP);
            mCircleIndicatorWrapper.addView(iv);
        }
        setCurrentPosition(0);
    }

    /**
     * reset the background of the circle indicator
     */
    private void resetCircleIndicatorBackground() {
        int count = mCircleIndicatorWrapper.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView iv = (ImageView) mCircleIndicatorWrapper.getChildAt(i);
            iv.setBackgroundResource(mBgList[0]);
        }
    }

    /**
     * Set the currently selected page.
     *
     * @param position
     */
    private void setCurrentPosition(int position) {
        int count = mCircleIndicatorWrapper.getChildCount();
        if (count == 0) return;
        /*if(position < 0 || position >= mCircleIndicatorWrapper.getChildCount()){
            Log.w(TAG, "position " + position + " is out of CircleIndicatorViewPager child count index [0, " + (mCircleIndicatorWrapper.getChildCount() - 1)+"]");
            return;
        }*/
        resetCircleIndicatorBackground();
        ImageView iv = (ImageView) mCircleIndicatorWrapper.getChildAt(position);
        iv.setBackgroundResource(mBgList[1]);
    }

    public HorizontalViewPager getViewPager() {
        return mHorizonViewPager;
    }

    public void setAdapter(HorizontalViewPager.ViewPagerAdapter adapter) {
        this.mAdapter = adapter;
        initHorizontalViewPager();
        initCircleIndicator();
        initConstraintSet();
        mHorizonViewPager.setAdapter(adapter);
    }

    private void initConstraintSet() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        int parentId = getId();
        /**
         * ViewPager 在ConstraintLayout居中，相当于属性：
         * layout_constraintBottom_toBottomOf="parent"
         * layout_constraintTop_toTopOf="parent"
         */
        constraintSet.connect(mHorizonViewPager.getId(), ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM);
        constraintSet.connect(mHorizonViewPager.getId(), ConstraintSet.TOP, parentId, ConstraintSet.TOP);

        /**
         * CircleIndicatorViewPager 在ConstraintLayout底部，水平居中，相当于属性：
         * layout_constraintBottom_toBottomOf="parent"
         * layout_constraintLeft_toLeftOf="parent"
         * layout_constraintRight_toRightOf="parent"
         */
        constraintSet.connect(mCircleIndicatorWrapper.getId(), ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM, 10);
        constraintSet.connect(mCircleIndicatorWrapper.getId(), ConstraintSet.LEFT, parentId, ConstraintSet.LEFT);
        constraintSet.connect(mCircleIndicatorWrapper.getId(), ConstraintSet.RIGHT, parentId, ConstraintSet.RIGHT);
        constraintSet.applyTo(this);
    }

    public void setOnPageChangeListener(HorizontalViewPager.OnPageChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * listener callback
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        //fix OutOfBoundsException
        if (position < 0) position = 0;
        if (position >= mCircleIndicatorWrapper.getChildCount())
            position = mCircleIndicatorWrapper.getChildCount() - 1;
        setCurrentPosition(position);
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    private int dp2Px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
