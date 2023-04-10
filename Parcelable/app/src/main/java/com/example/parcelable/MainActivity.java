package com.example.parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.parcelable.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.buttonSend.setOnClickListener(view -> {
            Score score = new Score(
                    Integer.valueOf(binding.editTextTextPersonMath.getText().toString()),
                    Integer.valueOf(binding.editTextTextPersonEnglish.getText().toString())
            );
            Student student = new Student(
                    binding.editTextTextPersonName.getText().toString(),
                    Integer.valueOf(binding.editTextTextPersonAge.getText().toString()),
                    score
            );

            // intent 作為系統程序間溝通的橋梁，這裡指定了在，兩 activity 間動作，注意參數的型別
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);

            // Bundle 是標準作法，就是個 key value dict，等於可以打包更多不同資料
            // intent.putParcelableArrayListExtra() 則是簡化版，沒那麼多打包
            Bundle bundle = new Bundle();
            bundle.putParcelable("student", student);
            intent.putExtra("data", bundle);

            // 啟動另一個 activity
            startActivity(intent);
        });

        int pid = android.os.Process.myPid();
        Log.d("mylog", "Activity1:" + pid);
    }
}