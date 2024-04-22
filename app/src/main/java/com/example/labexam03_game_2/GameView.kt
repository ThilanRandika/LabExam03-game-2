package com.example.labexam03_game_2

import android.graphics.Color
import android.graphics.Paint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceView
import java.util.Random

class GameView (private val activity: GameActivity, private val screenX: Int, private val screenY: Int): SurfaceView(activity), Runnable {

    private var thread: Thread? = null
    private var isPlaying = false
    private var isGameOver = false
    private var background1: Background
    private var background2: Background
    private var paint: Paint = Paint()
    private var player: Player
    private var startY: Float = 0f  // Stores the y-coordinate of the touch when ACTION_DOWN event occurs
    private var swiped = false  // Keeps track of whether the user swiped up or not

    companion object {
        var screenRatioX: Float = 0f
        var screenRatioY: Float = 0f
    }



    init {

        screenRatioX = 1920f / screenX
        screenRatioY = 1080f / screenY

        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)

        player = Player(this, screenY, resources, screenRatioX, screenRatioY)   // Initialize a player

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


            canvas.drawBitmap(player.getplayer(), player.x.toFloat(), player.y.toFloat(), paint)
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

        if (player.isGoingUp) {
            player.y -= (30 * screenRatioY).toInt() // Player go up
        } else {
            player.y += (30 * screenRatioY).toInt() // Player go down
        }

        // not allow player to pass the screen borders
        if (player.y < 0) {
            player.y = 0
        }

        if (player.y >= screenY - player.height) {
            player.y = screenY - player.height
        }


    }


    private fun sleep() {
        try {
            Thread.sleep(17)    // game animations works in 60fps
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = event.y    // Store the initial y-coordinate of the touch
                swiped = false  // Reset the swiped flag
            }
            MotionEvent.ACTION_UP -> {
                if (!swiped && event.x < screenX / 2) {
                    // User tapped on the left side
                    player.isGoingUp = true
                }
                player.isGoingUp = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.y - startY < -50 && event.x < screenX / 2) {
                    // User swiped up on the left side
                    player.isGoingUp = true
                    swiped = true
                }
            }
        }
        return true
    }

}