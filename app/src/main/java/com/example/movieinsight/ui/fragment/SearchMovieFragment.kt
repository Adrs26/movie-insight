package com.example.movieinsight.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.movieinsight.R
import com.example.movieinsight.adapter.RecyclerViewAdapter
import com.example.movieinsight.data.local.LocalDatabase
import com.example.movieinsight.data.remote.api.ApiClient
import com.example.movieinsight.data.repository.MovieRepository
import com.example.movieinsight.data.repository.TvRepository
import com.example.movieinsight.databinding.FragmentViewpagerMovieBinding
import com.example.movieinsight.viewmodel.SearchViewModel
import com.example.movieinsight.viewmodel.ViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class SearchMovieFragment : Fragment(R.layout.fragment_viewpager_movie) {
    private val binding by viewBinding(FragmentViewpagerMovieBinding::bind)
    private val searchViewModel by lazy {
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
        ViewModelProvider(requireActivity(), factory)[SearchViewModel::class.java]
    }
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupObservers()
    }

    private fun setupAdapter() {
        recyclerViewAdapter = RecyclerViewAdapter()
        binding.rvMovies.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovies.adapter = recyclerViewAdapter
    }

    private fun setupObservers() {
        searchViewModel.movies.observe(viewLifecycleOwner) { movies ->
            recyclerViewAdapter.submitList(movies) {
                binding.rvMovies.scrollToPosition(0)
            }

            if (movies.isNotEmpty()) {
                binding.rvMovies.visibility = View.VISIBLE
                binding.tvFailedToRetrieveData.visibility = View.GONE
            } else {
                binding.rvMovies.visibility = View.GONE
                binding.tvFailedToRetrieveData.text = resources.getString(
                    R.string.keyword_movies_not_found
                )
                binding.tvFailedToRetrieveData.visibility = View.VISIBLE
            }
        }

        searchViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvMovies.visibility = View.GONE
                binding.tvFailedToRetrieveData.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        searchViewModel.exception.observe(viewLifecycleOwner) { exception ->
            if (exception) {
                showToast(resources.getString(R.string.failed_to_retrieve_data))
                binding.tvFailedToRetrieveData.text = resources.getString(
                    R.string.failed_to_retrieve_data
                )
                binding.tvFailedToRetrieveData.visibility = View.VISIBLE
                searchViewModel.resetExceptionValue()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}