package com.qunar.hotel.cache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.qunar.hotel.R;
import com.qunar.hotel.bitmap.ImageListAdapter;

import java.util.ArrayList;
import java.util.Random;

public class CacheActivity extends AppCompatActivity{
    private GridView gridView;
    private ImageListAdapter imageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);

        gridView = (GridView) findViewById(R.id.gridview1);
        ArrayList<String> imageUrlList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            imageUrlList.add("http://192.168.1.105:8080/qserver/img/" + random.nextInt(16) % 6 + ".jpg");

        }
        imageListAdapter = new ImageListAdapter(this, imageUrlList);
        gridView.setAdapter(imageListAdapter);
    }
}