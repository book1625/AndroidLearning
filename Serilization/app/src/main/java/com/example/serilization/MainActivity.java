package com.example.serilization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.serilization.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private static final String FILE_NAME = "mydata.data";

    private static final String TAG = "mylog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding .......
        // setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        binding.buttonSave.setOnClickListener(view -> {
            try {
                Score score = new Score(
                        Integer.valueOf(binding.editTextTextPersonMath.getText().toString()),
                        Integer.valueOf(binding.editTextTextPersonEnglish.getText().toString()),
                        Integer.valueOf(binding.editTextTextPersonChinese.getText().toString())
                );
                Student student = new Student(
                        binding.editTextTextPersonName.getText().toString(),
                        Integer.valueOf(binding.editTextTextPersonAge.getText().toString()),
                        score
                );
                ObjectOutputStream outputStream = new ObjectOutputStream(openFileOutput(FILE_NAME, MODE_PRIVATE));
                outputStream.writeObject(student);
                outputStream.flush();
                outputStream.close();

                binding.editTextTextPersonName.getText().clear();
                binding.editTextTextPersonAge.getText().clear();
                binding.editTextTextPersonMath.getText().clear();
                binding.editTextTextPersonEnglish.getText().clear();
                binding.editTextTextPersonChinese.getText().clear();
                binding.textViewGrade.setText("-");

                Toast.makeText(MainActivity.this, "Data Saved !!", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Log.e(TAG, "save button", e);
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonLoad.setOnClickListener(view -> {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(openFileInput(FILE_NAME));
                Student student = (Student) inputStream.readObject();

                binding.editTextTextPersonName.setText(student.getName());
                binding.editTextTextPersonAge.setText(String.valueOf(student.getAge()));
                binding.editTextTextPersonMath.setText(String.valueOf(student.getScore().getMath()));
                binding.editTextTextPersonEnglish.setText(String.valueOf(student.getScore().getEnglish()));
                binding.editTextTextPersonChinese.setText(String.valueOf(student.getScore().getChinese()));
                binding.textViewGrade.setText(student.getScore().getGrade());

                Toast.makeText(MainActivity.this, "Data Loaded", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Log.e(TAG, "load button ", e);
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}