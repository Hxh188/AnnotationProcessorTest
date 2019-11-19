package com.hxiaohui.annotationprocessortest;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hxh.simpleintent.BindIntent;
import com.hxh.simpleintentutil.BindUtil;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

    @BindIntent(key = "name_k")
    String name;

    @BindIntent(key = "age_k")
    int age;

    @BindIntent(key = "sex_k")
    boolean isMan;

    @BindIntent(key = "shit_k")
    MyShit shit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindUtil.bind(this);

//        StringBuilder sb = new StringBuilder();
//        sb.append("name = ").append(name).append(",").append("age = ").append(age).append(",sex = ").append(isMan);
//
        Toast.makeText(this, shit.kk[1], Toast.LENGTH_SHORT).show();
    }

}
