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
import com.example.edulang.data.model.Lesson

class LessonAdapter(private val lessons: List<Lesson>,
                    private val  assets: AssetManager,
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

        // Texto da barra de progresso
        val barText = holder.title
        barText.text = lesson.title

        // Estilizar barra de progresso
        val typeface = Typeface.createFromAsset(assets, "fonts/KGHAPPYSolid.ttf")
        barText.typeface = typeface
        barText.textSize = 32f
        barText.setTextColor(Color.YELLOW)
        barText.setShadowLayer(20f, 0f, 0f, Color.BLACK)

        holder.itemView.setOnClickListener {
            setOnClickListener(lesson)
        }
    }

    override fun getItemCount(): Int = lessons.size
}
