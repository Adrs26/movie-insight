package com.example.movieinsight.ui.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.movieinsight.R
import com.example.movieinsight.adapter.ViewPagerAdapter
import com.example.movieinsight.data.local.LocalDatabase
import com.example.movieinsight.data.remote.api.ApiClient
import com.example.movieinsight.data.repository.MovieRepository
import com.example.movieinsight.data.repository.TvRepository
import com.example.movieinsight.databinding.ActivitySearchBinding
import com.example.movieinsight.ui.fragment.SearchMovieFragment
import com.example.movieinsight.ui.fragment.SearchTvFragment
import com.example.movieinsight.viewmodel.SearchViewModel
import com.example.movieinsight.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SourceLockedOrientationActivity")
class SearchActivity : AppCompatActivity(R.layout.activity_search) {
    private val binding by viewBinding(ActivitySearchBinding::bind)
    private val searchViewModel by lazy {
        val factory = ViewModelFactory(
            MovieRepository(
                ApiClient.apiClient,
                LocalDatabase.getDatabase(this).getDataDao()
            ),
            TvRepository(
                ApiClient.apiClient,
                LocalDatabase.getDatabase(this).getDataDao()
            )
        )
        ViewModelProvider(this, factory)[SearchViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButton()
        setupOrientation()
        setupSearchView()
        setupTabLayoutViewPager()
    }

    private fun setupButton() {
        binding.ibBack.setOnClickListener {
            finish()
        }
    }

    private fun setupOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setupSearchView() {
        binding.apply {
            searchView.queryHint = "Search title"
            searchView.findViewById<View>(androidx.appcompat.R.id.search_plate).background = null

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        searchViewModel.getMoviesByKeyword(query)
                        searchViewModel.getTvSeriesByKeyword(query)

                        binding.apply {
                            viewLine.visibility = View.GONE
                            tabLayout.visibility = View.VISIBLE
                            viewPager.visibility = View.VISIBLE
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun setupTabLayoutViewPager() {
        val fragments = listOf(SearchMovieFragment(), SearchTvFragment())
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle, fragments)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> resources.getString(R.string.movies_tab)
                1 -> resources.getString(R.string.tv_series_tab)
                else -> null
            }
        }.attach()
    }
}