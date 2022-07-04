package com.example.kotlinquizapp.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.KotlinApp.QuizApp.adapters.OptionAdapter
import com.KotlinApp.QuizApp.models.Question
import com.KotlinApp.QuizApp.models.Quiz
import com.example.kotlinquizapp.R
import com.google.firebase.database.collection.LLRBNode
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.NonCancellable.cancel
import java.util.*

class QuestionActivity : AppCompatActivity() {

    var quizzes: MutableList<Quiz>? = null
    var questions: MutableMap<String, Question>? = null
    var index = 1


   /* lateinit var btnPrevious: Button*/
    lateinit var btnNext: Button
    lateinit var btnSubmit: Button
    lateinit var txtViewTimer: TextView
    lateinit var description: TextView
    lateinit var optionList: RecyclerView
    lateinit var cdTimer : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        cdTimer = getTimer()



/*        btnPrevious = findViewById(R.id.btnPrevious)*/
        btnNext = findViewById(R.id.btnNext)
        btnSubmit = findViewById(R.id.btnSubmit)
        description = findViewById(R.id.description)
        optionList = findViewById(R.id.optionList)
        txtViewTimer = findViewById(R.id.txtViewTimer)

        setUpFirestore()
        setUpEventListener()

    }

    private  fun startTimer(){
        cdTimer.start()
    }
    private fun restartTimer(){
        cdTimer.cancel()
        cdTimer.start()
        txtViewTimer.setTextColor(Color.GRAY)
    }
    private fun setUpEventListener() {
   /*     btnPrevious.setOnClickListener {
            index--
            bindViews()
        }*/

        btnNext.setOnClickListener {
            index++
            bindViews()
            restartTimer()
        }

        btnSubmit.setOnClickListener {
            Log.d("FINALQUIZ", questions.toString())

            val intent = Intent(this, ResultActivity::class.java)
            val json = Gson().toJson(quizzes!![0])
            intent.putExtra("QUIZ", json)
            startActivity(intent)
        }
        startTimer()
    }

    private fun setUpFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        var date = intent.getStringExtra("DATE")
        if (date != null) {
            firestore.collection("quizzes").whereEqualTo("title", date)
                .get()
                .addOnSuccessListener {
                    if (it != null && !it.isEmpty) {
                        quizzes = it.toObjects(Quiz::class.java)
                        questions = quizzes!![0].questions
                        bindViews()
                    }
                }
        }
    }

    private fun bindViews() {
    /*    btnPrevious.visibility = View.GONE*/
        btnSubmit.visibility = View.GONE
        btnNext.visibility = View.GONE

        if (index == 1) { //first question
            btnNext.visibility = View.VISIBLE
        } else if (index == questions!!.size) { // last question
            btnSubmit.visibility = View.VISIBLE
       /*     btnPrevious.visibility = View.VISIBLE*/
        } else { // Middle
      /*      btnPrevious.visibility = View.VISIBLE*/
            btnNext.visibility = View.VISIBLE
        }

        val question = questions!!["question$index"]
        question?.let {
            description.text = it.description
            val optionAdapter = OptionAdapter(this, it)
            optionList.layoutManager = LinearLayoutManager(this)
            optionList.adapter = optionAdapter
            optionList.setHasFixedSize(true)
        }
    }

    private fun getTimer():CountDownTimer {
        val intent = Intent(this, ResultActivity::class.java)
        val timer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txtViewTimer.setText("00:" + millisUntilFinished / 1000)
                if(millisUntilFinished / 1000 < 6)
                {
                    txtViewTimer.setTextColor(Color.RED)
                }
            }

            override fun onFinish() {

                    if (index != questions!!.size) {
                        index++
                        bindViews()
                        restartTimer()
                    } else {
                        val json = Gson().toJson(quizzes!![0])
                        intent.putExtra("QUIZ", json)
                        startActivity(intent)
                    }
            }
        }
            return timer
        }
    }