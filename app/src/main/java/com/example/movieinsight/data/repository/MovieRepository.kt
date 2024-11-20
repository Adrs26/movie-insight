package com.example.movieinsight.data.repository

import com.example.movieinsight.data.local.dao.DataDao
import com.example.movieinsight.data.local.entity.Data
import com.example.movieinsight.data.remote.api.ApiService
import com.example.movieinsight.data.remote.model.ListHeader
import com.example.movieinsight.data.remote.model.MovieDetails

class MovieRepository(private val apiService: ApiService, private val dataDao: DataDao) {
    suspend fun getNowPlayingMovies(): ListHeader {
        return apiService.getNowPlayingMovies("en-US", 1)
    }

    suspend fun getPopularMovies(): ListHeader {
        return apiService.getPopularMovies("en-US", 1)
    }

    suspend fun getTopRatedMovies(): ListHeader {
        return apiService.getTopRatedMovies("en-US", 1)
    }

    suspend fun getMoviesByKeyword(keyword: String): ListHeader {
        return apiService.getMoviesByKeyword(keyword, "false", "en-US", 1)
    }

    suspend fun getMovieDetails(id: Int): MovieDetails {
        return apiService.getMovieDetails(id, "en-US")
    }

    suspend fun getBookmarkMovies(): List<Data> {
        return dataDao.getDataByType("Movie")
    }

    suspend fun saveDataToBookmark(data: Data) {
        dataDao.insertData(data)
    }

    suspend fun removeDataFromBookmark(id: Int) {
        dataDao.deleteData(id)
    }

    suspend fun checkIsDataExistInBookmark(id: Int): Int {
        return dataDao.isDataExist(id)
    }
}