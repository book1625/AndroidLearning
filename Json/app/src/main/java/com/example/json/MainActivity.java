package com.example.json;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mylog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 資料
        Student s1 = new Student("s1", 10, new Score(50, 80));
        Student s2 = new Student("s2", 9, new Score(90, 95));
        List<Student> students = List.of(s1, s2);

        // google json 套件
        Gson gson = new Gson();

        // 序列化
        String json = gson.toJson(students);
        Log.d(TAG, "json=>" + json);

        // 反序列化
        // 這裡，由於 java 泛型沒辦法直接作 .class 的動作，所以需要作一個暫接的 type 物件來支援
        // 如果這裡的型別 arraylist 的話就直接 .class 得到 type 就可用了
        students = gson.fromJson(json, new TypeToken<List<Student>>() {
        }.getType());

        students.forEach(student -> {
            Log.d(TAG, "student=>" + student.toString());
        });
    }
}

class Student {
    @SerializedName("student_name")  // 可以自訂name，不寫就會預設用變數名
    private String name;
    private int age;
    private Score score;  // 巢狀物件

    private transient int useless; // 可以 transient 作到 json ignore

    public Student(String name, int age, Score score) {
        this.name = name;
        this.age = age;
        this.score = score;

        useless = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public int getUseless() {
        return useless;
    }

    public void setUseless(int useless) {
        this.useless = useless;
    }

    @NonNull
    @Override
    public String toString() {
        return String.join(",", name, String.valueOf(age), score.toString());
    }
}

class Score {
    private int math;
    private int english;

    public Score(int math, int english) {
        this.math = math;
        this.english = english;
    }

    public int getMath() {
        return math;
    }

    public void setMath(int math) {
        this.math = math;
    }

    public int getEnglish() {
        return english;
    }

    public void setEnglish(int english) {
        this.english = english;
    }

    @NonNull
    @Override
    public String toString() {
        return String.join(",", String.valueOf(math), String.valueOf(english));
    }
}