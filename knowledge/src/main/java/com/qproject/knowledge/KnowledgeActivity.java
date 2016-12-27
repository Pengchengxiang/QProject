package com.qproject.knowledge;

import android.os.Bundle;
import android.view.View;

import com.qproject.common.QBaseActivity;

public class KnowledgeActivity extends QBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);
     }

    public void toNameSpace(View view) {
        toActivityByClass(NameSpaceActivity.class);
    }
}
