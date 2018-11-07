package com.jyl.viewpagersample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jyl.viewpagersample.activity.CircleIndicatorViewPagerActivity;
import com.jyl.viewpagersample.activity.HorizontalViewPagerActivity;
import com.jyl.viewpagersample.activity.RectangleIndicatorViewPagerActivity;
import com.jyl.viewpagersample.activity.ViewPagerInRecyclerViewHeaderActivity;
import com.jyl.viewpagersample.activity.ViewPagerInRecyclerViewHeaderActivity2;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private Class[] mClasses = new Class[]{HorizontalViewPagerActivity.class,
            CircleIndicatorViewPagerActivity.class, ViewPagerInRecyclerViewHeaderActivity.class,
            ViewPagerInRecyclerViewHeaderActivity2.class,
            RectangleIndicatorViewPagerActivity.class};
    private String[] mTitles = new String[]{"HorizontalViewPagerActivity", "CircleIndicatorViewPagerActivity",
            "ViewPagerInRecyclerViewHeaderActivity", "ViewPagerInRecyclerViewHeaderActivity2",
            "RectangleIndicatorViewPagerActivity"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview);
    }

    private void initData() {
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTitles));
    }

    private void setListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, mClasses[position]);
                startActivity(intent);
            }
        });
    }

}
