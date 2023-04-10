package com.example.lifecycles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity {

    private MyChronometer chronometer;

    //private long elapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer);

        //基礎值，下面就是它本身預設的寫法 elapsedRealtime 表示開機以來的 tick 數， chronometer 設了最一開始的 tick ，接下來就可以計算多了多少 tick
        //chronometer.setBase(SystemClock.elapsedRealtime());

        //當 chronometer 實作了 LifecycleEventObserver 後，它就具有處理 life cycle 事件的能力，這時就要將它到 life cycle 的 observer 群中，讓它有用
        getLifecycle().addObserver(findViewById(R.id.chronometer));
    }

// 由於改用了自已寫的 MyChronometer，透過 life cycle 控件，所以這裡就不需要了

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        //這裡算出這次花了多少 tick 記下來
//        elapsedTime = SystemClock.elapsedRealtime() - chronometer.getBase();
//
//        //這只是畫面刷不刷，計數器是靠算 tick 差來運作的
//        //chronometer.stop();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        //這裡則是利用減去已花掉的時間為新基礎，作出已經算過多少時間的效果來
//        chronometer.setBase(SystemClock.elapsedRealtime() - elapsedTime);
//
//        //這個只是要求要刷畫面
//        chronometer.start();
//    }
}