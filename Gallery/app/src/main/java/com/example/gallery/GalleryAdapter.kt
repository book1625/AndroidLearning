package com.example.gallery

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.supercharge.shimmerlayout.ShimmerLayout

//建立一個 adapter class 給 recyclerView 使用，一個 item view 就對一個 view holder
//為了配合 page 的作法，所以是使用 PagedListAdapter 來作中繼
class GalleryAdapter : PagedListAdapter<PhotoItem, CellViewHolder>(PhotoItemDiffCallback) {

    //事件的順序是，先建好 view holder，然後再 bind 到某個 position 的資料

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {

        //動態的畫出一個 cell view ，並結合 view holder

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_cell, parent, false)
        val holder = CellViewHolder(itemView)

        //這裡要處理 item 點擊
        itemView.setOnClickListener {

            //拿到當前 holder 所連結的 data item
            //val item = getItem(holder.bindingAdapterPosition)

            val bundle = Bundle()
            bundle.apply {
                //這裡從 ListAdapter 把當前的資料列抽出來作參數，這裡肯定非空，不然怎麼發生點擊
                putParcelableArrayList("photos", ArrayList(currentList!!))

                //當前的照片索引位置
                putInt("index", holder.bindingAdapterPosition)

                //呼叫換頁並傳參數
                holder.itemView.findNavController().navigate(R.id.action_galleryFragment_to_photoFragment, this)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {

        val item = getItem(position) ?: return

        //設置小圖的高度，讓它先佔好夠用的大小，讓排版不會在圖畫好時一直重排亂跳
        holder.image.layoutParams.height = item.previewHeight

        //等圖時的特效，由於選黑色，它其實沒什麼效果…
        holder.shimmer.apply {
            setShimmerColor(0x55FFFFFF)
            setShimmerAngle(0)
            startShimmerAnimation()
        }

        //載圖
        Glide.with(holder.itemView)
            .load(item.previewUrl)
            //.placeholder(R.drawable.photo_downloading_24) // 沒辦法控大小，它會縮放和 image 一樣，很難看
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.shimmer.stopShimmerAnimation()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false.also { holder.shimmer.stopShimmerAnimation() }
                }
            })
            .into(holder.image)
    }
}

//自定一個 與 adapter item view 成對使用的 view holder
class CellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val shimmer: ShimmerLayout
    val image: ImageView

    init {
        shimmer = itemView.findViewById(R.id.cellShimmerLayout)
        image = itemView.findViewById(R.id.cellImageView)
    }
}
