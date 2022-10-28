package com.example.safehomeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_adddevice.*
import android.text.TextUtils
import android.view.inputmethod.TextSnapshot
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class activity_adddevice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adddevice)

        //displayDevices()

        homeButton.setOnClickListener{
            val home_intent = Intent(this, activity_home::class.java)
            startActivity(home_intent)
        }

        addDeviceButton.setOnClickListener{
            val addDevice_intent = Intent(this, activity_deviceInformation::class.java)
           startActivity(addDevice_intent)
        }
    }

    private fun displayDevices(){
        var layout: LinearLayout = this.findViewById(R.id.deviceLayout)
        var row: LinearLayout = LinearLayout(this)

        val mAuth = FirebaseAuth.getInstance()
        //REALTIME DATABASE SETUP CODE
        var ref = FirebaseDatabase.getInstance().getReference("Devices")

        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val children = dataSnapshot!!.children
                children.forEach{
                    //Here is what produces
                    println(it.toString())
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
            }
        }


        val userID = mAuth.currentUser!!.uid
        ref.addValueEventListener(menuListener)
        //Realtime Database input code

    }
}