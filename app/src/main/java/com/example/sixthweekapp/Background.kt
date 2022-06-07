package com.example.sixthweekapp

import android.graphics.Color

interface Background {
    fun create()
    fun play()
    fun pause()
    fun destroy()
    fun reset()

    companion object {
        const val COLOR_TIMEOUT = 20_000L
        const val TIME_TIMEOUT = 100L
        const val TIME_SLEEP_PAUSE = 100L
        val colors = listOf(
            Color.WHITE,
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.CYAN,
            Color.DKGRAY,
            Color.GRAY,
            Color.MAGENTA

        )
    }
}