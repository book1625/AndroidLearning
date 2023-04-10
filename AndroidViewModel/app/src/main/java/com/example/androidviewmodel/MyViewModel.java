package com.example.androidviewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;

//Note : AndroidViewModel 提供了 getApplication() 來取得全域惟一應用程式指標，所以它建構時要提供 application
// 這個案例則進一步把 instance state 功能也整進來，作為 runtime 使用 和 file 級存取的比較

public class MyViewModel extends AndroidViewModel {

    private final SavedStateHandle handle;

    //用來在各資源中作為目標資料的索引鍵
    private final static String key_num = "key_num";

    //SharedPreferences 的索引鍵，SharedPreferences本身是一個 xml 的 key-value，而這個 name 就是檔名
    //不給檔名的話，就會以 activity 的名字來創
    //SharedPreferences 可以理解為一個 xml config file
    //它的位置在 /Data/Data/com.example.androidviewmodel/shared_prefs/
    //可以透過 device file explorer 看到，中間的 pkg 名則依實際名稱變化
    private final static String shp_name = "shp_name";

    //constructor
    public MyViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);

        this.handle = handle;

        //如果沒有 runtime 的值可用
        if (!handle.contains(key_num)) {
            //從 SharedPreferences 中載進來用
            load();
        }
    }

    public LiveData<Integer> getNumber() {
        return handle.getLiveData(key_num, 0);
    }

    // SharedPreferences 檔中載入
    public void load() {
        //讀檔
        SharedPreferences shp = getApplication().getSharedPreferences(shp_name, Context.MODE_PRIVATE);
        int temp = shp.getInt(key_num, 0);

        //放到 runtime dict
        handle.set(key_num, temp);
    }

    // 存到 SharedPreferences 檔中
    public void save() {
        //建立檔案寫入器
        SharedPreferences shp = getApplication().getSharedPreferences(shp_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        //從 runtime dict 中把值取來存，這裡有作 null 防止，和下面沒作的有個比對
        editor.putInt(key_num, getNumber().getValue() == null ? 0 : getNumber().getValue());
        editor.apply();
    }

    //變動number值 (runtime)
    public void addNumber(int x) {
        handle.set(key_num, getNumber().getValue() + x);
    }
}
