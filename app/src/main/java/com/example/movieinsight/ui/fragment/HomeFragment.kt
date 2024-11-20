package com.example.movieinsight.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.movieinsight.R
import com.example.movieinsight.adapter.ViewPagerAdapter
import com.example.movieinsight.databinding.FragmentHomeBinding
import com.example.movieinsight.ui.activity.SearchActivity
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchButton()
        setupTabLayoutViewPager()
    }

    private fun setupSearchButton() {
        binding.btnSearch.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }
    }

    private fun setupTabLayoutViewPager() {
        val fragments = listOf(HomeMovieFragment(), HomeTvFragment())
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle, fragments)
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