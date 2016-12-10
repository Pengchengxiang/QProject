package com.qunar.home.classloader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qunar.home.R;

import java.io.File;
import java.lang.reflect.Constructor;
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
    }

    public void loadDex(View view) {
        try {
            //动态从SD卡中的dex.jar文件中，加载Test.java类
            File inDexFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "dex.jar");
            File outDexFile = this.getDir("dex", Context.MODE_PRIVATE);
            DexClassLoader dexClassLoader = new DexClassLoader(inDexFile.getAbsolutePath(), outDexFile.getAbsolutePath(), null, getClassLoader());
            Class testClass = dexClassLoader.loadClass("Test");

            //创建Test.java类对象，调用getText方法获取字符串，显示在TextView中
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

    public void loadUninstallApk(View view) {
        try {
            File dexFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "app-debug.apk");
            File optimizedFile = this.getDir("dex", Context.MODE_PRIVATE);
            DexClassLoader dexClassLoader = new DexClassLoader(dexFile.getAbsolutePath(), optimizedFile.getAbsolutePath(), null, getClassLoader());
            Class testClass = dexClassLoader.loadClass("com.example.chengxiangpeng.loaduninstallapk.MainActivity");
            Constructor constructor = testClass.getConstructor(new Class[]{});
            Object object = constructor.newInstance(new Object[]{});
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

    public void loadInstallApk(View view) {
        try {
            //获取动态加载已安装apk的上下文对象，Resources对象
            String packageName = "com.example.chengxiangpeng.loadinstallapk";
            Context context = createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY | Context.CONTEXT_INCLUDE_CODE);

            //获取相关资源如字符串
            Resources resources = context.getResources();
            String string = resources.getString(resources.getIdentifier("load_install_apk_string", "string", packageName));
            Toast.makeText(this,string,Toast.LENGTH_SHORT).show();

            //动态加载MainActivit，并启动MainActivity
            Class aClass = context.getClassLoader().loadClass(packageName + ".MainActivity");
            startActivity(new Intent(context, aClass));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
