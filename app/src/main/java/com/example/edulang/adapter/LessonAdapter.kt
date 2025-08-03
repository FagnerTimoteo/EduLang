package com.example.edulang.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edulang.R
import com.example.edulang.data.model.Lesson
import com.example.edulang.util.applyDynamicTitleStyle

class LessonAdapter(
    private val context: Context,
    private val lessons: List<Lesson>,
    private val setOnClickListener: (Lesson) -> Unit) :
    RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]

        val barText = holder.title
        barText.text = lesson.title

        updateProgressBar(lesson, holder.progressBar)

        barText.applyDynamicTitleStyle(
            textToFit = lesson.title,
            maxTextSizeSp = 16f,
            minTextSizeSp = 12f,
            textColorResId = R.color.yellow,
            shadowRadius = 20f,
            shadowColor = Color.BLACK,
            fontAssetPath = "fonts/KGHAPPYSolid.ttf"
        )

        holder.itemView.setOnClickListener {
            setOnClickListener(lesson)
        }
    }
    override fun getItemCount(): Int = lessons.size

    private fun updateProgressBar(lesson: Lesson, progressBar: ProgressBar) {
        val prefs = context.getSharedPreferences("respostas", Context.MODE_PRIVATE)

        val totalQuestions = lesson.questions.size
        var correctAnswers = 0

        for (question in lesson.questions) {
            val key = "lesson_${lesson.id}_question_${question.id}"
            val isCorrect = prefs.getBoolean(key, false)
            if (isCorrect) {
                correctAnswers++
            }
        }

        val progressPercent = if (totalQuestions > 0) (correctAnswers * 100) / totalQuestions else 0
        progressBar.progress = progressPercent
    }

}