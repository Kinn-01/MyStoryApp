package com.example.mystoryapp.view.detailStory

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.data.retrofit.response.ListStoryItem
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

        val storyItem = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY_ITEM)

        storyItem?.let { item ->
            DetailView(item)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun DetailView(storyItem: ListStoryItem) {
        Glide.with(this@DetailStoryActivity)
            .load(storyItem.photoUrl)
            .into(binding.ivStory)

        binding.tvNama.text = storyItem.name
        binding.tvDesc.text = storyItem.description
        binding.tvDate.text = storyItem.createdAt
    }
    companion object {
        const val EXTRA_STORY_ITEM = "EXTRA_STORY_ITEM"
    }
}