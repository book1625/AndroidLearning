package com.example.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.gallery.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment() {

    private val logTag = "mylog"

    private var _binding: FragmentPhotoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //建 adapter 並將拿到的資料填給入
        val photos = arguments?.getParcelableArrayList<PhotoItem>("photos")
        PhotoAdapter().apply {
            binding.photoPager.adapter = this
            submitList(photos)
        }

        //連動換頁時，更改索引字的顯示
        binding.photoPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                "$position / ${photos?.count()}".also { binding.photoIndexer.text = it }
            }
        })

        //指定到想要的頁，不帶滑頁特效，帶的話有時沒換頁
        val index = arguments?.getInt("index")
        Log.d(logTag, "index:${index} ")
        binding.photoPager.setCurrentItem(index ?: 0, false)

        //讓 view pager 改支援直向的捲動
        //binding.photoPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}