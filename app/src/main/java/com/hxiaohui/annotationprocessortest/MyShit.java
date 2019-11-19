package com.hxiaohui.annotationprocessortest;

import java.io.Serializable;

public class MyShit implements Serializable {
    public String k;
    public String[] kk;

    public MyShit()
    {
        k = "kkkkkkkkk";
        kk = new String[]{"zzzzzz", "eeeeeeeeeee"};
    }

    public MyShit(String k, String[] kk) {
        this.k = k;
        this.kk = kk;
    }
}
