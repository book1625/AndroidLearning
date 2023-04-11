package com.example.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.paging.toLiveData

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    //這也是挺神奇的模式，對 factory 直接 toLiveData，其它的他都包辦?? pageSize 參數沒有實際一路用到我自己的 PixabayDataSource，所以這裡只是白傳…
    val pagedLivePhotos = PixabayDataSourceFactory(application).toLiveData(1)

    //用來提供畫面重刷新的關鍵字圖
    fun resetQuery() {
        //這動等於告訴 dataSource 物件說他沒用了，而與它連動的畫面會因此被告知，然後新的 dataSource 物件被再建立，重新開刷 page
        pagedLivePhotos.value?.dataSource?.invalidate()
    }
}