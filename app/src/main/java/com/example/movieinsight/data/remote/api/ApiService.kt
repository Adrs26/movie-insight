package com.example.movieinsight.data.remote.api

import com.example.movieinsight.data.remote.model.ListHeader
import com.example.movieinsight.data.remote.model.MovieDetails
import com.example.movieinsight.data.remote.model.TvSeriesDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): ListHeader

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): ListHeader

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): ListHeader

    @GET("tv/popular")
    suspend fun getPopularTvSeries(
        @Query("language") language: String,
        @Query("page") page: Int
    ): ListHeader

    @GET("tv/top_rated")
    suspend fun getTopRatedTvSeries(
        @Query("language") language: String,
        @Query("page") page: Int
    ): ListHeader

    @GET("tv/on_the_air")
    suspend fun getNowPlayingTvSeries(
        @Query("language") language: String,
        @Query("page") page: Int
    ): ListHeader

    @GET("search/movie")
    suspend fun getMoviesByKeyword(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): ListHeader

    @GET("search/tv")
    suspend fun getTvSeriesByKeyword(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): ListHeader

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Query("language") language: String,
    ): MovieDetails

    @GET("tv/{id}")
    suspend fun getTvSeriesDetails(
        @Path("id") id: Int,
        @Query("language") language: String,
    ): TvSeriesDetails
}