package com.qproject.test.leakcanary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qproject.test.R;
import com.qproject.test.TestManager;

public class LeakCanaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leakcanary);

        TestManager testManager = TestManager.getInstance(this);
    }
}
