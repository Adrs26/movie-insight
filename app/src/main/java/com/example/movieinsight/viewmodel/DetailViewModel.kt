package com.example.movieinsight.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieinsight.data.local.entity.Data
import com.example.movieinsight.data.remote.model.MovieDetails
import com.example.movieinsight.data.remote.model.TvSeriesDetails
import com.example.movieinsight.data.repository.MovieRepository
import com.example.movieinsight.data.repository.TvRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) : ViewModel() {
    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    private val _tvSeriesDetails = MutableLiveData<TvSeriesDetails>()
    val tvSeriesDetails: LiveData<TvSeriesDetails> = _tvSeriesDetails

    private val _isDataExistInBookmark = MutableLiveData<Boolean>()
    val isDataExistInBookmark: LiveData<Boolean> = _isDataExistInBookmark

    private val _exception = MutableLiveData<Boolean>()
    val exception: LiveData<Boolean> = _exception

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getMoviesDetails(id: Int) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _movieDetails.postValue(movieRepository.getMovieDetails(id))
                _exception.postValue(false)
            } catch (e: Exception) {
                _exception.postValue(true)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getTvSeriesDetails(id: Int) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _tvSeriesDetails.postValue(tvRepository.getTvSeriesDetails(id))
                _exception.postValue(false)
            } catch (e: Exception) {
                _exception.postValue(true)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun checkIsDataExistInBookmark(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        val isExist = movieRepository.checkIsDataExistInBookmark(id)
        if (isExist > 0) {
            _isDataExistInBookmark.postValue(true)
        } else {
            _isDataExistInBookmark.postValue(false)
        }
    }

    fun saveDataToBookmark(data: Data) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.saveDataToBookmark(data)
    }

    fun removeDataFromBookmark(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        movieRepository.removeDataFromBookmark(id)
    }

    fun resetExceptionValue() {
        _exception.value = false
    }
}