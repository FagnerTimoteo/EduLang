package com.example.edulang

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edulang.adapter.QuestionAdapter
import com.example.edulang.data.model.Lesson
import com.example.edulang.databinding.ActivityLessonBinding
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class LessonActivity : ComponentActivity()  {
    private lateinit var binding: ActivityLessonBinding
    private lateinit var questionAdapter: QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lessonId = intent.getIntExtra("lessonId", -1)
        val lessons = loadLessons(this)
        val lesson = lessons.find { it.id == lessonId }

        val lessonQuestions =   lesson?.questions ?: emptyList()

        binding.lessonsText.text = lesson?.title ?: "Error"

        val recyclerView = binding.recyclerView

        questionAdapter = QuestionAdapter(lessonQuestions) { question ->
            val intent = Intent(this, QuestionActivity::class.java)
            intent.putExtra("question", question)
            startActivity(intent)
        }

        recyclerView.adapter = questionAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadLessons(context: Context): List<Lesson> {
        val json = context.assets.open("lessons.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Lesson>>() {}.type
        return Gson().fromJson(json, type)
    }
}