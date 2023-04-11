package com.example.androidviewmodel;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidviewmodel.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //系結物件
    ActivityMainBinding binding;

    //資料實體
    MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Note 這個事件可以應對大多數的狀態，適合寫檔記錄的時間點

        myViewModel.save();
    }
}