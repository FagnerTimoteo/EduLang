package com.example.edulang.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

object Progress {
    fun saveProgress(context: Context, questionId: Int) {
        val prefs = context.getSharedPreferences("respostas", MODE_PRIVATE)
        prefs.edit().putBoolean("resposta_$questionId", true).apply()
    }

    fun recoverProgress(context: Context, questionId: Int): Boolean {
        val prefs = context.getSharedPreferences("respostas", MODE_PRIVATE)
        return prefs.getBoolean("resposta_$questionId", false)
    }
}