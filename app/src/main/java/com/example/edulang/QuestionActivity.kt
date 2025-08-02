package com.example.edulang

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.example.edulang.data.model.Lesson
import com.example.edulang.data.model.QuestionType
import com.example.edulang.databinding.ActivityQuestionBinding
import com.example.edulang.util.Progress.saveProgress
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class QuestionActivity : ComponentActivity() {
    private lateinit var binding: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lessonId = intent.getIntExtra("lessonId", -1)
        val questionId = intent.getIntExtra("questionId", -1)
        val lessons = loadLessons(this)
        val lesson = lessons.find { it.id == lessonId }
        val question = lesson?.questions?.find { it.id == questionId }

        if (question == null) {
            Toast.makeText(this, "Questão não encontrada!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val questionTextContent = question.questionText
        binding.questionText.text = questionTextContent

        // Estilo dinâmico para o texto da questão
        val typeface = Typeface.createFromAsset(assets, "fonts/KGHAPPYSolid.ttf")
        binding.questionText.typeface = typeface
        binding.questionText.setTextColor(ContextCompat.getColor(this, R.color.yellow))
        binding.questionText.setShadowLayer(20f, 0f, 0f, Color.BLACK)

        when (question.type) {
            QuestionType.MULTIPLE_CHOICE -> {
                binding.radioGroupOptions.visibility = View.VISIBLE
                question.options?.forEach { option ->
                    val radioButton = RadioButton(this)
                    radioButton.text = option
                    radioButton.textSize = 18f
                    radioButton.typeface = typeface
                    radioButton.setTextColor(ContextCompat.getColor(this, R.color.black))
                    val colorStateList = ContextCompat.getColorStateList(this, R.color.green)
                    radioButton.buttonTintList = colorStateList
                    binding.radioGroupOptions.addView(radioButton)
                }
            }
            QuestionType.TEXT_INPUT -> {
                binding.answerInput.visibility = View.VISIBLE
                binding.answerInput.typeface = typeface
                binding.answerInput.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        binding.submitButton.setOnClickListener {
            val userAnswer = when (question.type) {
                QuestionType.MULTIPLE_CHOICE -> {
                    val checkedRadioButtonId = binding.radioGroupOptions.checkedRadioButtonId
                    if (checkedRadioButtonId != -1) {
                        val checkedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
                        checkedRadioButton.text.toString()
                    } else {
                        null
                    }
                }
                QuestionType.TEXT_INPUT -> {
                    binding.answerInput.text.toString()
                }
            }?.trim()?.lowercase() ?: ""

            val isCorrect = userAnswer == question.correctAnswer.trim().lowercase()

            if (isCorrect) {
                // Linha 88 (ou próxima) - Chamada corrigida da função
                saveProgress(this, lessonId, question.id)
                val toast = Toast.makeText(this, "Correto!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                finish()
            } else {
                val toast = Toast.makeText(this, "Incorreto. Tente novamente.", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
    }

    private fun loadLessons(context: Context): List<Lesson> {
        val json = context.assets.open("lessons.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<Lesson>>() {}.type
        return Gson().fromJson(json, type)
    }
}