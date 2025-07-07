package com.example.edulang

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edulang.adapter.LessonAdapter
import com.example.edulang.data.model.Lesson
import com.example.edulang.databinding.ActivityMainBinding
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var lessonAdapter: LessonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atualizar barra de progresso de lições
        val lessons = loadLessons(this)
        val lessonsCompleted = 0 //TODO
        val progress = ((lessonsCompleted + 1) * 100) / lessons.size
        binding.progressBar.progress = progress

        // Atualizar texto da barra de progresso de lições
        val lessonsText = "Lições: $lessonsCompleted/${lessons.size}"
        val textView = binding.lessonText
        textView.text = lessonsText

        // Estilizar barra de progresso de lições
        val typeface = Typeface.createFromAsset(assets, "fonts/KGHAPPYSolid.ttf")
        textView.typeface = typeface
        textView.textSize = 32f
        textView.setTextColor(Color.YELLOW)
        textView.setShadowLayer(20f, 0f, 0f, Color.BLACK)

        // Recycle das lições
        val recyclerView = binding.recyclerView

        lessonAdapter = LessonAdapter(lessons, assets) { lesson ->
            val intent = Intent(this, LessonActivity::class.java)
            intent.putExtra("lessonId", lesson.id)
            startActivity(intent)
        }

        recyclerView.adapter = lessonAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadLessons(context: Context): List<Lesson> {
        val json = context.assets.open("lessons.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Lesson>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun salvarResposta(context: Context, questionId: Int, resposta: String) {
        val prefs = context.getSharedPreferences("respostas", MODE_PRIVATE)
        prefs.edit().putString("resposta_$questionId", resposta).apply()
    }

    fun obterResposta(context: Context, questionId: Int): String? {
        val prefs = context.getSharedPreferences("respostas", MODE_PRIVATE)
        return prefs.getString("resposta_$questionId", null)
    }
}

/*

        val questions1 = lessons[0].questions
        val questions2 = lessons[1].questions

        Log.i("MAIN", "progress: $progress")
        Log.i("MAIN", "lessons[0].title: ${lessons[0].title}")
        Log.i("MAIN", "questions1: $questions1")
        Log.i("MAIN", "questions2: $questions2")
        */