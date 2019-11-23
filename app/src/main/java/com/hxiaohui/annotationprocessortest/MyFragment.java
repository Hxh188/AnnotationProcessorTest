package com.hxiaohui.annotationprocessortest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

//import com.hxh.simpleintent.BindIntent;
//import com.hxh.simpleintentutil.BindUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author huangxiaohui
 * @date 2019/11/23
 */
public class MyFragment extends Fragment {
//    @BindIntent
//    String name;
//
//    @BindIntent
//    int age;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        BindUtil.bind(this);
//        Toast.makeText(getActivity(), name + " " + age, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my, container, false);
        return view;
    }
}
