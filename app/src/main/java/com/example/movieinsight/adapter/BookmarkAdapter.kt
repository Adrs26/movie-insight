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
import com.example.movieinsight.data.local.entity.Data
import com.example.movieinsight.databinding.ItemDataBinding
import com.example.movieinsight.ui.activity.DetailActivity
import com.example.movieinsight.util.DataUtil

@RequiresApi(Build.VERSION_CODES.O)
class BookmarkAdapter : ListAdapter<Data, BookmarkAdapter.ItemViewHolder>(
    object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
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
        fun bind(data: Data) {
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w500${data.posterPath}")
                .centerCrop()
                .into(itemBinding.ivPoster)
            itemBinding.tvScore.text = String.format("%.2f", data.averageVote)

            itemBinding.root.setOnClickListener {
                DataUtil.dataType = data.dataType
                DataUtil.dataId = data.id
                itemView.context.startActivity(Intent(itemView.context, DetailActivity::class.java))
            }
        }
    }
}