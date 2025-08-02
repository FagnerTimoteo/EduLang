package com.example.edulang.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.edulang.R
import com.example.edulang.data.model.Question
import com.example.edulang.util.Progress.recoverProgress
import com.example.edulang.util.applyDynamicTitleStyle

class QuestionAdapter(
    private val context: Context,
    private val questions: List<Question>,
    private val lessonId: Int,
    private val setOnClickListener: (Question) -> Unit) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]

        val barText = holder.title
        val formattedQuestionTitle = "Questão ${question.id}: ${question.questionText}"
        barText.text = formattedQuestionTitle

        barText.applyDynamicTitleStyle(
            textToFit = formattedQuestionTitle,
            maxTextSizeSp = 20f,
            minTextSizeSp = 16f,
            textColorResId = R.color.yellow,
            shadowRadius = 20f,
            shadowColor = Color.BLACK,
            fontAssetPath = "fonts/KGHAPPYSolid.ttf"
        )

        val isCompleted = recoverProgress(context, lessonId, question.id)
        holder.progressBar.progress = if (isCompleted) 100 else 0

        holder.itemView.setOnClickListener {
            if (!isCompleted)
                setOnClickListener(question)
            else
                Toast.makeText(context, "Está questão já está concluída.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = questions.size
}