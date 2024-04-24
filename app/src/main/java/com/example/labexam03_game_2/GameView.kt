package com.example.labexam03_game_2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
    private var startX: Float = 0f  // Stores the X-coordinate of the touch when ACTION_LEFT and ACTION_RIGHT event occurs
    private var swiped = false  // Keeps track of whether the user swiped up or not
    private var aliens = arrayOfNulls<Alien>(2)
    private var random = Random()
    private var prefs: SharedPreferences = activity.getSharedPreferences("game", Context.MODE_PRIVATE)
    private var points = 0
    private var sounds: SoundPool
    private var gameOverSound: Int
    private var jetpackSound: Int
    private var jetpackSoundStreamId: Int = 0  // Initialize jetpack sound stream ID

    companion object {
        var screenRatioX: Float = 0f
        var screenRatioY: Float = 0f
    }



    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            sounds = SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            sounds = SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }
        // Set all sounds
        gameOverSound = sounds.load(activity, R.raw.game_over, 1)
        jetpackSound = sounds.load(activity, R.raw.jetpack, 1)

        screenRatioX = 1920f / screenX
        screenRatioY = 1080f / screenY

        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)

        player = Player(this, screenY, resources, screenRatioX, screenRatioY)   // Initialize a player

        background2.x = screenX

        paint.textSize = 128f
        paint.color = Color.WHITE

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

            canvas.drawText(points.toString(), (screenX / 2).toFloat(), 164f, paint)

            if (isGameOver) {

                if (prefs.getBoolean("isSoundOn", true)) {
                    sounds.play(gameOverSound, 1f, 1f, 0, 0, 1f)
                }

                isPlaying = false
                canvas.drawBitmap(player.getDead(), player.x.toFloat(), player.y.toFloat(), paint)
                holder.unlockCanvasAndPost(canvas)
                savePoints()
                gameOver()
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

        // player's movements
        if (player.isGoingUp) {
            player.y -= (20 * screenRatioY).toInt() // Player go up
        } else if (player.isGoingDown) {
            player.y += (20 * screenRatioY).toInt() // Player go down
        } else if (player.isGoingLeft) {
            player.x -= (20 * screenRatioX).toInt() // Player go left
        } else if (player.isGoingRight) {
            player.x += (10 * screenRatioX).toInt() // Player go right
        } else
            player.y += (5 * screenRatioY).toInt() // Player fall under the planet's gravity


        // not allow player to pass the screen borders
        if (player.y < 0) {
            player.y = 0
        }

        if (player.y >= screenY - player.height - 100) {
            player.y = screenY - player.height - 100
        }

        if (player.x < 0) {
            player.x = 0
        }

        if (player.x >= screenX - player.width) {
            player.x = screenX - player.width
        }


        for (alien in aliens) {
            if (alien != null) {
                alien.x -= alien.speed
                if (alien.x + alien.width < 0) {
                    points++ // Increase points when an alien leaves the screen
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



    private fun savePoints() {
        prefs = activity.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
        val highScoresSet = prefs.getStringSet("high_scores", HashSet()) ?: HashSet()

        // Convert the set to a list of integers
        val highScoresList = highScoresSet.map { it.toInt() }.toMutableList()

        // Add the current score to the list
        highScoresList.add(points)

        // Sort the list in descending order
        highScoresList.sortDescending()

        // Keep only the top 5 scores
        if (highScoresList.size > 5) {
            highScoresList.removeAt(5)
        }

        // Convert the list back to a set of strings
        val updatedHighScoresSet = highScoresList.map { it.toString() }.toSet()

        // Save the updated set of high scores
        val editor = prefs.edit()
        editor.putStringSet("high_scores", updatedHighScoresSet)
        editor.apply()
    }


    private fun gameOver() {
        try {
            Thread.sleep(3000)
            val intent = Intent(activity, MainMenu::class.java)
            intent.putExtra("POINTS", points)

            activity.startActivity(intent)
            activity.finish()
        } catch (e: InterruptedException) {
            e.printStackTrace()
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
                startX = event.x    // Store the initial x-coordinate of the touch
                swiped = false      // Reset the swiped flag

                // Start playing jetpack sound when swiping begins
                if (event.x < screenX / 2) {
                    if (prefs.getBoolean("isSoundOn", true)) {
                        jetpackSoundStreamId = sounds.play(jetpackSound, 1f, 1f, 0, -1, 1f)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                // Stop playing jetpack sound when touch is released
                if (jetpackSoundStreamId != 0) {
                    sounds.stop(jetpackSoundStreamId)
                }
                if (!swiped && event.x < screenX / 2) {
                    // User tapped on the left side
                    player.isGoingUp = true
                }
                player.isGoingUp = false
                player.isGoingDown = false // Reset isGoingDown flag
                player.isGoingLeft = false // Reset isGoingLeft flag
                player.isGoingRight = false // Reset isGoingRight flag
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.y - startY < -50 && event.x < screenX / 2) {
                    // User swiped up on the left side
                    player.isGoingUp = true
                    swiped = true
                    player.isGoingDown = false // Reset isGoingDown flag
                    player.isGoingRight = false // Reset isGoingRight flag
                    player.isGoingLeft = false  // Reset isGoingLeft flag
                } else if (event.y - startY > 50 && event.x < screenX / 2) {
                    // User swiped down on the left side
                    player.isGoingDown = true
                    player.isGoingUp = false // Reset isGoingUp flag
                    player.isGoingRight = false // Reset isGoingRight flag
                    player.isGoingLeft = false  // Reset isGoingLeft flag
                } else if (event.x - startX < -50 && event.x < screenX / 2) {
                    // User swiped left on the left side
                    player.isGoingLeft = true
                    player.isGoingUp = false // Reset isGoingUp flag
                    player.isGoingDown = false // Reset isGoingDown flag
                    player.isGoingRight = false // Reset isGoingRight flag
                } else if (event.x - startX > 50 && event.x < screenX / 2) {
                    // User swiped right on the left side
                    player.isGoingRight = true
                    player.isGoingUp = false // Reset isGoingUp flag
                    player.isGoingDown = false // Reset isGoingDown flag
                    player.isGoingLeft = false // Reset isGoingLeft flag
                }
            }
        }
        return true
    }





}