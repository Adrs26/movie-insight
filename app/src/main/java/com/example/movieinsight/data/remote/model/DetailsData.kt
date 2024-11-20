package com.example.movieinsight.data.remote.model

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val genres: List<Genres>,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanies>,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountries>,
    val status: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val popularity: Double,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val overview: String
)

data class TvSeriesDetails(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("poster_path")
    val posterPath: String,
    val name: String,
    val genres: List<Genres>,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanies>,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountries>,
    val status: String,
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int,
    val popularity: Double,
    @SerializedName("vote_average")
    val voteAverage: Double,
    val overview: String
)

data class Genres(
    val name: String
)

data class ProductionCompanies(
    val name: String
)

data class ProductionCountries(
    val name: String
)
