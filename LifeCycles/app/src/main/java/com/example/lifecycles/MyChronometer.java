package com.example.lifecycles;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

public class MyChronometer extends Chronometer implements LifecycleEventObserver {

    //這個值，如果需要到 activity 死掉依然可支援，就要考慮從 view model 取來，這裡寫 static 只是偷懶
    //這裡就造成如果使用多個 MyChronometer 時，他們只會共用同一個 elapsedTime，這是不行的

    //而且 static 就算按了 sys back 鍵離開，回來都還看到，也是很神奇的，要到手動殺程序才能真的砍掉

    private static long elapsedTime;

    public MyChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if(event == Lifecycle.Event.ON_PAUSE)
        {
            elapsedTime = SystemClock.elapsedRealtime()  - getBase();
        }
        else if(event == Lifecycle.Event.ON_RESUME)
        {
            setBase(SystemClock.elapsedRealtime() - elapsedTime);
            start();
        }
    }
}
