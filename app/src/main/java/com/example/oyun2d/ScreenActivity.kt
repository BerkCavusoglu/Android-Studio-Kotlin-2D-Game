package com.example.oyun2d

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import android.os.Handler
import java.util.*

class ScreenActivity : AppCompatActivity() {

    private lateinit var cl: ConstraintLayout
    private lateinit var scorTextView: TextView
    private lateinit var buttonStart: TextView
    private lateinit var yellow: ImageView
    private lateinit var ch: ImageView
    private lateinit var black: ImageView
    private lateinit var pink: ImageView

    // Positions
    private var chX = 0f
    private var chY = 0f
    private var yellowX = 0f
    private var yellowY = 0f
    private var blackX = 0f
    private var blackY = 0f
    private var pinkX = 0f
    private var pinkY = 0f

    private var screenwidth = 0
    private var screenheight = 0
    private var chwidth = 0
    private var chheight = 0

    private var chspeed = 0
    private var yellowspeed = 0
    private var blackspeed = 0
    private var pinkspeed = 0

    private var checkControl = false
    private var startingControl = false

    private var score = 0 // 'scor' yerine 'score' ve int tanımı

    private val timer = Timer()
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen)

        cl = findViewById(R.id.cl)
        scorTextView = findViewById(R.id.scor)
        buttonStart = findViewById(R.id.buttonStart)
        yellow = findViewById(R.id.yellow)
        ch = findViewById(R.id.ch)
        black = findViewById(R.id.black)
        pink = findViewById(R.id.pink)

        black.x = -80f
        black.y = -80f
        yellow.x = -80f
        yellow.y = -80f
        pink.x = -80f
        pink.y = -80f

        cl.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (startingControl) {
                        Log.d("MotionEvent", "The screen was touched")
                        checkControl = true
                    } else {
                        startingControl = true
                        chX = ch.x
                        chY = ch.y
                        timer.schedule(object : TimerTask() {
                            override fun run() {
                                handler.post {
                                    moveObjects()
                                    collisionControl()
                                    if (checkControl) {
                                        chY -= 20
                                    } else {
                                        chY += 20
                                    }
                                    ch.y = chY

                                    // Limit character movement within screen bounds
                                    if (chY <= 0) {
                                        chY = 0f
                                    }
                                    if (chY >= screenheight - chheight) {
                                        chY = (screenheight - chheight).toFloat()
                                    }
                                }
                            }
                        }, 0, 20)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("MotionEvent", "Screen left")
                    checkControl = false
                }
            }
            true
        }

        buttonStart.visibility = View.INVISIBLE
        chwidth = ch.width
        chheight = ch.height
        screenwidth = cl.width
        screenheight = cl.height
    }

    private fun moveObjects() {
        yellowspeed = Math.round(screenwidth / 60f)
        blackspeed = Math.round(screenwidth / 60f)
        pinkspeed = Math.round(screenwidth / 30f)

        blackX -= blackspeed
        if (blackX < 0) {
            blackX = screenwidth + 20f
            blackY = Math.floor(Math.random() * screenheight).toFloat()
        }
        black.x = blackX
        black.y = blackY

        yellowX -= yellowspeed
        if (yellowX < 0) {
            yellowX = screenwidth + 20f
            yellowY = Math.floor(Math.random() * screenheight).toFloat()
        }
        yellow.x = yellowX
        yellow.y = yellowY

        pinkX -= pinkspeed
        if (pinkX < 0) {
            pinkX = screenwidth + 20f
            pinkY = Math.floor(Math.random() * screenheight).toFloat()
        }
        pink.x = pinkX
        pink.y = pinkY
    }

    private fun collisionControl() {
        val yellowcenterX = yellowX + yellow.width / 2
        val yellowcenterY = yellowY + yellow.height / 2

        if (0 <= yellowcenterX && yellowcenterX <= chwidth && chY <= yellowcenterY && yellowcenterY <= chY + chheight) {
            score += 20
            yellowX = -10f
        }

        val pinkcenterX = pinkX + pink.width / 2
        val pinkcenterY = pinkY + pink.height / 2

        if (0 <= pinkcenterX && pinkcenterX <= chwidth && chY <= pinkcenterY && pinkcenterY <= chY + chheight) {
            score += 50
            pinkX = -10f
        }

        val blackcenterX = blackX + black.width / 2
        val blackcenterY = blackY + black.height / 2

        if (0 <= blackcenterX && blackcenterX <= chwidth && chY <= blackcenterY && blackcenterY <= chY + chheight) {
            blackX = -10f
            timer.cancel()

            val intent = Intent(this@ScreenActivity, ResultScreenActivity::class.java)
            intent.putExtra("score", score)
            startActivity(intent)
        }

        scorTextView.text = score.toString()
    }
}
