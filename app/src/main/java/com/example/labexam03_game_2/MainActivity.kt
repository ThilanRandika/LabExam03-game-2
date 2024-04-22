package com.example.labexam03_game_2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    val SPLASH_DELAY: Long = 2000 // 2 seconds
    var shouldNavigate = false // identify whether  skip button was clicked

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //set to fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Delay navigation to MainMenu after SPLASH_DELAY milliseconds
        Handler(Looper.getMainLooper()).postDelayed({
            if (!shouldNavigate) {
                navigateToMainMenu()
            }
        }, SPLASH_DELAY)

        // Navigate to main menu using skip button
        val skipBtn_welcomeScreen: Button = findViewById(R.id.skipBtn_welcomeScreen)
        skipBtn_welcomeScreen.setOnClickListener {
            shouldNavigate = true
            navigateToMainMenu()
        }

    }

    //function for navigate to the main menu
    fun navigateToMainMenu() {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
        finish() // Finish current activity so user cannot navigate back to splash screen
    }

}