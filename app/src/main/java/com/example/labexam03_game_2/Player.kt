package com.example.labexam03_game_2

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Player (private val gameView: GameView, screenY: Int, res: Resources, private val screenRatioX: Float, private val screenRatioY: Float){

    var x: Int
    var y: Int
    var width: Int
    var height: Int
    private var player1: Bitmap
    private var player2: Bitmap
    private var wingCounter = 0
    var isGoingUp = false


    init {
        player1 = BitmapFactory.decodeResource(res, R.drawable.player1)
        player2 = BitmapFactory.decodeResource(res, R.drawable.player2)

        width = player1.width
        height = player1.height

        width /= 2
        height /= 2

        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        player1 = Bitmap.createScaledBitmap(player1, width, height, false)
        player2 = Bitmap.createScaledBitmap(player2, width, height, false)

        y = screenY / 2
        x = (64 * screenRatioX).toInt()
    }


    fun getplayer(): Bitmap {

        if (wingCounter == 0) {
            wingCounter++
            return player1
        }
        wingCounter--
        return player2
    }

}