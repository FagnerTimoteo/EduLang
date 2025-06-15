package com.example.edulang

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.edulang.databinding.ActivityQuestionBinding

class QuestionActivity : ComponentActivity() {
    private lateinit var binding: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}