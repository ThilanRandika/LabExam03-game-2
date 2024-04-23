package com.example.labexam03_game_2

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
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
    private var aliens = arrayOfNulls<Alien>(2)
    private var random = Random()

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

        for (i in aliens.indices) {
            val alien = Alien(resources)
            aliens[i] = alien
        }

        random = Random()

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

            aliens.forEach { alien -> alien?.let { canvas.drawBitmap(it.getAlien(), it.x.toFloat(), it.y.toFloat(), paint) } }

            if (isGameOver) {
                isPlaying = false
                canvas.drawBitmap(player.getDead(), player.x.toFloat(), player.y.toFloat(), paint)
                holder.unlockCanvasAndPost(canvas)
                return
            }

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

        // player's vertical movement based on isGoingUp and isGoingDown
        if (player.isGoingUp) {
            player.y -= (20 * screenRatioY).toInt() // Player go up
        } else if (player.isGoingDown) {
            player.y += (20 * screenRatioY).toInt() // Player go down
        } else
            player.y += (5 * screenRatioY).toInt() // Player fall with planet's gravity

        // not allow player to pass the screen borders
        if (player.y < 0) {
            player.y = 0
        }

        if (player.y >= screenY - player.height - 100) {
            player.y = screenY - player.height - 100
        }


        for (alien in aliens) {
            if (alien != null) {
                alien.x -= alien.speed
                if (alien.x + alien.width < 0) {
                    val bound = (30 * screenRatioX).toInt()
                    alien.speed = random.nextInt(bound)
                    if (alien.speed < 10 * screenRatioX) {
                        alien.speed = (10 * screenRatioX).toInt()
                    }
                    alien.x = screenX   // Position Alien to the right side of the screen
                    alien.y = random.nextInt(screenY - alien.height)
                }
                if (Rect.intersects(alien.getCollisionShape(), player.getCollisionShape())) {
                    isGameOver = true
                    return
                }
            }
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
                swiped = false      // Reset the swiped flag
            }
            MotionEvent.ACTION_UP -> {
                if (!swiped && event.x < screenX / 2) {
                    // User tapped on the left side
                    player.isGoingUp = true
                }
                player.isGoingUp = false
                player.isGoingDown = false // Reset isGoingDown flag
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.y - startY < -50 && event.x < screenX / 2) {
                    // User swiped up on the left side
                    player.isGoingUp = true
                    swiped = true
                    player.isGoingDown = false // Reset isGoingDown flag
                } else if (event.y - startY > 50 && event.x < screenX / 2) {
                    // User swiped down on the left side
                    player.isGoingDown = true
                }
            }
        }
        return true
    }


}