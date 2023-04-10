package com.example.databinding;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.databinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //Model 是資料獨立存活，不同於 Activity 的生命週期，但也不是永遠，需注意
    MyViewModel myViewModel;

    //Binding 其實就是把畫面控件的設置，自動宣告化成可用變數
    //然後在 xaml 裡面就可以直接作動作連接
    ActivityMainBinding binding;

    //被 handle 取代
    //final static String Key_num = "key_num";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //不需要
        //setContentView(R.layout.activity_main);

        //綁 binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //建立 model，在 handle 版，需使用建構子
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        //被 handle 取代
        //表示有作過存狀態的動作，可以載出來再置回(老作法了)
//        if(savedInstanceState != null)
//        {
//            myViewModel.getNumber().setValue(savedInstanceState.getInt(Key_num));
//        }

        //把 model 設給 binding 用
        binding.setData(myViewModel);

        //沒寫不會動
        binding.setLifecycleOwner(this);


        //以下原本需要自己寫事件反映的作法就不用了…
        //而是寫到 activity_main.xml 的原始碼裡
        //findViewById(R.i.xxx) 這種連結控件的作法就不用了

//        myViewModel.getNumber().observe(this, num->{
//            binding.textView1.setText(String.valueOf(num));
//        });

//        binding.button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myViewModel.add();
//            }
//        });
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        //利用 key value 的方式把指定型別的值存放，到 instance state 裡，但這是比較老的作法
//        outState.putInt(Key_num, myViewModel.getNumber().getValue());
//    }
}