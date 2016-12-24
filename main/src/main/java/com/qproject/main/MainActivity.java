package com.qunar.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qproject.main.R;
import com.qproject.ui.UiActivity;
import com.qproject.main.classloader.ClassLoaderActivity;
import com.qproject.main.render.view.RenderActivity;
import com.qproject.main.smartupdate.SmartUpdateActivity;
import com.qproject.feature.FeatureActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toFeature(View view) {
        Intent intent = new Intent(this, FeatureActivity.class);
        startActivity(intent);
    }

    public void toUi(View view) {
        Intent intent = new Intent(this, UiActivity.class);
        startActivity(intent);
    }

    public void toSmartUpdate(View view) {
        Intent intent = new Intent(this, SmartUpdateActivity.class);
        startActivity(intent);
    }

    public void toClassLoader(View view) {
        Intent intent = new Intent(this, ClassLoaderActivity.class);
        startActivity(intent);
    }

    public void toRender(View view) {
        Intent intent = new Intent(this, RenderActivity.class);
        startActivity(intent);
    }
}
