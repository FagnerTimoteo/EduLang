package com.example.edulang

import android.content.Context
import android.graphics.Color
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

        // Obtém a questão da Intent
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

        // Exibe o texto da questão
        binding.questionText.text = question.questionText

        // Lógica para lidar com os tipos de questão
        when (question.type) {
            QuestionType.MULTIPLE_CHOICE -> {
                binding.radioGroupOptions.visibility = View.VISIBLE
                question.options?.forEach { option ->
                    val radioButton = RadioButton(this)
                    radioButton.text = option
                    radioButton.textSize = 18f
                    radioButton.setTextColor(Color.WHITE)

                    // Altera a cor do botão (círculo)
                    val colorStateList = ContextCompat.getColorStateList(this, R.color.green)
                    radioButton.buttonTintList = colorStateList

                    binding.radioGroupOptions.addView(radioButton)
                }
            }
            QuestionType.TEXT_INPUT -> {
                binding.answerInput.visibility = View.VISIBLE
            }
        }

        // Ação do botão de envio
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

            // Validação da resposta
            val isCorrect = userAnswer == question.correctAnswer.trim().lowercase()

            if (isCorrect) {
                // Salva o progresso e mostra feedback
                saveProgress(this, lessonId, question.id)
                val toast = Toast.makeText(this, "Correto!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                finish() // Volta para a LessonActivity
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