package com.qunar.hotel.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qunar.hotel.R;
import com.qunar.rn.RnActivity;

public class HotelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
    }

    public void toLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void toRn(View view) {
        Intent intent = new Intent(this, RnActivity.class);
        startActivity(intent);
    }

    public void toCache(View view) {
        Intent intent = new Intent(this, CacheActivity.class);
        startActivity(intent);
    }

    public void toBitmap(View view) {
        Intent intent = new Intent(this, BitmapActivity.class);
        startActivity(intent);
    }
}