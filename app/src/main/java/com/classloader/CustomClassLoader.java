package com.classloader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import dalvik.system.DexClassLoader;

/**
 * date:2019/9/13
 * name:windy
 * function:  自定义 classLoader
 */
public class CustomClassLoader extends DexClassLoader {

    public CustomClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }


    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = getClassData(name);
        if (classData != null) {
            return defineClass(classData, 0, classData.length);
        } else {
            throw new ClassNotFoundException();
        }
    }

    private byte[] getClassData(String name) {

        try {
            InputStream inputStream = new FileInputStream(name);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 2048 * 2;
            byte[] buffer = new byte[bufferSize];
            int bytesNumRead = -1;
            while ((bytesNumRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesNumRead);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
