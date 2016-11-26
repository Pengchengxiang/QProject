package com.qunar.hotel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qunar.rn.RnActivity;

public class HotelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
    }

    public void toNext(View view) {
        Intent intent = new Intent(this, NextActivity.class);
        startActivity(intent);
    }

    public void toRn(View view) {
        Intent intent = new Intent(this, RnActivity.class);
        startActivity(intent);
    }

    public void toList(View view) {
        Intent intent = new Intent(this, CacheActivity.class);
        startActivity(intent);
    }

    public void toBitmap(View view) {
        Intent intent = new Intent(this, BitmapActivity.class);
        startActivity(intent);
    }
}