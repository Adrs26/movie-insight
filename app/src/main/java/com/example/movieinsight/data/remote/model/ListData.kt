package com.example.movieinsight.data.remote.model

import com.google.gson.annotations.SerializedName

data class ListHeader(
    val page: Int,
    val results: List<ListData>
)

data class ListData(
    val id: Int,
    @SerializedName("origin_country")
    val originCountry: List<String>?,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double
)
