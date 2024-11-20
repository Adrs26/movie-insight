package com.example.movieinsight.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
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
import com.example.movieinsight.databinding.FragmentHomeMovieBinding
import com.example.movieinsight.viewmodel.HomeMovieViewModel
import com.example.movieinsight.viewmodel.ViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class HomeMovieFragment : Fragment(R.layout.fragment_home_movie) {
    private val binding by viewBinding(FragmentHomeMovieBinding::bind)
    private val homeMovieViewModel by lazy {
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
        ViewModelProvider(requireActivity(), factory)[HomeMovieViewModel::class.java]
    }
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupAdapter()
        setupObservers()
    }

    private fun setupMenu() {
        binding.tvNowPlaying.setOnClickListener {
            homeMovieViewModel.changeMenu(NOW_PLAYING)
        }
        binding.tvPopular.setOnClickListener {
            homeMovieViewModel.changeMenu(POPULAR)
        }
        binding.tvTopRated.setOnClickListener {
            homeMovieViewModel.changeMenu(TOP_RATED)
        }
    }

    private fun setupAdapter() {
        recyclerViewAdapter = RecyclerViewAdapter()
        binding.rvMovies.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovies.adapter = recyclerViewAdapter
    }

    private fun setupObservers() {
        homeMovieViewModel.menuId.observe(viewLifecycleOwner) { menuId ->
            when (menuId) {
                NOW_PLAYING -> homeMovieViewModel.getNowPlayingMovies()
                POPULAR -> homeMovieViewModel.getPopularMovies()
                TOP_RATED -> homeMovieViewModel.getTopRatedMovies()
            }
        }

        homeMovieViewModel.nowPlayingStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupMenuStyle(binding.tvNowPlaying, background, textColor)
        }
        homeMovieViewModel.popularStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupMenuStyle(binding.tvPopular, background, textColor)
        }
        homeMovieViewModel.topRatedStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupMenuStyle(binding.tvTopRated, background, textColor)
        }

        homeMovieViewModel.movies.observe(viewLifecycleOwner) { movies ->
            recyclerViewAdapter.submitList(movies) {
                binding.rvMovies.scrollToPosition(0)
            }
            binding.rvMovies.visibility = View.VISIBLE
            binding.tvFailedToRetrieveData.visibility = View.GONE
        }

        homeMovieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvMovies.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        homeMovieViewModel.exception.observe(viewLifecycleOwner) { exception ->
            if (exception) {
                showToast(resources.getString(R.string.failed_to_retrieve_data))
                binding.tvFailedToRetrieveData.visibility = View.VISIBLE
                homeMovieViewModel.resetExceptionValue()
            }
        }
    }

    private fun setupMenuStyle(menu: TextView, background: Int, textColor: Int) {
        menu.background = ContextCompat.getDrawable(requireContext(), background)
        menu.setTextColor(ContextCompat.getColor(requireContext(), textColor))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val NOW_PLAYING = 1
        const val POPULAR = 2
        const val TOP_RATED = 3
    }
}