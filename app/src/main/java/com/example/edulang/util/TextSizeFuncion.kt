package com.example.edulang.util

import android.widget.TextView
import android.text.TextPaint

fun TextView.setDynamicTextSize(textToFit: String, maxTextSize: Float, minTextSize: Float) {
    val textPaint = TextPaint(paint)
    var textSize = maxTextSize
    textPaint.textSize = textSize

    // Calcula a largura da view
    val viewWidth = this.width.toFloat()

    // Loop para reduzir o tamanho da fonte até o texto caber
    while (textPaint.measureText(textToFit) > viewWidth && textSize > minTextSize) {
        textSize -= 1f
        textPaint.textSize = textSize
    }

    // Aplica o novo tamanho
    this.textSize = textSize
}