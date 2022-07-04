package com.example.kotlinquizapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.kotlinquizapp.R
import com.google.firebase.auth.FirebaseAuth


class SignupActivity : AppCompatActivity() {

    lateinit var etEmail : EditText
    lateinit var etConfirmPassword : EditText
    lateinit var etPassword : EditText
    lateinit var btnSignup : Button
    lateinit var btnRedirectLogin : TextView


    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        firebaseAuth = FirebaseAuth.getInstance()

        btnSignup = findViewById<Button>(R.id.btnSignUp)
        btnRedirectLogin = findViewById<TextView>(R.id.btnLogin)
        etEmail = findViewById<EditText>(R.id.etEmailAddress)
        etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        etPassword = findViewById<EditText>(R.id.etPassword)

         btnSignup.setOnClickListener()
         {
             signUpUser()
         }
        btnRedirectLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun signUpUser() {
        val  email = etEmail.text.toString()
        val  password = etPassword.text.toString()
        val  confirmPassword = etConfirmPassword.text.toString()

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if(password.length < 8)
        {
            Toast.makeText(this, "Password length needs to be at least 8 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    Toast.makeText(this, "Error, user couldn't be created", Toast.LENGTH_SHORT).show()
                }
            }
    }

}



