package com.example.safehomeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show()
        //Allows user to login
        home_button.setOnClickListener {
            loginUser()
        }
        //Allow users to register as a new user
        register_button.setOnClickListener {
            val register_intent = Intent(this, activity_register::class.java)
            startActivity(register_intent)
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        Toast.makeText(this, "Please Login", Toast.LENGTH_LONG).show()
        return
    }
    //The method below allows the app to detect user input and verify the login based on firebase database
    private fun loginUser() {
        val mailAddr = emailText.text.toString()
        val pwd = passwordText.text.toString()

        if (TextUtils.isEmpty(mailAddr)) {
            Toast.makeText(this, "Please enter Email Address", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_LONG).show()
            return
        }

        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(mailAddr, pwd)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()

                    val homeIntent = Intent(this, activity_home::class.java)
                    startActivity(homeIntent)

                } else {
                    Toast.makeText(this, "Login Failed! Try Again", Toast.LENGTH_LONG).show()
                }
            }
    }
}