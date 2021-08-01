package com.chengzw.class01;

/**
 * @author chengzw
 * @description
 * @since 2021/7/17
 */
public class User {

    private int id;
    private String name;

    public User() {
    }

    public User(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void sout() {
        System.out.println("=======AppClassLoader 加载类调用方法 User=======");
    }
}
