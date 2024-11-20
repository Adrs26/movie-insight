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
import com.example.movieinsight.databinding.FragmentHomeTvBinding
import com.example.movieinsight.viewmodel.HomeTvViewModel
import com.example.movieinsight.viewmodel.ViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
class HomeTvFragment : Fragment(R.layout.fragment_home_tv) {
    private val binding by viewBinding(FragmentHomeTvBinding::bind)
    private val homeTvViewModel by lazy {
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
        ViewModelProvider(requireActivity(), factory)[HomeTvViewModel::class.java]
    }
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupAdapter()
        setupObservers()
    }

    private fun setupMenu() {
        binding.tvOnTheAir.setOnClickListener {
            homeTvViewModel.changeMenu(ON_THE_AIR)
        }
        binding.tvPopular.setOnClickListener {
            homeTvViewModel.changeMenu(POPULAR)
        }
        binding.tvTopRated.setOnClickListener {
            homeTvViewModel.changeMenu(TOP_RATED)
        }
    }

    private fun setupAdapter() {
        recyclerViewAdapter = RecyclerViewAdapter()
        binding.rvTvSeries.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvTvSeries.adapter = recyclerViewAdapter
    }

    private fun setupObservers() {
        homeTvViewModel.menuId.observe(viewLifecycleOwner) { menuId ->
            when (menuId) {
                ON_THE_AIR -> homeTvViewModel.getNowPlayingTvSeries()
                POPULAR -> homeTvViewModel.getPopularTvSeries()
                TOP_RATED -> homeTvViewModel.getTopRatedTvSeries()
            }
        }

        homeTvViewModel.onTheAirStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupMenuStyle(binding.tvOnTheAir, background, textColor)
        }
        homeTvViewModel.popularStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupMenuStyle(binding.tvPopular, background, textColor)
        }
        homeTvViewModel.topRatedStyle.observe(viewLifecycleOwner) { (background, textColor) ->
            setupMenuStyle(binding.tvTopRated, background, textColor)
        }

        homeTvViewModel.tvSeries.observe(viewLifecycleOwner) { tvSeries ->
            recyclerViewAdapter.submitList(tvSeries) {
                binding.rvTvSeries.scrollToPosition(0)
            }
            binding.rvTvSeries.visibility = View.VISIBLE
            binding.tvFailedToRetrieveData.visibility = View.GONE
        }

        homeTvViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.rvTvSeries.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        homeTvViewModel.exception.observe(viewLifecycleOwner) { exception ->
            if (exception) {
                showToast(resources.getString(R.string.failed_to_retrieve_data))
                binding.tvFailedToRetrieveData.visibility = View.VISIBLE
                homeTvViewModel.resetExceptionValue()
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
        const val ON_THE_AIR = 1
        const val POPULAR = 2
        const val TOP_RATED = 3
    }
}