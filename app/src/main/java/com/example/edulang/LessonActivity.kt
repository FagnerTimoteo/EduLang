package com.example.edulang

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
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

        // Obtém a lição
        val lessonId = intent.getIntExtra("lessonId", -1)
        val lessons = loadLessons(this)
        val lesson = lessons.find { it.id == lessonId }
        val contentLesson = lesson?.content // O texto da lição
        val lessonQuestions =  lesson?.questions ?: emptyList()

        // Atualizar texto da barra de progresso de questões
        val lessonText = binding.lessonText
        lessonText.text = lesson?.title ?: "Error"

        // Estilizar barra de progresso de questões
        val typeface = Typeface.createFromAsset(assets, "fonts/KGHAPPYSolid.ttf")
        lessonText.typeface = typeface
        lessonText.textSize = 32f
        lessonText.setTextColor(Color.YELLOW)
        lessonText.setShadowLayer(20f, 0f, 0f, Color.BLACK)

        // Recycle das questões
        val recyclerView = binding.recyclerView

        questionAdapter = QuestionAdapter(lessonQuestions, assets) { question ->
            val intent = Intent(this, QuestionActivity::class.java)
            intent.putExtra("questionId", question.id)
            intent.putExtra("lessonId", lessonId) // Adicione isso também
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