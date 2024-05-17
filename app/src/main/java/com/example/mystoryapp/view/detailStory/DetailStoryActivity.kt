package com.example.mystoryapp.view.detailStory

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detailStory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val photoUrl = intent.getStringExtra(EXTRA_PHOTO_URL)
        val createdAt = intent.getStringExtra(EXTRA_CREATED_AT)
        val name = intent.getStringExtra(EXTRA_NAME)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val lon = intent.getDoubleExtra(EXTRA_LON, 0.0)
        val id = intent.getStringExtra(EXTRA_ID)
        val lat = intent.getDoubleExtra(EXTRA_LAT, 0.0)

        DetailView(photoUrl, createdAt, name, description, lon, id, lat)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun DetailView(photoUrl: String?, createdAt: String?, name: String?, description: String?, lon: Double, id: String?, lat: Double) {
        Glide.with(this@DetailStoryActivity)
            .load(photoUrl)
            .into(binding.ivStory)

        binding.tvNama.text = name
        binding.tvDesc.text = description
        binding.tvDate.text = createdAt
    }
    companion object {
        const val EXTRA_PHOTO_URL = "EXTRA_PHOTO_URL"
        const val EXTRA_CREATED_AT = "EXTRA_CREATED_AT"
        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        const val EXTRA_LON = "EXTRA_LON"
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_LAT = "EXTRA_LAT"
    }
}