package com.example.edulang.adapter

import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edulang.R
import com.example.edulang.data.model.Question

class QuestionAdapter(
    private val questions: List<Question>,
    private val  assets: AssetManager,
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

        // Texto da barra de progresso
        val barText = holder.title
        barText.text = question.questionText

        // Estilizar barra de progresso
        val typeface = Typeface.createFromAsset(assets, "fonts/KGHAPPYSolid.ttf")
        barText.typeface = typeface
        barText.textSize = 32f
        barText.setTextColor(Color.YELLOW)
        barText.setShadowLayer(20f, 0f, 0f, Color.BLACK)

        holder.itemView.setOnClickListener {
            setOnClickListener(question)
        }
    }

    override fun getItemCount(): Int = questions.size
}

