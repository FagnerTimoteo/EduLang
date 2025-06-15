package com.example.edulang.data.model

data class Question(
    val id: Int,
    val questionText: String,
    val type: QuestionType,
    val options: List<String>?, // apenas para escolha múltipla
    val correctAnswer: String // serve para texto livre ou comparação
)

enum class QuestionType {
    MULTIPLE_CHOICE,
    TEXT_INPUT
}
