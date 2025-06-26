package com.example.edulang

import android.util.Log;
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.edulang.data.model.Lesson
import com.example.edulang.databinding.ActivityMainBinding
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progressBar = binding.progressBar

        val lessons = loadLessons(this)

        val questions1 = lessons[0].questions
        val questions2 = lessons[1].questions

        val progress = ((0 + 1) * 100) / questions1.size
        progressBar.progress = progress

        Log.i("MAIN", "progress: $progress")
        Log.i("MAIN", "lessons[0].title: ${lessons[0].title}")
        Log.i("MAIN", "questions1: $questions1")
        Log.i("MAIN", "questions2: $questions2")
    }

    private fun loadLessons(context: Context): List<Lesson> {
        val json = context.assets.open("lessons.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Lesson>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun salvarResposta(context: Context, questionId: Int, resposta: String) {
        val prefs = context.getSharedPreferences("respostas", Context.MODE_PRIVATE)
        prefs.edit().putString("resposta_$questionId", resposta).apply()
    }

    fun obterResposta(context: Context, questionId: Int): String? {
        val prefs = context.getSharedPreferences("respostas", Context.MODE_PRIVATE)
        return prefs.getString("resposta_$questionId", null)
    }

}