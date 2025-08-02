package com.example.edulang.adapter

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edulang.R
import com.example.edulang.data.model.Question
import com.example.edulang.util.Progress.recoverProgress
import com.example.edulang.util.applyDynamicTitleStyle

class QuestionAdapter(
    private val context: Context,
    private val questions: List<Question>,
    private val assets: AssetManager,
    private val lessonId: Int,
    private val setOnClickListener: (Question) -> Unit) :
    RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
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

        val isCompleted = recoverProgress(context, lessonId, question.id)

        if (isCompleted) {
            holder.itemView.isClickable = false
            holder.itemView.alpha = 0.5f
            barText.applyDynamicTitleStyle(
                textToFit = formattedQuestionTitle,
                maxTextSizeSp = 20f,
                minTextSizeSp = 16f,
                textColorResId = R.color.green,
                shadowRadius = 20f,
                shadowColor = Color.BLACK,
                fontAssetPath = "fonts/KGHAPPYSolid.ttf"
            )
        } else {
            holder.itemView.isClickable = true
            holder.itemView.alpha = 1.0f
            barText.applyDynamicTitleStyle(
                textToFit = formattedQuestionTitle,
                maxTextSizeSp = 20f,
                minTextSizeSp = 16f,
                textColorResId = R.color.yellow,
                shadowRadius = 20f,
                shadowColor = Color.BLACK,
                fontAssetPath = "fonts/KGHAPPYSolid.ttf"
            )
            holder.itemView.setOnClickListener {
                setOnClickListener(question)
            }
        }
    }

    override fun getItemCount(): Int = questions.size
}