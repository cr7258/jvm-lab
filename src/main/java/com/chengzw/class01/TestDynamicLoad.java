package com.chengzw.class01;

/**
 * @author chengzw
 * @description 动态链接
 * @since 2021/7/17
 */
public class TestDynamicLoad {

    static {
        System.out.println("*************load com.com.chengzw.class01.TestDynamicLoad************");
    }

    public static void main(java.lang.String[] args) {
        new A();
        System.out.println("*************load test************");
        B b = null;  //动态链接，B不会加载，除非这里执行 new com.com.chengzw.class01.B()
    }
}

class A {
    static {
        System.out.println("*************load com.com.chengzw.class01.A************");
    }

    public A() {
        System.out.println("*************initial com.com.chengzw.class01.A************");
    }
}

class B {
    static {
        System.out.println("*************load com.com.chengzw.class01.B************");
    }

    public B() {
        System.out.println("*************initial com.com.chengzw.class01.B************");
    }
}