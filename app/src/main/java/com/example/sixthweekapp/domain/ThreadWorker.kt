package com.example.sixthweekapp.domain

import android.graphics.Color
import android.util.Log
import com.example.sixthweekapp.Background
import com.example.sixthweekapp.Background.Companion.COLOR_TIMEOUT
import com.example.sixthweekapp.Background.Companion.TIME_SLEEP_PAUSE
import com.example.sixthweekapp.Background.Companion.TIME_TIMEOUT
import com.example.sixthweekapp.Background.Companion.colors
import com.example.sixthweekapp.Callbacks
import com.example.sixthweekappcoroutines.domain.GenerationPI
import java.util.*

class ThreadWorker(private val uiCallback: Callbacks) : Background {
    @Volatile
    private var isPaused = false
    private val random = Random(System.currentTimeMillis())
    private val colorThread: Thread
    private val timeThread: Thread
    private val piThread: Thread

    @Volatile
    private var sleepTime = 0L

    @Volatile
    private var timeMillis = 0L

    @Volatile
    private var n = 1

    init {
        piThread = Thread {
            try {
                while (true) {
                    while (isPaused) {
                        Thread.sleep(TIME_SLEEP_PAUSE)
                    }
                    val piNumber = GenerationPI.formula(n)
                    val piString = "%.${2 * n}f".format(piNumber)
                    val pi = piString.subSequence(0, n + 2)
                    uiCallback.onPiChanged(pi)
                    n++
                }
            } catch (e: InterruptedException) {
                Log.w("KekPek", "Поток числа Пи остановлен")
            }
        }
        colorThread = Thread {
            try {
                while (true) {
                    while (sleepTime < COLOR_TIMEOUT) {
                        Thread.sleep(TIME_SLEEP_PAUSE)
                        if (!isPaused) {
                            sleepTime += TIME_SLEEP_PAUSE
                        }
                    }
                    val colorIndex = random.nextInt(colors.size)
                    val color = colors.get(colorIndex)
                    uiCallback.onColorChanged(color)
                    sleepTime = 0
                }
            } catch (e: InterruptedException) {
                Log.w("KekPek", "Поток цвета остановлен")
            }
        }
        timeThread = Thread {
            try {

                while (true) {
                    while (isPaused) {
                        Thread.sleep(TIME_SLEEP_PAUSE)
                    }
                    Thread.sleep(TIME_TIMEOUT)
                    timeMillis += TIME_TIMEOUT
                    uiCallback.onTimeChanged(timeMillis)
                }
            } catch (e: InterruptedException) {
                Log.w("KekPek", "Поток таймера остановлен")
            }
        }
    }

    override fun create() {
        colorThread.start()
        timeThread.start()
        piThread.start()
    }

    override fun destroy() {
        colorThread.interrupt()
        timeThread.interrupt()
        piThread.interrupt()
    }

    override fun pause() {
        isPaused = true
    }

    override fun play() {
        isPaused = false
    }

    override fun reset() {
        sleepTime = 0
        timeMillis = 0
        n = 1
        isPaused = false
        uiCallback.onColorChanged(Color.WHITE)

    }
}