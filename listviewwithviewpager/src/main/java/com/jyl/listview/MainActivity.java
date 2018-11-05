package com.jyl.listview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyl.view.HorizontalViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }


    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        //item divider
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());
        divider.setDrawable(getResources().getDrawable(R.drawable.common_line));
        mRecyclerView.addItemDecoration(divider);
    }

    private void initData() {
        mRecyclerView.setAdapter(new MyAdapter());
    }

    private static class MyAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private int[] resIds = new int[]{R.drawable.friend1, R.drawable.friend2, R.drawable.friend3, R.drawable.friend4,
                R.drawable.friend5, R.drawable.friend6, R.drawable.friend7, R.drawable.friend8, R.drawable.friend9,
                R.drawable.friend10, R.drawable.friend11, R.drawable.friend12, R.drawable.group2};


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            this.mContext = parent.getContext();
            if (viewType == TYPE_HEADER) {
                View headerView = LayoutInflater.from(mContext).inflate(R.layout.viewpager_layout, null);
                return new HeaderViewHolder(headerView);
            } else {
                View normalItemView = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, null);
                return new NormalViewHolder(normalItemView);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type == TYPE_HEADER) {
                bindHeaderViewHolder((HeaderViewHolder) holder);
            } else {
                NormalViewHolder normalHolder = (NormalViewHolder) holder;
                normalHolder.ivContact.setBackgroundResource(resIds[position % resIds.length]);
                normalHolder.tvContact.setText("position " + position);
            }
        }

        private void bindHeaderViewHolder(HeaderViewHolder holder) {
            List<MyModel> mData = new ArrayList<MyModel>();
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            int mScreenWidth = size.x;
            int[] resIds = new int[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4, R.drawable.banner5};
            for (int i = 0; i < resIds.length; i++) {
                MyModel model = new MyModel();
                model.setResId(resIds[i]);
                mData.add(model);
            }

            HorizontalViewPager viewPager = holder.viewPager;
//            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            viewPager.setLayoutParams(p);
            viewPager.setAdapter(new MyViewPagerAdapter(mContext, mData, mScreenWidth));
            viewPager.setOnPageChangeListener(new HorizontalViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    Log.i(TAG, "onPageSelected position="+position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_NORMAL;
            }
        }

    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContact;
        private ImageView ivContact;

        public NormalViewHolder(View itemView) {
            super(itemView);
            this.tvContact = itemView.findViewById(R.id.tv_contact);
            this.ivContact = itemView.findViewById(R.id.iv_contact);
        }
    }


    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private HorizontalViewPager viewPager;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.viewPager = itemView.findViewById(R.id.viewpager);
        }
    }


    private static class MyViewPagerAdapter extends HorizontalViewPager.ViewPagerAdapter<MyModel> {

        private final int mScreenWidth;
        private Context mContext;
        private List<MyModel> mObjects;

        public MyViewPagerAdapter(Context context, List<MyModel> data, int screenWidth) {
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
            iv.setBackgroundResource(getItem(position % getCount()).getResId());
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(mScreenWidth, dp2Pix(mContext, 140));
            iv.setLayoutParams(p);


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
