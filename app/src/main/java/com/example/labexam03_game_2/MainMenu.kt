package com.example.labexam03_game_2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainMenu : AppCompatActivity() {

    private var isSoundOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_menu)
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

        val prefs = getSharedPreferences("game", MODE_PRIVATE)
        isSoundOn = prefs.getBoolean("isSoundOn", true)

        val soundCtrl = findViewById<ImageView>(R.id.soundBtn_mainMenu)
        soundCtrl.setImageResource(if (isSoundOn) R.drawable.sound_on else R.drawable.mute)

        soundCtrl.setOnClickListener {
            isSoundOn = !isSoundOn
            soundCtrl.setImageResource(if (isSoundOn) R.drawable.sound_on else R.drawable.mute)

            val editor = prefs.edit()
            editor.putBoolean("isSoundOn", isSoundOn)
            editor.apply()
        }


    }

    //start the game when the play button clicks on the main menu
    fun startTheGame(v: View) {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    //start the leaderboard activity when the leaderboard button is clicked
    fun openLeaderboard(v: View) {
        val intent = Intent(this, Leaderboard::class.java)
        startActivity(intent)
    }

    // Function to close the game
    fun quitGame(v: View) {
        finishAffinity()
    }





}