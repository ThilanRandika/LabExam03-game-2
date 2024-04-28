package com.example.labexam03_game_2

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

class Alien (res: Resources){

    var speed = 20
    var x = 0
    var y: Int
    var width: Int
    var height: Int
    private var alienCounter = 1
    private var alien1: Bitmap
    private var alien2: Bitmap



    init {
        alien1 = BitmapFactory.decodeResource(res, R.drawable.alien1)
        alien2 = BitmapFactory.decodeResource(res, R.drawable.alien2)

        width = alien1.width
        height = alien1.height

        width /= 6
        height /= 6

        width = (width * GameView.screenRatioX).toInt()
        height = (height * GameView.screenRatioY).toInt()

        alien1 = Bitmap.createScaledBitmap(alien1, width, height, false)
        alien2 = Bitmap.createScaledBitmap(alien2, width, height, false)

        y = -height
    }


    fun getAlien(): Bitmap {
        return when (alienCounter) {
            1 -> {
                alienCounter++
                alien1
            }
            else -> {
                alienCounter = 1
                alien2
            }
        }
    }


    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }


}