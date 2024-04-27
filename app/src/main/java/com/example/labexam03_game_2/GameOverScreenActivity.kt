package com.example.labexam03_game_2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class GameOverScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_over_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve points value from intent extras
        val points = intent.getIntExtra("POINTS", 0)

        // Find the TextView for displaying points
        val pointsTextView = findViewById<TextView>(R.id.gamePoints)

        // Set the points value to the TextView
        pointsTextView.text = "Points: $points"


    }

    //start the game when the rePlay button clicks on the Game Over Menu
    fun startTheGameAgain(v: View) {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    //Go to the MainMenu
    fun openMainMenu(v: View) {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
        this.finish()
    }
}