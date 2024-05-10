package com.example.mystoryapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.view.ViewModelFactory
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.data.adapter.StoryAdapter
import com.example.mystoryapp.data.preferences.UserPreference
import com.example.mystoryapp.data.retrofit.api.ApiConfig
import com.example.mystoryapp.data.retrofit.response.ListStoryItem
import com.example.mystoryapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
//    private lateinit var storiesAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        observeSession()
//        CoroutineScope(Dispatchers.Main).launch {
//            try {
//                getAllStories()
//            } catch (e: Exception) {
//                Log.e("MainActivity", "Error: ${e.message}")
//            }
//        }
//        observeListStory()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observeSession() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

//    suspend fun getAllStories() {
//        viewModel.getAllStory()
//    }
//
//    private fun observeListStory() {
//        viewModel.listStory.observe(this) { listStory ->
//            if (listStory.isNotEmpty()) {
//                // Inisialisasi adapter dan atur RecyclerView
//                storiesAdapter = StoryAdapter(listStory)
//                storiesAdapter.setOnItemCallback(object : StoryAdapter.OnItemClickCallback {
//                    override fun onItemClicked(data: ListStoryItem) {
//                        // Implementasi aksi saat item diklik
//                    }
//                })
//                binding.rvStories.adapter = storiesAdapter
//                binding.rvStories.layoutManager = LinearLayoutManager(this)
//            } else {
//                // Tangani jika daftar cerita kosong
//                Toast.makeText(this, "Daftar cerita kosong", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

}