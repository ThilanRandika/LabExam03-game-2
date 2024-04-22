package com.example.labexam03_game_2

import android.graphics.Color
import android.graphics.Paint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.SurfaceView
import java.util.Random

class GameView (private val activity: GameActivity, private val screenX: Int, private val screenY: Int): SurfaceView(activity), Runnable {

    private var thread: Thread? = null
    private var isPlaying = false
    private var isGameOver = false
    private var background1: Background
    private var background2: Background
    private var paint: Paint = Paint()

    companion object {
        var screenRatioX: Float = 0f
        var screenRatioY: Float = 0f
    }



    init {

        screenRatioX = 1920f / screenX
        screenRatioY = 1080f / screenY

        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)

        background2.x = screenX

    }



    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread?.start()
    }

    fun pause() {
        try {
            isPlaying = false
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


    private fun draw(){

        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawBitmap(background1.background, background1.x.toFloat(), background1.y.toFloat(), paint)
            canvas.drawBitmap(background2.background, background2.x.toFloat(), background2.y.toFloat(), paint)


            holder.unlockCanvasAndPost(canvas)
        }
    }


    private fun update(){

        background1.x -= (10 * screenRatioX).toInt()
        background2.x -= (10 * screenRatioX).toInt()

        if (background1.x + background1.background.width < 0) {
            background1.x = screenX
        }

        if (background2.x + background2.background.width < 0) {
            background2.x = screenX
        }


    }


    private fun sleep() {
        try {
            Thread.sleep(17)    // game animations works in 60fps
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

}