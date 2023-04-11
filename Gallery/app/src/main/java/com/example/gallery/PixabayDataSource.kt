package com.example.gallery

import android.content.Context
import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson

//依照網路教程，這裡是用 page 2 實作，但實際上 page 3 出來後，操作的方式就不同了
//試作後主要卡在 volley 的要求是非同步監聽事件配合 page 2 的回呼串起行為
//但 page 3 的話，load 函式就要直接給出結果，照官方的示例，是要配合 retrofit 套件來實作，不需要自己處理非同步
//https://developer.android.com/reference/kotlin/androidx/paging/PagingSource
//這裡為了要能照教程往下寫，所以先實作了 page 2

//這裡有個神奇語法，建構子的參數變成了整個物件的私有成員，不用寫建構式來接
class PixabayDataSource(private val context: Context) : PageKeyedDataSource<Int, PhotoItem>() {

    private val logTag: String = "mylog"

    //每次建這個實體時，就決定一次 keyword
    private val keyWord = arrayOf("cat", "dog", "bird", "tree", "computer", "phone", "map", "moon", "sun", "star", "space").random()

    //取得目標 URL
    private fun getUrl(page: Int?): String {
        return "https://pixabay.com/api/?key=34763193-ee3828eff9bf8e28fe96f3a29&q=${keyWord}&image_type=photo&pretty=true&per_page=10&page=${page ?: 1}"
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, PhotoItem>) {
        //建個要求物件去排隊，等待事件
        val url = getUrl(null)
        StringRequest(
            Request.Method.GET,
            url,
            {
                val datalist = Gson().fromJson(it, Pixabay::class.java).hits.toList()
                callback.onResult(datalist, null, 2)
            },
            {
                Log.d(logTag, "loadInitial: $it")
                TODO("error control of net")
            }
        ).also { VolleySingleton.getVolleyQueue(context).add(it) }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {
        //由於沒有往回捲的設計，所以這裡沒實作
        //一般的對話式app，不見得是從頭出現訊息，只有上捲時才開始拿舊的訊息
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {
        val url = getUrl(params.key)
        StringRequest(
            Request.Method.GET,
            url,
            {
                val datalist = Gson().fromJson(it, Pixabay::class.java).hits.toList()
                callback.onResult(datalist, params.key.inc())
            },
            {
                Log.d(logTag, "loadAfter: $it")
                TODO("error control of net")
            }
        ).also { VolleySingleton.getVolleyQueue(context).add(it) }
    }
}

//工作模式來實例化 PixabayDataSource
class PixabayDataSourceFactory(private val context: Context) : DataSource.Factory<Int, PhotoItem>() {

    override fun create(): DataSource<Int, PhotoItem> {
        //工廠模式提供了一個可以介入生成流程的位置，這裡只是很簡單的把我們需要的 context 傳進來
        //可以想像，依照各種參數的不同，工作可以間接的生成多樣化的 DataSource for PhotoItem 來達到個種目的
        return PixabayDataSource(context)
    }
}