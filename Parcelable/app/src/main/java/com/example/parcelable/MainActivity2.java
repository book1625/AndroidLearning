package com.example.parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;

import com.example.parcelable.databinding.ActivityMain2Binding;
import com.google.android.material.tabs.TabLayout;

// 透過 AndroidManifest.xml 的修改，對這個 activity 加上了 android:process=":mainActivity2" 屬性
// 它就會在另一個 process 運行，這點從加上的 logd 可以看出來
// 沒有加的話，兩個 activity 其實還是在同一 process 底下運作

public class MainActivity2 extends AppCompatActivity {

    ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2);

        // 得到啟動 activity 的 intent 並從中提取資料
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        Student student = bundle.getParcelable("student");

        // 連結畫面
        binding.textViewName.setText(student.getName());
        binding.textViewAge.setText(String.valueOf(student.getAge()));
        binding.textViewMath.setText(String.valueOf(student.getScore().getMath()));
        binding.textViewEnglish.setText(String.valueOf(student.getScore().getEnglish()));

        int pid = android.os.Process.myPid();
        Log.d("mylog", "Activity2:" + pid);
    }
}