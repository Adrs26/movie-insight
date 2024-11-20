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
import com.example.movieinsight.databinding.FragmentViewpagerMovieBinding
import com.example.movieinsight.viewmodel.BookmarkViewModel
import com.example.movieinsight.viewmodel.ViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class BookmarkMovieFragment : Fragment(R.layout.fragment_viewpager_movie) {
    private val binding by viewBinding(FragmentViewpagerMovieBinding::bind)
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
        getBookmarkMovies()
    }

    private fun setupAdapter() {
        bookmarkAdapter = BookmarkAdapter()
        binding.rvMovies.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovies.adapter = bookmarkAdapter
    }

    private fun getBookmarkMovies() {
        bookmarkViewModel.getBookmarkMovies()
    }

    private fun setupObservers() {
        bookmarkViewModel.movies.observe(viewLifecycleOwner) { movies ->
            bookmarkAdapter.submitList(movies)

            if (movies.isNotEmpty()) {
                binding.rvMovies.visibility = View.VISIBLE
                binding.tvFailedToRetrieveData.visibility = View.GONE
            } else {
                binding.rvMovies.visibility = View.GONE
                binding.tvFailedToRetrieveData.text = resources.getString(
                    R.string.your_bookmark_is_still_empty
                )
                binding.tvFailedToRetrieveData.visibility = View.VISIBLE
            }
        }
    }
}