package com.airing.jvm.clazz.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class CustomClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            File file = new File("/Users/GEYI/HappyCric/test/" + name + ".class");
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int index = 0;
            while ((index = fis.read()) != -1) {
                os.write(index);
            }
            byte[] b = os.toByteArray();
            os.close();
            fis.close();
            return defineClass(name, b, 0, b.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }

    public static void main(String[] args) throws ClassNotFoundException {
        CustomClassLoader ccl = new CustomClassLoader();
        Class clazz = ccl.loadClass("Hello");
        Arrays.stream(clazz.getDeclaredMethods()).forEach((m) -> {
            try {
                m.invoke(clazz.newInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        });

        System.out.println(clazz.getClassLoader());
        System.out.println(clazz.getClassLoader().getParent());
    }
}
