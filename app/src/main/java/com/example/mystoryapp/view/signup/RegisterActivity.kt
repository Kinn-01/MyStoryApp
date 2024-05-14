package com.example.mystoryapp.view.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.mystoryapp.R
import com.example.mystoryapp.data.viewmodel.RegisterViewModel
import com.example.mystoryapp.databinding.ActivitySignupBinding
import com.example.mystoryapp.view.ViewModelFactory
import com.example.mystoryapp.view.login.LoginActivity
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        setupView()
        setupAction()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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

   private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                name.isEmpty()-> {
                    binding.nameEditText.error = getString(R.string.nameValidation)
                }
                email.isEmpty()-> {
                    binding.emailEditText.error  = getString(R.string.emailValidation)
                }
                password.isEmpty()-> {
                    binding.passwordEditText.error = getString(R.string.passwordValidation)
                } else -> {
                    isShowLoading(true)
                    viewModel.register(name, email, password)
                    viewModel.isLoading.observe(this, Observer { isLoading ->
                        if (isLoading) {
                            AlertDialog.Builder(this).apply {
                                setTitle("Yeah!")
                                val message = getString(R.string.account_created_message, email)
                                setMessage(message)
                                setPositiveButton(getString(R.string.next)) { _, _ ->
                                    val intent = Intent(context, LoginActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        } else {
                            Snackbar.make(binding.root, getString(R.string.registerFailed), Snackbar.LENGTH_SHORT).show()
                        }
                        isShowLoading(false)
                    })
                    setupView()
                }
            }
        }
    }
    private fun isShowLoading(isLoading: Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}