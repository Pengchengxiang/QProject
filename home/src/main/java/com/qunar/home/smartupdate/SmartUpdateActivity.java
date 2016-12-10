package com.qunar.home.smartupdate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.qunar.home.R;
import com.qunar.home.utils.DiffUtils;
import com.qunar.home.utils.PatchUtils;

import java.io.File;

/**
 * Created by chengxiang.peng on 2016/11/11.
 */

public class SmartUpdateActivity extends AppCompatActivity {
    String externalStoragePath = Environment.getExternalStorageDirectory().getPath() + "/qproject/";
    String newPath = externalStoragePath + "app-new.apk";
    String oldPath = externalStoragePath + "app-old.apk";
    String patchPath = externalStoragePath + "patch.patch";
    String mergePath = externalStoragePath + "merge.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartupdate);

    }

    public void generateDiffPatch(View view) {
        int resultCode = DiffUtils.diff(oldPath, newPath, patchPath);
        if (resultCode == 0) {
            Toast.makeText(this, "generateDiffPatch success!", Toast.LENGTH_SHORT).show();
        }
    }

    public void mergePatchApk(View view) {
        int resultCode = PatchUtils.patch(oldPath, mergePath, patchPath);
        if (resultCode == 0) {
            Toast.makeText(this, "mergePatchApk success!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updatePatchApk(View view) {
        File mergeFile = new File(mergePath);
        if (mergeFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(mergeFile), "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }

    static {
        System.loadLibrary("smartupdate");
    }
}
