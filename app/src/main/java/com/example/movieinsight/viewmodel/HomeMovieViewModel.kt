package com.example.movieinsight.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieinsight.R
import com.example.movieinsight.data.remote.model.ListData
import com.example.movieinsight.data.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeMovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private val _menuId = MutableLiveData<Int>().apply { value = 1 }
    val menuId: LiveData<Int> = _menuId

    private val _nowPlayingStyle = MutableLiveData<Pair<Int, Int>>()
    val nowPlayingStyle: LiveData<Pair<Int, Int>> = _nowPlayingStyle

    private val _popularStyle = MutableLiveData<Pair<Int, Int>>()
    val popularStyle: LiveData<Pair<Int, Int>> = _popularStyle

    private val _topRatedStyle = MutableLiveData<Pair<Int, Int>>()
    val topRatedStyle: LiveData<Pair<Int, Int>> = _topRatedStyle

    private val _movies = MutableLiveData<List<ListData>>()
    val movies: LiveData<List<ListData>> = _movies

    private val _exception = MutableLiveData<Boolean>()
    val exception: LiveData<Boolean> = _exception

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun changeMenu(menuId: Int) {
        _menuId.value = menuId
        updateMenuStyles(menuId)
    }

    private fun updateMenuStyles(menuId: Int) {
        _nowPlayingStyle.value = getMenuStyle(menuId, 1)
        _popularStyle.value = getMenuStyle(menuId, 2)
        _topRatedStyle.value = getMenuStyle(menuId, 3)
    }

    private fun getMenuStyle(menuId: Int, targetMenuId: Int): Pair<Int, Int> {
        return if (menuId == targetMenuId) {
            Pair(R.drawable.btn_border_checked, R.color.white)
        } else {
            Pair(R.drawable.btn_border_unchecked, R.color.secondary_color)
        }
    }

    fun getNowPlayingMovies() {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _movies.postValue(movieRepository.getNowPlayingMovies().results)
                _exception.postValue(false)
            } catch (e: Exception) {
                _exception.postValue(true)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getPopularMovies() {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _movies.postValue(movieRepository.getPopularMovies().results)
                _exception.postValue(false)
            } catch (e: Exception) {
                _exception.postValue(true)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getTopRatedMovies() {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _movies.postValue(movieRepository.getTopRatedMovies().results)
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