package com.example.movieinsight.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieinsight.data.remote.model.ListData
import com.example.movieinsight.data.repository.MovieRepository
import com.example.movieinsight.data.repository.TvRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) : ViewModel() {
    private val _movies = MutableLiveData<List<ListData>>()
    val movies: LiveData<List<ListData>> = _movies

    private val _tvSeries = MutableLiveData<List<ListData>>()
    val tvSeries: LiveData<List<ListData>> = _tvSeries

    private val _exception = MutableLiveData<Boolean>()
    val exception: LiveData<Boolean> = _exception

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getMoviesByKeyword(keyword: String) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _movies.postValue(movieRepository.getMoviesByKeyword(keyword).results)
                _exception.postValue(false)
            } catch (e: Exception) {
                _exception.postValue(true)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getTvSeriesByKeyword(keyword: String) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _tvSeries.postValue(tvRepository.getTvSeriesByKeyword(keyword).results)
                _exception.postValue(false)
            } catch (e: Exception) {
                _exception.postValue(true)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun resetExceptionValue() {
        _exception.value = false
    }
}