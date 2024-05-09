package com.example.labexam03_game_2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Leaderboard : AppCompatActivity() {

    private var highScores: IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_leaderboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        // Retrieve high scores from SharedPreferences
        val prefs = getSharedPreferences("high_scores", Context.MODE_PRIVATE)
        val highScoresSet = prefs.getStringSet("high_scores", HashSet()) ?: HashSet()

        // Convert the set to a sorted list of integers
        val highScoresList = highScoresSet.map { it.toInt() }.toMutableList()
        highScoresList.sortDescending()

        // Convert the list to an array of integers
        highScores = highScoresList.toIntArray()

        // Update the UI to display the high scores
        if (highScores != null) {
            val highScoresTextView = findViewById<TextView>(R.id.leader_board_highscores)
            highScoresTextView.text = "High Scores:\n"
            for ((index, score) in highScores!!.withIndex()) {
                highScoresTextView.append("${index + 1}. $score\n")
            }
        }

    }

    fun openMainMenu(v: View) {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }

}