package com.example.edulang.util

import android.widget.TextView
import android.text.TextPaint
import android.graphics.Typeface
import android.util.TypedValue
import androidx.core.content.ContextCompat

fun TextView.applyDynamicTitleStyle(
    textToFit: String,
    maxTextSizeSp: Float,
    minTextSizeSp: Float,
    textColorResId: Int,
    shadowRadius: Float,
    shadowColor: Int,
    fontAssetPath: String
) {
    // Aplica a fonte
    val typeface = Typeface.createFromAsset(context.assets, fontAssetPath)
    this.typeface = typeface

    // Aplica a cor do texto
    this.setTextColor(ContextCompat.getColor(context, textColorResId))

    // Aplica a sombra
    this.setShadowLayer(shadowRadius, 0f, 0f, shadowColor)

    // Ajuste de tamanho dinâmico
    this.post {
        val viewWidth = this.width.toFloat()
        if (viewWidth <= 0) {
            this.textSize = maxTextSizeSp
            return@post
        }

        val textPaint = TextPaint(paint)
        var currentTextSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, maxTextSizeSp, resources.displayMetrics)
        textPaint.textSize = currentTextSizePx

        val minTextSizePx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, minTextSizeSp, resources.displayMetrics)

        while (textPaint.measureText(textToFit) > viewWidth && currentTextSizePx > minTextSizePx) {
            currentTextSizePx -= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1f, resources.displayMetrics)
            textPaint.textSize = currentTextSizePx
        }
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTextSizePx)
    }
}