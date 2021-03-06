package com.example.sixthweekapp

import androidx.annotation.ColorInt

interface Callbacks {
    fun onPiChanged(pi: CharSequence)
    fun onTimeChanged(timeMs: Long)
    fun onColorChanged(@ColorInt color: Int)
}