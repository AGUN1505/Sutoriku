package com.dicoding.sutoriku.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.lifecycle.ViewModelProvider
import com.dicoding.sutoriku.MainActivity
import com.dicoding.sutoriku.R
import com.dicoding.sutoriku.data.pref.UserPreference
import com.dicoding.sutoriku.data.pref.dataStore
import com.dicoding.sutoriku.ui.welcome.WelcomeActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var splashViewModel: SplashViewModel
    @SuppressLint("UnsafeIntentLaunch")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        val userPreference = UserPreference.getInstance(application.dataStore)
        splashViewModel = ViewModelProvider(this, SplashViewModelFactory(userPreference))[SplashViewModel::class.java]

        Handler(Looper.getMainLooper()).postDelayed({
            splashViewModel.userToken.observe(this) { userToken ->
                if (userToken) {
                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 2000)
    }
}