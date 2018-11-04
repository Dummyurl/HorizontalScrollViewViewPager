package com.jyl.view;

import android.view.View;

/**
 * HorizontanViewPager adapter
 * Created by JiYouLiang on 2018/11/01.
 */

public abstract class ViewPagerAdapter<T> {

    /**
     * get item count
     * @return
     */
    public abstract int getCount();

    /**
     * Get Model item by position
     * @param position
     * @return
     */
    public abstract T getItem(int position);

    /**
     * Get item View by position
     * @param position
     * @return
     */
    public abstract View getView(int position);

}
