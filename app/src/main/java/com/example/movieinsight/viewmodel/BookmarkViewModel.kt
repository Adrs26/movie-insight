package com.example.movieinsight.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieinsight.data.local.entity.Data
import com.example.movieinsight.data.repository.MovieRepository
import com.example.movieinsight.data.repository.TvRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) : ViewModel() {
    private val _movies = MutableLiveData<List<Data>>()
    val movies: LiveData<List<Data>> = _movies

    private val _tvSeries = MutableLiveData<List<Data>>()
    val tvSeries: LiveData<List<Data>> = _tvSeries

    fun getBookmarkMovies() = viewModelScope.launch(Dispatchers.IO) {
        _movies.postValue(movieRepository.getBookmarkMovies())
    }

    fun getBookmarkTvSeries() = viewModelScope.launch(Dispatchers.IO) {
        _tvSeries.postValue(tvRepository.getBookmarkTvSeries())
    }
}