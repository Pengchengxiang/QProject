package com.qproject.feature;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qproject.feature.bitmap.BitmapActivity;
import com.qproject.feature.cache.CacheActivity;
import com.qproject.feature.login.view.LoginActivity;
import com.qproject.rn.RnActivity;

public class FeatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);
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