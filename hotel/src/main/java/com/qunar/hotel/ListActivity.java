package com.qunar.hotel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.GridView;

import com.qunar.hotel.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.Random;

public class ListActivity extends AppCompatActivity implements AbsListView.OnScrollListener {
    private static final String TAG = "ListActivity";
    private GridView gridView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        gridView = (GridView) findViewById(R.id.gridview1);
        ArrayList<String> imageUrlList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            imageUrlList.add("http://192.168.1.105:8080/qserver/img/" + random.nextInt(16) % 6 + ".jpg");

        }
        imageAdapter = new ImageAdapter(this, imageUrlList);
        gridView.setOnScrollListener(this);
        gridView.setAdapter(imageAdapter);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            Log.i("test", "SCROLL_STATE_IDLE");
            imageAdapter.setGridViewIdeal(true);
            imageAdapter.notifyDataSetChanged();
        } else {
            Log.i("test", "NOT SCROLL_STATE_IDLE");
            imageAdapter.setGridViewIdeal(false);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}