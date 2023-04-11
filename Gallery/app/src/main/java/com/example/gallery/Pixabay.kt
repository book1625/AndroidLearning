package com.example.gallery

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

//kotlin 特有資料類，幫你把各種 get set 都寫了，宣告 class 直接就指定了建構參數取代了建構子

data class Pixabay(
    val totalHits: Int,
    val hits: Array<PhotoItem>,
    val total: Int
) {

    //由於屬性中有 Array，所以 compiler 會要求寫一下 compare 的動作

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pixabay

        if (totalHits != other.totalHits) return false
        if (!hits.contentEquals(other.hits)) return false
        if (total != other.total) return false

        return true
    }

    override fun hashCode(): Int {
        var result = totalHits
        result = 31 * result + hits.contentHashCode()
        result = 31 * result + total
        return result
    }
}

//這裡使用 kotlin 的 plugin => kotlin-parcelize 來簡化 Parcelable 實作
@Parcelize
data class PhotoItem(
    @SerializedName("id")
    val photoId: Int,

    @SerializedName("webformatURL")
    val previewUrl: String,
    @SerializedName("webformatWidth")
    val previewWidth: Int,
    @SerializedName("webformatHeight")
    val previewHeight: Int,

    @SerializedName("largeImageURL")
    val imageUrl: String,
    @SerializedName("imageWidth")
    val imageWidth: Int,
    @SerializedName("imageHeight")
    val imageHeight: Int,

    @SerializedName("likes")
    val likes: Int

    //由於這裡用了 Parcelable，所以就不能再用 companion object ，沒辦法把差異比對寫在這
) : Parcelable

//定義一個 PhotoItem 的差異比對器，靜態存在，讓處理照片的 adapter 可以引用
object PhotoItemDiffCallback : DiffUtil.ItemCallback<PhotoItem>() {

    override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem.photoId == newItem.photoId
    }

    override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem == newItem //data class 已 override equal 方法，逐項比對
    }
}