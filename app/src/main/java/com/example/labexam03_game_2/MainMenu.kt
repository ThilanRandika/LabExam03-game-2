package com.example.labexam03_game_2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainMenu : AppCompatActivity() {

    private var highScores: IntArray? = null // Declare highScores as a member variable

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

        // Retrieve high scores from the intent extras
        highScores = intent.getIntArrayExtra("HIGH_SCORES")

        // Update the UI to display the high scores
        if (highScores != null) {
            val highScoresTextView = findViewById<TextView>(R.id.highScoresTextView)
            highScoresTextView.text = "High Scores:\n"
            for ((index, score) in highScores!!.withIndex()) {
                highScoresTextView.append("${index + 1}. $score\n")
            }
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



}