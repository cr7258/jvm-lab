package com.chengzw.class02;

import java.util.ArrayList;

/**
 * @author chengzw
 * @description 测试 GC，使用 的jvisualvm 插件查看
 * @since 2021/8/1
 */
public class HeapTest {

    byte[] a  =new byte[1024 * 100]; //100KB

    public static void main(String[] args) throws InterruptedException {
        ArrayList<HeapTest> heapTests = new ArrayList<>();
        while (true){
            heapTests.add(new HeapTest()); //只要有引用，就不会被 GC 回收
            Thread.sleep(10);
        }
    }
}
