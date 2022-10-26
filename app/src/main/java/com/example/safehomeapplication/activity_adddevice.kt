package com.example.safehomeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_adddevice.*

class activity_adddevice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adddevice)

        homeButton.setOnClickListener{
            val home_intent = Intent(this, activity_home::class.java)
            startActivity(home_intent)
        }

        addDeviceButton.setOnClickListener{
            val addDevice_intent = Intent(this, activity_deviceInformation::class.java)
           startActivity(addDevice_intent)
        }
    }
}