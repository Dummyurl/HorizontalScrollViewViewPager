package com.jyl.viewpagersample.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jyl.view.HorizontalViewPager;
import com.jyl.viewpagersample.R;
import com.jyl.viewpagersample.model.MyModel;

import java.util.ArrayList;
import java.util.List;

public class HorizontalViewPagerActivity extends AppCompatActivity {

    private HorizontalViewPager mViewPager;
    private List<MyModel> mData = new ArrayList<MyModel>();
    private int mScreenWidth;
//    private static final

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_view_pager);
        initView();
        initData();
    }

    private void initData() {
        WindowManager wm = getWindowManager();
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        mScreenWidth = size.x;
        int[] resIds = new int[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4, R.drawable.banner5};
        for (int i = 0; i < resIds.length; i++) {
            MyModel model = new MyModel();
            model.setResId(resIds[i]);
            mData.add(model);
        }
        MyAdapter adapter = new MyAdapter(this, mData, mScreenWidth);
        mViewPager.setAdapter(adapter);

    }

    private void initView() {
        mViewPager = (HorizontalViewPager) findViewById(R.id.viewpager);
    }

    private static class MyAdapter extends HorizontalViewPager.ViewPagerAdapter<MyModel> {

        private final int mScreenWidth;
        private Context mContext;
        private List<MyModel> mObjects;

        public MyAdapter(Context context, List<MyModel> data, int screenWidth) {
            this.mObjects = data;
            this.mContext = context;
            this.mScreenWidth = screenWidth;
        }


        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public MyModel getItem(int position) {
            return mObjects.get(position % mObjects.size());
        }

        @Override
        public View getView(int position) {
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(mScreenWidth, dp2Pix(mContext, 140));
            iv.setLayoutParams(p);

            iv.setBackgroundResource(getItem(position % getCount()).getResId());

            return iv;
        }
    }

    private static int dp2Pix(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }
}
