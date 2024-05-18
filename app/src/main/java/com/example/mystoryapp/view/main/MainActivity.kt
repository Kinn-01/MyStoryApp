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
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.data.adapter.StoryAdapter
import com.example.mystoryapp.data.retrofit.response.ListStoryItem
import com.example.mystoryapp.view.addStory.StoryAddActivity
import com.example.mystoryapp.view.detailStory.DetailStoryActivity
import com.example.mystoryapp.view.login.LoginActivity
import com.example.mystoryapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.provider.Settings
import com.example.mystoryapp.view.maps.MapsActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var storiesAdapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        observeSession()
        observeLogout()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                getAllStories()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e.message}")
            }
        }
        observeListStory()

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this@MainActivity, StoryAddActivity::class.java))
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun observeLogout() {
        binding.barApp.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemMenu -> {
                    // Menghentikan event onClickListener sementara
                    binding.barApp.setOnMenuItemClickListener(null)

                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.exitConfirm))
                        setMessage(getString(R.string.exit))
                        setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                            dialog.dismiss()

                            // Lakukan logout setelah dialog dikonfirmasi
                            viewModel.logout()

                            // Mengembalikan event onClickListener setelah dialog ditutup
                            binding.barApp.setOnMenuItemClickListener {
                                observeLogout()
                                true
                            }

                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        setNegativeButton(getString(R.string.no)) { dialog, _ ->
                            dialog.dismiss()
                            // Mengembalikan event onClickListener setelah dialog ditutup
                            binding.barApp.setOnMenuItemClickListener {
                                observeLogout()
                                true
                            }
                        }
                        create()
                        show()
                    }
                    true
                }
                R.id.menuBahasa -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.menuMaps -> {
                    startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                    true
                }
                else -> false
            }
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
    private suspend fun getAllStories() {
        viewModel.getAllStory()
    }
    private fun observeListStory() {
        viewModel.listStory.observe(this) { listStory ->
            if (listStory.isNotEmpty()) {
                // Inisialisasi adapter dan atur RecyclerView
                this.storiesAdapter = StoryAdapter(listStory, object : StoryAdapter.OnAdapterListener {
                    override fun onClick(story: ListStoryItem) {
                        // Implementasi aksi saat item diklik
                        navigateToDetailStory(story)
                    }
                })
                binding.rvStories.adapter = storiesAdapter
                binding.rvStories.layoutManager = LinearLayoutManager(this)
            } else {
                // Tangani jika daftar cerita kosong
                Toast.makeText(this, getString(R.string.emptyStory), Toast.LENGTH_SHORT).show()

                // Atur adapter dan RecyclerView menjadi null atau kosong
                this.storiesAdapter = StoryAdapter(mutableListOf(), object : StoryAdapter.OnAdapterListener {
                    override fun onClick(story: ListStoryItem) {
                        // Implementasi aksi saat item diklik
                    }
                })
                binding.rvStories.adapter = storiesAdapter
                binding.rvStories.layoutManager = LinearLayoutManager(this)
            }
        }
    }
    private fun navigateToDetailStory(story: ListStoryItem) {
        val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.EXTRA_PHOTO_URL, story.photoUrl)
        intent.putExtra(DetailStoryActivity.EXTRA_CREATED_AT, story.createdAt)
        intent.putExtra(DetailStoryActivity.EXTRA_NAME, story.name)
        intent.putExtra(DetailStoryActivity.EXTRA_DESCRIPTION, story.description)
        intent.putExtra(DetailStoryActivity.EXTRA_LON, story.lon)
        intent.putExtra(DetailStoryActivity.EXTRA_ID, story.id)
        intent.putExtra(DetailStoryActivity.EXTRA_LAT, story.lat)
        startActivity(intent)
    }
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