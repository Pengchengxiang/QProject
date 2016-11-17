package com.qunar.home;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class ClassLoaderActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classloader);
        textView = (TextView) findViewById(R.id.textview1);
        try {
            File dexFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "dex.jar");
            File optimizedFile = this.getDir("dex", Context.MODE_PRIVATE);
            DexClassLoader dexClassLoader = new DexClassLoader(dexFile.getAbsolutePath(), optimizedFile.getAbsolutePath(), null, getClassLoader());
            Class testClass = dexClassLoader.loadClass("Test");
            Object object = testClass.newInstance();
            Method method = testClass.getDeclaredMethod("getText");
            String text = (String) method.invoke(object);
            textView.setText(text);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
