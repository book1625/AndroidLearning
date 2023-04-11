package com.example.gallery

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.gallery.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<GalleryViewModel>()

    //    override fun onCreate(savedInstanceState: Bundle?) {
    //        super.onCreate(savedInstanceState)
    //
    //        //被上面那個神奇的寫法取代了…
    //        //viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
    //    }

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

        viewModel.pagedLivePhotos.observe(this.viewLifecycleOwner) {
            galleryAdapter.submitList(it)
            binding.gallerySwipe.isRefreshing = false
        }

        binding.gallerySwipe.setOnRefreshListener {
            viewModel.resetQuery()
        }
    }
}