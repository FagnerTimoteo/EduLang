package com.example.edulang

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edulang.adapter.LessonAdapter
import com.example.edulang.data.model.Lesson
import com.example.edulang.databinding.ActivityMainBinding
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.example.edulang.util.Progress.recoverProgress
import com.example.edulang.util.applyDynamicTitleStyle // Importe a nova função

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var lessonAdapter: LessonAdapter
    private lateinit var allLessons: List<Lesson>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allLessons = loadLessons(this)

        val recyclerView = binding.recyclerView
        lessonAdapter = LessonAdapter(allLessons) { lesson ->
            val intent = Intent(this, LessonActivity::class.java)
            intent.putExtra("lessonId", lesson.id)
            startActivity(intent)
        }
        recyclerView.adapter = lessonAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        updateProgressBar()
    }

    private fun updateProgressBar() {
        val lessonsCompleted = allLessons.count { lesson ->
            lesson.questions.all { question -> recoverProgress(this, lesson.id, question.id) }
        }

        val progress = (lessonsCompleted * 100) / allLessons.size
        binding.progressBar.progress = progress

        val lessonsText = "Lições: $lessonsCompleted/${allLessons.size}"
        val textView = binding.lessonText
        textView.text = lessonsText

        textView.applyDynamicTitleStyle(
            textToFit = lessonsText,
            maxTextSizeSp = 32f,
            minTextSizeSp = 18f,
            textColorResId = R.color.yellow,
            shadowRadius = 20f,
            shadowColor = Color.BLACK,
            fontAssetPath = "fonts/KGHAPPYSolid.ttf"
        )
    }

    private fun loadLessons(context: Context): List<Lesson> {
        val json = context.assets.open("lessons.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Lesson>>() {}.type
        return Gson().fromJson(json, type)
    }
}