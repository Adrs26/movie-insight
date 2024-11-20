package com.example.movieinsight.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.movieinsight.R
import com.example.movieinsight.adapter.ViewPagerAdapter
import com.example.movieinsight.databinding.FragmentBookmarkBinding
import com.google.android.material.tabs.TabLayoutMediator

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private val binding by viewBinding(FragmentBookmarkBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayoutViewPager()
    }

    private fun setupTabLayoutViewPager() {
        val fragments = listOf(BookmarkMovieFragment(), BookmarkTvFragment())
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