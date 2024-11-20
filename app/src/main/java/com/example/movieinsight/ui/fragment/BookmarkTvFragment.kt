package com.example.movieinsight.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.movieinsight.R
import com.example.movieinsight.adapter.BookmarkAdapter
import com.example.movieinsight.data.local.LocalDatabase
import com.example.movieinsight.data.remote.api.ApiClient
import com.example.movieinsight.data.repository.MovieRepository
import com.example.movieinsight.data.repository.TvRepository
import com.example.movieinsight.databinding.FragmentViewpagerTvBinding
import com.example.movieinsight.viewmodel.BookmarkViewModel
import com.example.movieinsight.viewmodel.ViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class BookmarkTvFragment : Fragment(R.layout.fragment_viewpager_tv) {
    private val binding by viewBinding(FragmentViewpagerTvBinding::bind)
    private val bookmarkViewModel by lazy {
        val factory = ViewModelFactory(
            MovieRepository(
                ApiClient.apiClient,
                LocalDatabase.getDatabase(requireContext()).getDataDao()
            ),
            TvRepository(
                ApiClient.apiClient,
                LocalDatabase.getDatabase(requireContext()).getDataDao()
            )
        )
        ViewModelProvider(requireActivity(), factory)[BookmarkViewModel::class.java]
    }
    private lateinit var bookmarkAdapter: BookmarkAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        getBookmarkTvSeries()
    }

    private fun setupAdapter() {
        bookmarkAdapter = BookmarkAdapter()
        binding.rvTvSeries.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvTvSeries.adapter = bookmarkAdapter
    }

    private fun getBookmarkTvSeries() {
        bookmarkViewModel.getBookmarkTvSeries()
    }

    private fun setupObservers() {
        bookmarkViewModel.tvSeries.observe(viewLifecycleOwner) { tvSeries ->
            bookmarkAdapter.submitList(tvSeries)

            if (tvSeries.isNotEmpty()) {
                binding.rvTvSeries.visibility = View.VISIBLE
                binding.tvFailedToRetrieveData.visibility = View.GONE
            } else {
                binding.rvTvSeries.visibility = View.GONE
                binding.tvFailedToRetrieveData.text = resources.getString(
                    R.string.your_bookmark_is_still_empty
                )
                binding.tvFailedToRetrieveData.visibility = View.VISIBLE
            }
        }
    }
}