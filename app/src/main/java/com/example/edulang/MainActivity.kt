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
import com.example.edulang.util.Progress
import com.example.edulang.util.Progress.clearProgress
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.example.edulang.util.Progress.recoverProgress
import com.example.edulang.util.setDynamicTextSize

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
        lessonAdapter = LessonAdapter(allLessons, assets) { lesson ->
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

    override fun onDestroy() {
        super.onDestroy()
        clearProgress(this) // Se você quer zerar ao fechar, descomente esta linha
    }

    private fun updateProgressBar() {
        val lessonsCompleted = allLessons.count { lesson ->
            // AGORA USE lesson.id JUNTO COM it.id (question.id)
            lesson.questions.all { question -> recoverProgress(this, lesson.id, question.id) } // <--- MUDANÇA AQUI
        }

        val progress = (lessonsCompleted * 100) / allLessons.size
        binding.progressBar.progress = progress

        val lessonsText = "Lições: $lessonsCompleted/${allLessons.size}"
        val textView = binding.lessonText
        textView.text = lessonsText

        val typeface = Typeface.createFromAsset(assets, "fonts/KGHAPPYSolid.ttf")
        textView.typeface = typeface
        textView.setTextColor(Color.YELLOW)
        textView.setShadowLayer(20f, 0f, 0f, Color.BLACK)

        textView.setDynamicTextSize(lessonsText, 32f, 18f)
    }

    private fun loadLessons(context: Context): List<Lesson> {
        val json = context.assets.open("lessons.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Lesson>>() {}.type
        return Gson().fromJson(json, type)
    }
}