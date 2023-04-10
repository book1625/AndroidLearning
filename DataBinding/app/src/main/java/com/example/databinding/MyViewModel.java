package com.example.databinding;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {

    //由於使用 handle 改成宣在內部
    final static String key_num = "key_num";

    //新一代的 ViewModel 支援
    private SavedStateHandle handle;
    public MyViewModel(SavedStateHandle handle)
    {
        //這個 handle 可以視為一個傳來傳去的 Dict
        this.handle = handle;
    }

    //屬性實體
    private MutableLiveData<Integer> number;

    //屬性存取子
    public MutableLiveData<Integer> getNumber()
    {
        if(!handle.contains(key_num)){
            handle.set(key_num, 0);
        }
        return handle.getLiveData(key_num);

        // 被 handle 取代了
        //  if(number == null){
        //      number = new MutableLiveData<>();
        //      number.setValue(0);
        //  }
        //  return number;
    }

    //按鍵 binding 方法
    public void add(){

        getNumber().setValue(getNumber().getValue()+1);

        //被 handle 取代了
        //number.setValue(number.getValue()+1);
    }
}
