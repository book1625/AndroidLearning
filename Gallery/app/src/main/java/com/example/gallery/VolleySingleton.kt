package com.example.gallery

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

// Volley 支援隊列 web request 元件
// 用來確保 volley queue 可以全域惟一，私有建構子讓該類只能取用靜態無法建置，而 queue 透過靜態方法開放

class VolleySingleton private constructor() {

    //這個等於宣告類別靜態成員
    //https://ithelp.ithome.com.tw/articles/10238313
    companion object {

        private const val logTag = "mylog"

        //私有
        private var volleyQueue: RequestQueue? = null

        //取得惟一實體，注意 synchronized 封裝方法避免多緒插斷，其內是回傳一次建置，只是連帶設置行為，
        fun getVolleyQueue(context: Context): RequestQueue =
            volleyQueue ?: synchronized(this) {
                Volley.newRequestQueue(context.applicationContext).also { volleyQueue = it }
            }

        //要求取消所有
        fun cancelAll() {
            volleyQueue?.cancelAll { _ ->
                Log.d(logTag, "volley queue item canceled")
                true
            }
        }
    }
}

