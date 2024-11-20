package com.example.movieinsight.adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieinsight.data.remote.model.ListData
import com.example.movieinsight.databinding.ItemDataBinding
import com.example.movieinsight.ui.activity.DetailActivity
import com.example.movieinsight.util.DataUtil

@RequiresApi(Build.VERSION_CODES.O)
class RecyclerViewAdapter : ListAdapter<ListData, RecyclerViewAdapter.ItemViewHolder>(
    object : DiffUtil.ItemCallback<ListData>() {
        override fun areItemsTheSame(oldItem: ListData, newItem: ListData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListData, newItem: ListData): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDataBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(
        private val itemBinding: ItemDataBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ListData) {
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w500${data.posterPath}")
                .centerCrop()
                .into(itemBinding.ivPoster)
            itemBinding.tvScore.text = String.format("%.2f", data.voteAverage)

            itemBinding.root.setOnClickListener {
                if (data.originCountry == null) {
                    DataUtil.dataType = "Movie"
                } else {
                    DataUtil.dataType = "Tv Series"
                }

                DataUtil.dataId = data.id
                itemView.context.startActivity(Intent(itemView.context, DetailActivity::class.java))
            }
        }
    }
}