package com.hxiaohui.annotationprocessortest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hxh.simpleintentutil.BindUtil;

public class FirstSeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_see);
    }

    public void gotoNext(View view) {
        MainActivity_BindIntent.GetSetter a = new MainActivity_BindIntent.GetSetter();
        a.setAaa("黄晓辉");
        a.setShit(new MyShit("fuck", new String[]{"ooooo1", "oooooooo2"}));
        BindUtil.start(this, MainActivity.class, a);
    }
}
