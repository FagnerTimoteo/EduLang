package com.example.edulang

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.edulang.data.model.Lesson
import com.example.edulang.databinding.ActivityQuestionBinding
import com.example.edulang.util.Progress.saveProgress
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class QuestionActivity : ComponentActivity() {
    private lateinit var binding: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtém a questão
        val lessonId = intent.getIntExtra("lessonId", -1)
        val questionId = intent.getIntExtra("questionId", -1)
        val lessons = loadLessons(this)
        val lesson = lessons.find { it.id == lessonId }
        val question = lesson?.questions?.find { it.id == questionId }

        Log.i("TESTE", "question: $question")

        // Exemplo de como salvar o progresso para questões respondidas corretamente
        // O segundo parâmetro é o id da questão respondida corretamente
        saveProgress(this, 1)
    }

    private fun loadLessons(context: Context): List<Lesson> {
        val json = context.assets.open("lessons.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Lesson>>() {}.type
        return Gson().fromJson(json, type)
    }
}