package com.chengzw.class01;

import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * @author chengzw
 * @description 打破双亲委派机制
 * @since 2021/7/18
 */
public class BrokeParentDelegate {
    static class BrokeParentDelegateClassLoader extends ClassLoader {
        private String classPath;

        public BrokeParentDelegateClassLoader(String classPath) {
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

        /**
         * 重写类加载方法，实现自己的加载逻辑，不委派给双亲加载
         * @param name
         * @param resolve
         * @return
         * @throws ClassNotFoundException
         */
        protected Class<?> loadClass(String name, boolean resolve)
                throws ClassNotFoundException {
            synchronized (getClassLoadingLock(name)) {
                // First, check if the class has already been loaded
                Class<?> c = findLoadedClass(name);

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();

                    //如果是我们自己的类，就不委派给双亲加载
                    if(!name.startsWith("com.chengzw.class01")){
                        c = this.getParent().loadClass(name);
                    }else {
                        c = findClass(name);
                    }
                    // this is the defining class loader; record the stats
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
                if (resolve) {
                    resolveClass(c);
                }
                return c;
            }
        }
    }


    public static void main(String args[]) throws Exception {
        //初始化自定义类加载器，会先初始化父类ClassLoader，其中会把自定义类加载器的父加载器设置为应用程序类加载器AppClassLoader
        BrokeParentDelegateClassLoader classLoader = new BrokeParentDelegateClassLoader("/Users/chengzhiwei/Code/github/jvm-lab/src/main/classloader");
        //Users/chengzhiwei/Code/github/jvm-lab/src/main/classloader 创建 com/chengzw/class01 目录，将User类的复制类User.class丢入该目录
        Class clazz = classLoader.loadClass("com.chengzw.class01.User");
        Object obj = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("sout", null);
        method.invoke(obj, null);
        //打破双亲委派，不让AppClassLoader加载User类
        System.out.println(clazz.getClassLoader().getClass().getName());

    }
}
