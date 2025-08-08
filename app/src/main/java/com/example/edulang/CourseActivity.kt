package com.example.edulang

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.edulang.data.model.Lesson
import com.example.edulang.databinding.ActivityCourseBinding
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson


class CourseActivity : ComponentActivity() {
    private lateinit var binding: ActivityCourseBinding
    private lateinit var lessons: List<Lesson>
    private var lessonId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lessonId = intent.getIntExtra("lessonId", -1)
        lessons = loadLessons(this)

        val lesson = lessons.find { it.id == lessonId }

        binding.textLessonTitle.text = lesson?.title
        binding.textLessonContent.text = lesson?.content

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadLessons(context: Context): List<Lesson> {
        val json = context.assets.open("lessons.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Lesson>>() {}.type
        return Gson().fromJson(json, type)
    }
}