package com.qproject.main;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qproject.common.QBaseActivity;
import com.qproject.knowledge.KnowledgeActivity;
import com.qproject.main.R;
import com.qproject.test.TestActivity;
import com.qproject.ui.UiActivity;
import com.qproject.main.classloader.ClassLoaderActivity;
import com.qproject.main.render.view.RenderActivity;
import com.qproject.main.smartupdate.SmartUpdateActivity;
import com.qproject.feature.FeatureActivity;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends QBaseActivity {

    public void toFeature(View view) {
        toActivityByClass(FeatureActivity.class);
    }

    public void toKnowledge(View view) {
        toActivityByClass(KnowledgeActivity.class);
    }

    public void toUi(View view) {
        toActivityByClass(UiActivity.class);
    }

    public void toTest(View view) {
        toActivityByClass(TestActivity.class);
    }

    public void toSmartUpdate(View view) {
        toActivityByClass(SmartUpdateActivity.class);
    }

    public void toClassLoader(View view) {
        toActivityByClass(ClassLoaderActivity.class);
    }

    public void toRender(View view) {
        toActivityByClass(RenderActivity.class);
    }
}
