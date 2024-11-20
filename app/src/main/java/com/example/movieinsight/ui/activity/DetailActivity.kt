package com.example.movieinsight.ui.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.movieinsight.R
import com.example.movieinsight.data.local.LocalDatabase
import com.example.movieinsight.data.local.entity.Data
import com.example.movieinsight.data.remote.api.ApiClient
import com.example.movieinsight.data.repository.MovieRepository
import com.example.movieinsight.data.repository.TvRepository
import com.example.movieinsight.databinding.ActivityDetailBinding
import com.example.movieinsight.util.DataUtil
import com.example.movieinsight.util.DateUtil.convertDateToEnglishFormat
import com.example.movieinsight.viewmodel.DetailViewModel
import com.example.movieinsight.viewmodel.ViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SourceLockedOrientationActivity")
class DetailActivity : AppCompatActivity(R.layout.activity_detail) {
    private val binding by viewBinding(ActivityDetailBinding::bind)
    private val detailViewModel by lazy {
        val factory = ViewModelFactory(
            MovieRepository(
                ApiClient.apiClient,
                LocalDatabase.getDatabase(this).getDataDao()
            ),
            TvRepository(
                ApiClient.apiClient,
                LocalDatabase.getDatabase(this).getDataDao()
            )
        )
        ViewModelProvider(this, factory)[DetailViewModel::class.java]
    }
    private var isBookmark = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButton()
        setupOrientation()
        getDataDetails()
        checkIsDataExistInBookmark()
        setupObservers()
    }

    private fun setupButton() {
        binding.ibBack.setOnClickListener {
            finish()
        }
        binding.ibBookmark.setOnClickListener {
            if (!isBookmark) {
                binding.ibBookmark.setImageResource(R.drawable.ic_bookmark_checked)
                saveDataToBookmark()
            } else {
                binding.ibBookmark.setImageResource(R.drawable.ic_bookmark_unchecked)
                removeDataFromBookmark()
            }
            isBookmark = !isBookmark
        }
    }

    private fun setupOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun getDataDetails() {
        if (DataUtil.dataType == "Movie") {
            detailViewModel.getMoviesDetails(DataUtil.dataId)
            setStatusSeasonsAndEpisodesData(View.GONE)
        } else {
            detailViewModel.getTvSeriesDetails(DataUtil.dataId)
            setStatusSeasonsAndEpisodesData(View.VISIBLE)
        }
    }

    private fun checkIsDataExistInBookmark() {
        detailViewModel.checkIsDataExistInBookmark(DataUtil.dataId)
    }

    private fun setupObservers() {
        if (DataUtil.dataType == "Movie") {
            detailViewModel.movieDetails.observe(this) { movieDetails ->
                binding.apply {
                    dataContainer.visibility = View.VISIBLE

                    setupGlide(movieDetails.backdropPath, ivPosterBackground)
                    setupGlide(movieDetails.posterPath, ivPoster)

                    tvTitle.text = movieDetails.originalTitle
                    tvGenres.text = movieDetails.genres.toDisplayText { it.name }
                    tvProduction.text = movieDetails.productionCompanies.toDisplayText { it.name }
                    tvCountries.text = movieDetails.productionCountries.toDisplayText { it.name }
                    tvStatus.text = movieDetails.status
                    tvReleasedDate.text = convertDateToEnglishFormat(movieDetails.releaseDate)
                    tvPopularity.text = resources.getString(
                        R.string.popularity,
                        movieDetails.popularity.toInt()
                    )
                    tvOverview.text = movieDetails.overview

                    DataUtil.dataMovieDetails = movieDetails
                }
            }
        } else {
            detailViewModel.tvSeriesDetails.observe(this) { tvSeriesDetails ->
                binding.apply {
                    dataContainer.visibility = View.VISIBLE

                    setupGlide(tvSeriesDetails.backdropPath, ivPosterBackground)
                    setupGlide(tvSeriesDetails.posterPath, ivPoster)

                    tvTitle.text = tvSeriesDetails.name
                    tvGenres.text = tvSeriesDetails.genres.toDisplayText { it.name }
                    tvProduction.text = tvSeriesDetails.productionCompanies.toDisplayText { it.name }
                    tvCountries.text = tvSeriesDetails.productionCountries.toDisplayText { it.name }
                    tvStatus.text = tvSeriesDetails.status
                    tvReleasedDate.text = convertDateToEnglishFormat(tvSeriesDetails.firstAirDate)
                    tvNumberOfSeasons.text = resources.getString(
                        R.string.number_of_seasons,
                        tvSeriesDetails.numberOfSeasons
                    )
                    tvNumberOfEpisodes.text = resources.getString(
                        R.string.number_of_episodes,
                        tvSeriesDetails.numberOfEpisodes
                    )
                    tvPopularity.text = resources.getString(
                        R.string.popularity,
                        tvSeriesDetails.popularity.toInt()
                    )
                    tvOverview.text = tvSeriesDetails.overview

                    DataUtil.dataTvSeriesDetails = tvSeriesDetails
                }
            }
        }

        detailViewModel.isDataExistInBookmark.observe(this) { isExist ->
            isBookmark = isExist

            if (isExist) {
                binding.ibBookmark.setImageResource(R.drawable.ic_bookmark_checked)
            } else {
                binding.ibBookmark.setImageResource(R.drawable.ic_bookmark_unchecked)
            }
        }

        detailViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.dataContainer.visibility = View.GONE
                binding.tvFailedToRetrieveData.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        detailViewModel.exception.observe(this) { exception ->
            if (exception) {
                showToast(resources.getString(R.string.failed_to_retrieve_data))
                binding.tvFailedToRetrieveData.visibility = View.VISIBLE
                detailViewModel.resetExceptionValue()
            }
        }
    }

    private fun saveDataToBookmark() {
        if (DataUtil.dataType == "Movie") {
            detailViewModel.saveDataToBookmark(Data(
                DataUtil.dataId,
                DataUtil.dataMovieDetails!!.posterPath,
                DataUtil.dataMovieDetails!!.voteAverage,
                DataUtil.dataType
            ))
        } else {
            detailViewModel.saveDataToBookmark(
                Data(
                    DataUtil.dataId,
                    DataUtil.dataTvSeriesDetails!!.posterPath,
                    DataUtil.dataTvSeriesDetails!!.voteAverage,
                    DataUtil.dataType
            ))
        }
        showToast(resources.getString(R.string.successfully_save_to_bookmark))
    }

    private fun removeDataFromBookmark() {
        detailViewModel.removeDataFromBookmark(DataUtil.dataId)
        showToast(resources.getString(R.string.successfully_remove_from_bookmark))
    }

    private fun setStatusSeasonsAndEpisodesData(status: Int) {
        binding.apply {
            tvNumberOfSeasonsText.visibility = status
            tvNumberOfSeasons.visibility = status
            tvNumberOfEpisodesText.visibility = status
            tvNumberOfEpisodes.visibility = status
        }
    }

    private fun <T> List<T>.toDisplayText(transform: (T) -> String): String {
        return if (size == 1) transform(this[0]) else joinToString { transform(it) }
    }

    private fun setupGlide(path: String, target: ImageView) {
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500$path")
            .centerCrop()
            .into(target)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}