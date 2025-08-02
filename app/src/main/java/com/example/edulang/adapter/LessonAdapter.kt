package com.example.edulang.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edulang.R
import com.example.edulang.data.model.Lesson
import com.example.edulang.util.applyDynamicTitleStyle

class LessonAdapter(
    private val lessons: List<Lesson>,
    private val setOnClickListener: (Lesson) -> Unit) :
    RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
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
}