package com.example.kotlinquizapp.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.TextView
import android.widget.Toast
import com.KotlinApp.QuizApp.models.Quiz
import com.example.kotlinquizapp.R
import com.google.gson.Gson

class ResultActivity : AppCompatActivity() {
    lateinit var quiz: Quiz
    lateinit var txtFeedback : TextView

    private lateinit var txtAnswer : TextView
    private lateinit var txtScore : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        txtFeedback = findViewById(R.id.txtFeedback)
        txtAnswer = findViewById(R.id.txtAnswer)
        txtScore = findViewById(R.id.txtScore)

        setUpViews()
    }

    private fun setUpViews() {
        val quizData = intent.getStringExtra("QUIZ")
        quiz = Gson().fromJson<Quiz>(quizData, Quiz::class.java)
        calculateScore()
        setAnswerView()
    }

    private fun setAnswerView() {
        val builder = StringBuilder("")
        for (entry in quiz.questions.entries) {
            val question = entry.value
            builder.append("<font color'#18206F'><b>Question: ${question.description}</b></font><br/><br/>")
            builder.append("<font color='#009688'>Answer: ${question.answer}</font><br/><br/>")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtAnswer.text = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT);
        } else {
            txtAnswer.text = Html.fromHtml(builder.toString());
        }
    }

    private fun calculateScore() {
        var correctAnsw = 0
        for (entry in quiz.questions.entries) {
            val question = entry.value
            if (question.answer == question.userAnswer) {
                correctAnsw += 1

            }
        }
        val question = quiz.questions.entries.size
        val finalResult = (correctAnsw/question)*100
        txtScore.text = "Your Score : $finalResult%"
        if(finalResult == 0)
        {
            txtFeedback.setText("0?? Get real...")
        }
        else if(finalResult < 30)
        {
            txtFeedback.setText("You can do better")
        }
        else if(finalResult < 60)
        {
            txtFeedback.setText("Not bad!")
        }
        else if(finalResult < 95)
        {
            txtFeedback.setText("Amazing!")
        }
        else
            txtFeedback.setText("Nerd...")
    }


}