package com.qunar.hotel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.GridView;

import com.qunar.hotel.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.Random;

public class CacheActivity extends AppCompatActivity{
    private GridView gridView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);

        gridView = (GridView) findViewById(R.id.gridview1);
        ArrayList<String> imageUrlList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            imageUrlList.add("http://localhost:8080/qserver/img/" + random.nextInt(16) % 6 + ".jpg");

        }
        imageAdapter = new ImageAdapter(this, imageUrlList);
        gridView.setAdapter(imageAdapter);
    }
}