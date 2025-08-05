package com.example.edulang

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edulang.adapter.QuestionAdapter
import com.example.edulang.data.model.Lesson
import com.example.edulang.databinding.ActivityLessonBinding
import com.example.edulang.util.Progress.recoverLessonProgress
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.example.edulang.util.applyDynamicTitleStyle

class LessonActivity : ComponentActivity() {
    private lateinit var binding: ActivityLessonBinding
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var lessons: List<Lesson>
    private var lessonId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lessonId = intent.getIntExtra("lessonId", -1)
        lessons = loadLessons(this)

        updateProgressBar()
        setupLessonUI()
        setupQuestionsList()

        binding.progressBar.setOnClickListener {
            val intent = Intent(this, CourseActivity::class.java)
            intent.putExtra("lessonId", lessonId)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this) {
            setResult(RESULT_OK, Intent().putExtra("updatedLessonId", lessonId))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        updateProgressBar()
        questionAdapter.notifyDataSetChanged()
    }

    private fun updateProgressBar() {
        lessonId = intent.getIntExtra("lessonId", -1)
        lessons = loadLessons(this)

        val lesson = lessons.find { it.id == lessonId }
        val numQuestions: Int = lesson?.questions?.size ?: 0
        val isCompleted = recoverLessonProgress(this, lessonId, numQuestions)

        binding.progressBar.progress = if (isCompleted) 100 else 0
    }

    private fun setupLessonUI() {
        val lesson = lessons.find { it.id == lessonId }
        val titleContent = lesson?.title ?: "Error"

        val lessonText = binding.lessonText
        lessonText.text = titleContent
        lessonText.applyDynamicTitleStyle(
            textToFit = titleContent,
            maxTextSizeSp = 32f,
            minTextSizeSp = 18f,
            textColorResId = R.color.yellow,
            shadowRadius = 20f,
            shadowColor = Color.BLACK,
            fontAssetPath = "fonts/KGHAPPYSolid.ttf"
        )
    }

    private fun setupQuestionsList() {
        val lesson = lessons.find { it.id == lessonId }
        val lessonQuestions =  lesson?.questions ?: emptyList()
        val recyclerView = binding.recyclerView

        questionAdapter = QuestionAdapter(
            this,
            lessonQuestions,
            lessonId
        ) { question ->
            val intent = Intent(this, QuestionActivity::class.java)
            intent.putExtra("questionId", question.id)
            intent.putExtra("lessonId", lessonId)
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