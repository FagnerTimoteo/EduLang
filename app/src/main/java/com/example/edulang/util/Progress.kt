package com.example.edulang.util

import android.content.Context
import android.content.Context.MODE_PRIVATE

object Progress {
    // A chave agora inclui o lessonId e questionId para ser única
    private fun getProgressKey(lessonId: Int, questionId: Int): String {
        return "lesson_${lessonId}_question_${questionId}"
    }

    // Salva o progresso de uma questão específica dentro de uma lição
    fun saveProgress(context: Context, lessonId: Int, questionId: Int) {
        val prefs = context.getSharedPreferences("respostas", MODE_PRIVATE)
        prefs.edit().putBoolean(getProgressKey(lessonId, questionId), true).apply()
    }

    // Recupera o progresso de uma questão específica dentro de uma lição
    fun recoverProgress(context: Context, lessonId: Int, questionId: Int): Boolean {
        val prefs = context.getSharedPreferences("respostas", MODE_PRIVATE)
        return prefs.getBoolean(getProgressKey(lessonId, questionId), false)
    }

    // Recupera o progresso de uma liçao e depois salva em sharedPreferences
    fun recoverLessonProgress(context: Context, lessonId: Int, totalQuestions: Int): Boolean {
        val prefs = context.getSharedPreferences("respostas", MODE_PRIVATE)

        for (questionId in 1..totalQuestions) {
            val key = "lesson_${lessonId}_question_${questionId}"
            val answered = prefs.getBoolean(key, false)
            if (!answered) return false
        }

        return true
    }
}