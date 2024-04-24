package com.example.labexam03_game_2

import android.content.Intent
import android.os.Bundle
import android.view.View
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
    }

    //start the game when the rePlay button clicks on the Game Over Menu
    fun startTheGameAgain(v: View) {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

    //Go to the MainMenu
    fun openMainMenu(v: View) {
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }
}