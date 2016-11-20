package com.qunar.hotel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.GridView;

import com.qunar.hotel.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.Random;

public class CacheActivity extends AppCompatActivity implements AbsListView.OnScrollListener{
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private boolean isGridViewIdle = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);

        gridView = (GridView) findViewById(R.id.gridview1);
        gridView.setOnScrollListener(this);
        ArrayList<String> imageUrlList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            imageUrlList.add("http://192.168.1.105:8080/qserver/img/" + random.nextInt(16) % 6 + ".jpg");

        }
        imageAdapter = new ImageAdapter(this, imageUrlList,isGridViewIdle);
        gridView.setAdapter(imageAdapter);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            isGridViewIdle = true;
            imageAdapter.notifyDataSetChanged();
        }else{
            isGridViewIdle = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}