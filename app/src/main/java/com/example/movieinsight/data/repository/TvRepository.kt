package com.example.movieinsight.data.repository

import com.example.movieinsight.data.local.dao.DataDao
import com.example.movieinsight.data.local.entity.Data
import com.example.movieinsight.data.remote.api.ApiService
import com.example.movieinsight.data.remote.model.ListHeader
import com.example.movieinsight.data.remote.model.TvSeriesDetails

class TvRepository(private val apiService: ApiService, private val dataDao: DataDao) {
    suspend fun getNowPlayingTvSeries(): ListHeader {
        return apiService.getNowPlayingTvSeries("en-US", 1)
    }

    suspend fun getPopularTvSeries(): ListHeader {
        return apiService.getPopularTvSeries("en-US", 1)
    }

    suspend fun getTopRatedTvSeries(): ListHeader {
        return apiService.getTopRatedTvSeries("en-US", 1)
    }

    suspend fun getTvSeriesByKeyword(keyword: String): ListHeader {
        return apiService.getTvSeriesByKeyword(keyword, "false", "en-US", 1)
    }

    suspend fun getTvSeriesDetails(id: Int): TvSeriesDetails {
        return apiService.getTvSeriesDetails(id, "en-US")
    }

    suspend fun getBookmarkTvSeries(): List<Data> {
        return dataDao.getDataByType("Tv Series")
    }
}