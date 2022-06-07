package com.example.sixthweekapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sixthweekapp.domain.ThreadWorker

class MainActivity : AppCompatActivity(), Callbacks {

    private val handler = Handler(Looper.getMainLooper())
    private val worker: Background = ThreadWorker(this)

    private val piText by lazy { findViewById<TextView>(R.id.pi_text) }
    private val timeText by lazy { findViewById<TextView>(R.id.timer) }
    private val timeBackground by lazy { findViewById<View>(R.id.play_pause) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        worker.create()
        val play = findViewById<Button>(R.id.play)
        val pause = findViewById<Button>(R.id.pause)
        val reset = findViewById<Button>(R.id.reset)
        play.setOnClickListener { worker.play() }
        pause.setOnClickListener { worker.pause() }
        reset.setOnClickListener { worker.reset() }
    }

    override fun onResume() {
        super.onResume()
        worker.play()
    }

    override fun onPause() {
        super.onPause()
        worker.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        worker.destroy()
    }

    override fun onPiChanged(pi: CharSequence) {
        handler.post {
            piText.text = pi
        }
    }

    override fun onTimeChanged(timeMs: Long) {
        handler.post {
            val millis = timeMs % 1000
            val minutes = timeMs / 1000 / 60
            val seconds = timeMs / 1000 % 60
            timeText.text =
                "${"%02d".format(minutes)}:${"%02d".format(seconds)}:${"%03d".format(millis)}"
        }
    }

    override fun onColorChanged(color: Int) {
        handler.post {
            timeBackground.setBackgroundColor(color)
        }
    }
}