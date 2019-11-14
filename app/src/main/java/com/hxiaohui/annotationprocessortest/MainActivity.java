package com.hxiaohui.annotationprocessortest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hxiaohui.sgetter.Sgetter;

public class MainActivity extends AppCompatActivity {
    @Sgetter
    long id;

    @Sgetter
    String name;

    int kk = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
