package com.example.gallery

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.gallery.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: GalleryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val galleryAdapter = GalleryAdapter()

        var colCount: Int

        //依方向決定畫面一欄顯示幾張
        when (resources.configuration.orientation) {
            ORIENTATION_LANDSCAPE -> 4
            else -> 2
        }.also { colCount = it }

        binding.photoGalleryView.apply {
            adapter = galleryAdapter

            //這裡讓 recyclerView 變成 grid 化，但橫向時這裡應該要有所改變
            //layoutManager = GridLayoutManager(requireContext(), colCount)
            layoutManager = StaggeredGridLayoutManager(colCount, StaggeredGridLayoutManager.VERTICAL)
        }

        viewModel.photoListLive.observe(this.viewLifecycleOwner) {
            galleryAdapter.submitList(it)
            binding.gallerySwipe.isRefreshing = false
        }

        if (viewModel.photoListLive.value == null) viewModel.fetchData()

        binding.gallerySwipe.setOnRefreshListener {
            viewModel.fetchData()
        }
    }
}