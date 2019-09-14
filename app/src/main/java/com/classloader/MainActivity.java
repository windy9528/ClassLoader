package com.classloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Log.d("windy", getExternalCacheDir().getAbsolutePath() + "xxxxx");
        String apkPath = getExternalCacheDir().getAbsolutePath() + "bundle.apk";
        loadApk(apkPath);
    }
//    D:\soft\As\ClassLoader\bundle\build\outputs\apk\debug\bundle-debug.apk

    private void loadApk(String apkPath) {
        File optDir = getDir("opt", MODE_PRIVATE);

        //初始化ClassLoader
        DexClassLoader dexClassLoader = new DexClassLoader(apkPath,
                optDir.getAbsolutePath(), null, this.getClassLoader());

        try {
            Class cls = dexClassLoader.loadClass("com.bundle.BundleUtils");

            //通过反射 拿到外部方法
            if (cls != null) {
                Object instance = cls.newInstance();
                Method method = cls.getMethod("printLog");
                method.invoke(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
