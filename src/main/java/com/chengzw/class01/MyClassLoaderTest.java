package com.chengzw.class01;

import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * @author chengzw
 * @description 自定义类加载器
 * @since 2021/7/17
 */
public class MyClassLoaderTest {
    static class MyClassLoader extends ClassLoader {
        private String classPath;

        public MyClassLoader(String classPath) {
            this.classPath = classPath;
        }

        //从磁盘上把类文件读到字节数组中（二进制字节码）
        private byte[] loadByte(String name) throws Exception {
            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath + "/" + name
                    + ".class");
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;
        }

        //自定义类加载器只需要继承 java.lang.ClassLoader 类，该类有两个核心方法，一个是loadClass(String, boolean)，实现了双亲委派机制，
        //还有一个方法是findClass，默认实现是空方法，所以我们自定义类加载器主要是重写findClass方法。
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadByte(name);
                //defineClass将一个字节数组转为Class对象，这个字节数组是class文件读取后最终的字节数组。
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }
        }

    }

    public static void main(String args[]) throws Exception {
        //初始化自定义类加载器，会先初始化父类ClassLoader，其中会把自定义类加载器的父加载器设置为应用程序类加载器AppClassLoader
        MyClassLoader classLoader = new MyClassLoader("/Users/chengzhiwei/Code/github/jvm-lab/src/main/classloader");
        //Users/chengzhiwei/Code/github/jvm-lab/src/main/classloader 创建 com/class01/chengzw 目录，将User类的复制类User.class丢入该目录，改一下print输出作区分
        Class clazz = classLoader.loadClass("com.chengzw.class01.User");
        Object obj = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("sout", null);
        method.invoke(obj, null);
        //sun.misc.Launcher$AppClassLoader，因为双亲委派机制AppClassLoader已经加载过User类了，因此这里看到的类加载器不是我们自定义的类加载器
        System.out.println(clazz.getClassLoader().getClass().getName());


        //在相同目录下创建User2.class文件，可以通过javac生成class文件，改一下print输出作区分
        Class clazz2 = classLoader.loadClass("com.chengzw.class01.User2");
        Object obj2 = clazz2.newInstance();
        Method method2 = clazz2.getDeclaredMethod("sout", null);
        method2.invoke(obj2, null);
        //因为程序目录下没有 User 类
        //此时由于AppClassLoader没有加载过User2类，因此这里看到的类加载器就是我们自定义的类加载器了
        System.out.println(clazz2.getClassLoader().getClass().getName());

    }
}