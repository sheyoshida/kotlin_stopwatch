package com.example.shenayoshida.stopwatch

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var startButton: Button? = null // prevent accidentally calling null by making optional
    var timeTextView: TextView? = null
    var resetButton: Button? = null

    var isTimerRunning: Boolean = false // type inference predicts type (bool!)

    var time: Long = 0
    var lastTime: Long = 0 // store time when stopping / resetting
    var timerHandler: Handler = Handler()
    var timerAction: (() -> Unit) = {
        if (isTimerRunning) updateTimerText()
    }

    // Mark: - Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // find UI elements from Resources (R.)
        timeTextView = findViewById(R.id.tv_time)
        startButton = findViewById(R.id.btn_start)
        resetButton = findViewById(R.id.btn_reset)

        startButton?.setBackgroundColor(Color.GREEN)
        resetButton?.setBackgroundColor(Color.GRAY)

        // when timer is not running, change isTimerRunning to false
        startButton?.setOnClickListener {
            isTimerRunning = !isTimerRunning

            if (isTimerRunning) {
                lastTime = System.currentTimeMillis() // save current time in ms
                timerHandler.postDelayed(timerAction,0)

            } else {
                startButton?.setText("Start")
            }
            startButton?.setText(if (isTimerRunning) "Stop" else "Start") // do the above in one line
            startButton?.setBackgroundColor((if (isTimerRunning) Color.RED else Color.GREEN))
        }

        // reset button logic
        resetButton?.setOnClickListener {
            time = 0
            timeTextView?.setText(time.timeIntervalString())
            startButton?.setText("Start")
            startButton?.setBackgroundColor((if (isTimerRunning) Color.RED else Color.GREEN))
        }

    }

    fun updateTimerText() {
        val currentTime = System.currentTimeMillis()
        time += currentTime - lastTime
        lastTime = currentTime

        timeTextView?.setText(time.timeIntervalString())
        timerHandler.postDelayed(timerAction,30)
    }

}

// outside of the class... create extension to turn long into string
fun Long.toCentiseconds() = this / 10
fun Long.toSeconds() = this / 1000
fun Long.toMinutes() = this.toSeconds() / 60

// reset seconds every 60 seconds, reset centiseconds every 100 seconds
fun Long.timeIntervalString() = String.format("%02d:%02d:%02d", this.toMinutes(), this.toSeconds() % 60, this.toCentiseconds() % 100)