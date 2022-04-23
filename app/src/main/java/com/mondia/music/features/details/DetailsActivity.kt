package com.mondia.music.features.details

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mondia.music.base.repo.ImageRepo
import com.mondia.music.databinding.ActivityDetailsBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DetailsActivity(private val repo: ImageRepo = ImageRepo()) : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showSongDetails()
        actions()
    }

    private fun actions() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun showSongDetails() {
        loadLargeImage(intent.getStringExtra("song_cover"))
        val songTitle = intent.getStringExtra("song_title")
        binding.tvSongTitle.text = songTitle
        binding.tvToolbarTitle.text = songTitle
        binding.tvSongType.text = intent.getStringExtra("song_type")
        binding.tvSongPublishDate.text = intent.getStringExtra("song_publish_date")!!.split("T")[0]
    }

    private fun loadLargeImage(largeCover: String?) {
        largeCover?.let {
            MainScope().launch {
                Log.d("load image", "url: $it")
                val bitmap = repo.getBitmap(it)
                binding.ivSongCover.setImageBitmap(bitmap)
            }
        }
    }
}