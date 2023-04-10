package com.example.gallery

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val logTag: String = "mylog"

    //singleton 模式處理 photoListLive
    private val _photoListLive = MutableLiveData<List<PhotoItem>>()
    val photoListLive: MutableLiveData<List<PhotoItem>> get() = _photoListLive

    fun fetchData() {
        val request = StringRequest(Request.Method.GET, getUrl(), {
            //得到資料時，轉 json to list  填入 live data，注意如何取 type ( :: creates a member reference or a class reference)
            _photoListLive.value = Gson().fromJson(it, Pixabay::class.java).hits.toList()
        }, {
            //失敗時....
            Log.d(logTag, "fetchData:${it}")
        })
        VolleySingleton.getVolleyQueue(getApplication()).add(request)
    }

    //todo 這裡其實應該進一步的考慮如何在同關鍵字上，取得更多圖片的連接回來，而且往下填下去… 目前就一次拿回 50 個，看完也沒了

    //取得清單網址，內部有隨機換關鍵字的功能
    private fun getUrl(): String {
        val keyWord = arrayOf("cat", "dog", "bird", "tree", "computer", "phone", "map", "moon", "sun", "star", "space").random()
        Log.d(logTag, "keyword:${keyWord} ")
        return "https://pixabay.com/api/?key=34763193-ee3828eff9bf8e28fe96f3a29&q=${keyWord}&image_type=photo&pretty=true&per_page=50"
    }
}