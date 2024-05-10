package com.example.mystoryapp.view.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mystoryapp.R
import com.example.mystoryapp.data.viewmodel.RegisterViewModel
import com.example.mystoryapp.databinding.ActivitySignupBinding
import com.example.mystoryapp.view.ViewModelFactory
import com.example.mystoryapp.view.login.LoginActivity
import com.example.mystoryapp.view.main.MainActivity

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
//        setViewModel()

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

    private fun setViewModel() {
        viewModel.registerResponse.observe(this) {
            if (it.error == true) {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Register success", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

   private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                name.isEmpty()-> {
                    binding.nameEditText.error = "Kolom Nama harus diisi"
                }
                email.isEmpty()-> {
                    binding.emailEditText.error  = "Email harus diisi"
                }
                password.isEmpty()-> {
                    binding.passwordEditText.error = "Password harus diisi"
                } else -> {
                    viewModel.register(name, email, password)
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan mulai berbagi cerita.")
                        setPositiveButton("Lanjut") { _, _ ->
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
            }
            viewModel.isLoading.observe(this) {
                isShowLoading(it)
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