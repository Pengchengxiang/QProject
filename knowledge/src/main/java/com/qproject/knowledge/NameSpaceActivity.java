package com.qproject.knowledge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NameSpaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namespace);
        int res = getResources().getIdentifier("dyused_knowledge_img", "drawable", getPackageName());
    }
}
