package com.example.edulang

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.edulang.databinding.ActivityLessonBinding

class LessonActivity : ComponentActivity()  {
    private lateinit var binding: ActivityLessonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}