package com.example.gallery

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//這個 adapter 用來提供提單一照片頁的 ViewPager 使用，因為 ViewPager 內部其實就是 RecyclerView
//差異比對的部份就直接共用了
class PhotoAdapter : ListAdapter<PhotoItem, PhotoViewHolder>(PhotoItemDiffCallback) {

    private val logTag: String = "mylog"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.photo_cell, parent, false).apply {
            return PhotoViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = getItem(position)

        //產生顯示資訊
        "${photo.imageWidth} * ${photo.imageHeight}".also { holder.size.text = it }
        holder.likes.text = "${photo.likes}"

        //這裡要 glide 圖進來
        Glide.with(holder.itemView)
            .load(photo.imageUrl)
            .placeholder(R.drawable.photo_downloading_24)
            .into(holder.photo)

        holder.download.setOnClickListener {
            // 在 API 29 以後，寫入 ex-storage 的權限預設是可以的

            Log.d(logTag, "[${Thread.currentThread().id} / ${Looper.getMainLooper().thread.id}] before save photo")

            //往上找元件的 lifecycle owner，請它在其 scope 中運行指定的代碼
            holder.view.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                Log.d(logTag, "[${Thread.currentThread().id} / ${Looper.getMainLooper().thread.id}] launch section save photo ")
                savePhoto(holder, photo).also {
                    Log.d(logTag, "[${Thread.currentThread().id} / ${Looper.getMainLooper().thread.id}] also section of save photo => $it ")

                    //很明顯這裡是主線程，依然可用 ui 操作
                    Toast.makeText(holder.view.context, "Save Image => $it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 以下函式這段碼掛載到 io 線程上去運行，也因此，這個函式需要被指定為 suspend
    // suspend 很像是 c# 的 async 關鍵字，而 witContext 這段就很像一個 Task 而且被指定在 IO 線程上運行
    // 同樣的，由於這個方法具 suspend，所以呼叫者需以非同步處理方式來叫用它
    // kotlin 也是有 async 的，它比較像 C# new 一個 Task 的用法，然後調用它的 await 方法來等到結果
    // https://developer.android.com/kotlin/coroutines/coroutines-adv?hl=zh-tw

    private suspend fun savePhoto(holder: PhotoViewHolder, photoItem: PhotoItem): Boolean {
        return withContext(Dispatchers.IO) {
            Log.d(logTag, "[${Thread.currentThread().id} / ${Looper.getMainLooper().thread.id}] inner save photo")

            //測試非主線程的效果時使用，可以證明長時下載不會卡住畫面操作
            //delay(5000)

            //建立圖物件，但忽略細部設定，只保留原圖長寬
            val bitmap = holder.photo.drawable.toBitmap(photoItem.imageWidth, photoItem.imageHeight)

            //建立圖的位置，它使用了統一的媒體位置
            val uri: Uri = holder.view.context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues()) ?: return@withContext false

            //建立寫入串流，寫入圖檔
            holder.view.context.contentResolver.openOutputStream(uri).use {
                return@withContext bitmap.run { compress(Bitmap.CompressFormat.JPEG, 90, it) }
            }
        }

        //試寫一段平行 task
        //        coroutineScope {
        //            var tasks = listOf(
        //                async {/* job 1 */},
        //                async {/* job 2 */}
        //            )
        //            tasks.awaitAll()
        //        }
    }
}

//PhotoAdapter 成對的 view holder
class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val view: View
    val photo: ImageView
    val size: TextView
    val likes: TextView
    val download: ImageView

    init {
        view = itemView
        photo = itemView.findViewById(R.id.photoImageView)
        size = itemView.findViewById(R.id.sizeText)
        likes = itemView.findViewById(R.id.likesText)
        download = itemView.findViewById(R.id.downloadButton)
    }
}