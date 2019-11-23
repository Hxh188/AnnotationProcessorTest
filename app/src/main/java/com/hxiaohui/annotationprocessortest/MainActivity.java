package com.hxiaohui.annotationprocessortest;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//import com.hxh.simpleintent.BindIntent;
//import com.hxh.simpleintentutil.BindUtil;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements Serializable {

//    @BindIntent
//    String aaa;
//    @BindIntent
//    MyShit shit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        BindUtil.bind(this);
//
////        StringBuilder sb = new StringBuilder();
////        sb.append("name = ").append(name).append(",").append("age = ").append(age).append(",sex = ").append(isMan);
////        Toast.makeText(this, aaa, Toast.LENGTH_SHORT).show();
//
//        MyFragment f = new MyFragment();
//        MyFragment_BindIntent.GetSetter gs = new MyFragment_BindIntent.GetSetter();
//        gs.setName("hhhhaaaaggggg");
//        gs.setAge(333);
//        Bundle bd = BindUtil.newFragmentBundle(gs);
//        f.setArguments(bd);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tra = manager.beginTransaction();
//        tra.replace(R.id.llayout_content, f);
        tra.commitAllowingStateLoss();

    }

}
