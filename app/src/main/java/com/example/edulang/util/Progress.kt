package com.example.edulang.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

object Progress {
    // A chave agora inclui o lessonId para ser única, corrigindo a contagem na barra de progresso
    private fun getProgressKey(lessonId: Int, questionId: Int): String {
        return "lesson_${lessonId}_question_${questionId}"
    }

    fun saveProgress(context: Context, lessonId: Int, questionId: Int) {
        val prefs = context.getSharedPreferences("respostas", MODE_PRIVATE)
        prefs.edit().putBoolean(getProgressKey(lessonId, questionId), true).apply()
    }

    fun recoverProgress(context: Context, lessonId: Int, questionId: Int): Boolean {
        val prefs = context.getSharedPreferences("respostas", MODE_PRIVATE)
        return prefs.getBoolean(getProgressKey(lessonId, questionId), false)
    }

    fun clearProgress(context: Context) {
        val prefs = context.getSharedPreferences("respostas", MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}