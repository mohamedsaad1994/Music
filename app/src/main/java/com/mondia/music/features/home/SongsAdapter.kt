package com.mondia.music.features.home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mondia.music.base.network.response.Song
import com.mondia.music.base.repo.ImageRepo
import com.mondia.music.databinding.ItemSearchResultBinding
import com.mondia.music.features.details.DetailsActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SongsAdapter(private val repo: ImageRepo = ImageRepo()) :
    RecyclerView.Adapter<SongsAdapter.SongsViewHolder>() {
    private val data = ArrayList<Song>()

    fun addData(result: List<Song>) {
        data.clear()
        data.addAll(result)
        notifyItemRangeChanged(0, result.size - 1)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SongsViewHolder {
        val binding =
            ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return SongsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        holder.bind(data[position], repo)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class SongsViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Song, repo: ImageRepo) {
            val context = binding.root.context
            binding.tvArtistName.text = item.artist?.name
            binding.tvSongTitle.text = item.title
            binding.tvType.text = item.type
            loadMediumImage(item, repo)
            binding.root.setOnClickListener {
                context.startActivity(Intent(context, DetailsActivity::class.java).apply {
                    putExtra("song_title", item.title)
                    putExtra("song_publish_date", item.publishDate)
                    putExtra("song_type", item.type)
                    putExtra("song_cover", item.cover?.large)
                })
            }
        }

        private fun loadMediumImage(item: Song, repo: ImageRepo) {
            if (item.cover != null && item.cover.mediumBitmap == null) {
                item.cover.medium?.let {
                    MainScope().launch {
                        Log.d("load image", "url: $it")
                        val bitmap = repo.getBitmap(it)
                        item.cover.mediumBitmap = bitmap
                        binding.ivSongCover.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
}