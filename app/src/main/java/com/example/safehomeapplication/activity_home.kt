package com.example.safehomeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class activity_home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        add_Button.setOnClickListener{
            val add_intent = Intent(this, activity_adddevice::class.java)
            startActivity(add_intent)
        }

        logButton.setOnClickListener{
            val log_intent = Intent(this, activity_logs::class.java)
            startActivity(log_intent)
        }

        logout_button.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val logout_intent = Intent(this, MainActivity::class.java)
            startActivity(logout_intent)
        }
    }
}