package com.example.movieinsight.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movieinsight.data.repository.MovieRepository
import com.example.movieinsight.data.repository.TvRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeMovieViewModel::class.java) -> {
                HomeMovieViewModel(movieRepository) as T
            }
            modelClass.isAssignableFrom(HomeTvViewModel::class.java) -> {
                HomeTvViewModel(tvRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(movieRepository, tvRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(movieRepository, tvRepository) as T
            }
            modelClass.isAssignableFrom(BookmarkViewModel::class.java) -> {
                BookmarkViewModel(movieRepository, tvRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}