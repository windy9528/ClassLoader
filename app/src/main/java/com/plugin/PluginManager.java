package com.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.SystemClock;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * date:2019/9/13
 * name:windy
 * function:
 */
public class PluginManager {

    private static PluginManager instance;

    private static Context context;
    private static File optFile;
    private static HashMap<String, PluginInfo> pluginMap;

    public PluginManager(Context context) {
        context = context;
        optFile = context.getDir("opt", Context.MODE_PRIVATE);
        pluginMap = new HashMap<>();
    }

    public static PluginManager getInstance() {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager(context);
                }
            }
        }
        return instance;
    }

    //为插件apk创建对应的classLoader
    private static DexClassLoader createPluginDexClassLoader(String apkPath) {

        DexClassLoader classLoader = new DexClassLoader(apkPath,
                optFile.getAbsolutePath(), null, null);

        return classLoader;
    }

    //为对应的插件创建AssetManager
    private static AssetManager createPluginAssetManager(String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();

            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);

            addAssetPath.invoke(assetManager, apkPath);

            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //为对应的插件创建AssetManager
    private static Resources createPluginResources(String apkPath) {
        AssetManager assetManager = createPluginAssetManager(apkPath);

        Resources superResources = context.getResources();

        Resources pluginResources = new Resources(assetManager,
                superResources.getDisplayMetrics(), superResources.getConfiguration());

        return pluginResources;
    }

    //完成classLoader的加载
    public static PluginInfo loadApk(String apkPath) {
        //初始化PluginInfo
        if (pluginMap.get(apkPath) != null) {
            return pluginMap.get(apkPath);
        }
        PluginInfo pluginInfo = new PluginInfo();
        pluginInfo.dexClassLoader = createPluginDexClassLoader(apkPath);
        pluginInfo.assetManager = createPluginAssetManager(apkPath);
        pluginInfo.resources = createPluginResources(apkPath);

        pluginMap.put(apkPath, pluginInfo);

        return pluginInfo;
    }
}
