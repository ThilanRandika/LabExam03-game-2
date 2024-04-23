package com.example.labexam03_game_2

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

class Player (private val gameView: GameView, screenY: Int, res: Resources, private val screenRatioX: Float, private val screenRatioY: Float){

    var x: Int
    var y: Int
    var width: Int
    var height: Int
    private var player1: Bitmap
    private var player2: Bitmap
    private var playerUp: Bitmap
    private var wingCounter = 0
    var isGoingUp = false
    var isGoingDown = false
    var isGoingRight = false
    var isGoingLeft = false
    private var dead: Bitmap


    init {
        player1 = BitmapFactory.decodeResource(res, R.drawable.player1)
        player2 = BitmapFactory.decodeResource(res, R.drawable.player2)
        playerUp = BitmapFactory.decodeResource(res, R.drawable.player_up)

        width = player1.width
        height = player1.height

        width /= 4
        height /= 4

        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        player1 = Bitmap.createScaledBitmap(player1, width, height, false)
        player2 = Bitmap.createScaledBitmap(player2, width, height, false)
        playerUp = Bitmap.createScaledBitmap(playerUp, width, height, false)

        dead = BitmapFactory.decodeResource(res, R.drawable.player_dead)
        dead = Bitmap.createScaledBitmap(dead, width, height, false)

        y = screenY / 2                     // Player starts vertically center of the screen
        x = (256 * screenRatioX).toInt()    // Starting X position
    }


    fun getplayer(): Bitmap {

        // Check if the player is going up
        if (isGoingUp) {
            // Return the image for when the player is going up
            return playerUp
        }

        if (wingCounter == 0) {
            wingCounter++
            return player1
        }
        wingCounter--
        return player2
    }


    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }


    fun getDead(): Bitmap {
        return dead
    }

}