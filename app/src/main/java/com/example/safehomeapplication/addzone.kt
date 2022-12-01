package com.example.safehomeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_adddevice.*
import kotlinx.android.synthetic.main.activity_addzone.*
import kotlinx.android.synthetic.main.activity_device_information.*

class addzone : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addzone)

        submitZone.setOnClickListener{
            createNewZone()
        }

    }
    private fun createNewZone(){
        val zoneHeight = inputZoneHeight.text.toString()
        val zoneWidth = inputZoneWidth.text.toString()
        val zoneLength = inputZoneLength.text.toString()
        val zoneName = inputZoneName.text.toString()

        if (TextUtils.isEmpty(zoneHeight) || zoneHeight?.toIntOrNull() == null){
            Toast.makeText(this, "Please enter zone's Height", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(zoneWidth) || zoneWidth?.toIntOrNull() == null){
            Toast.makeText(this, "Please enter zone's Width", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(zoneLength) || zoneLength?.toIntOrNull() == null){
            Toast.makeText(this, "Please enter zone's Length", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(zoneName)){
            Toast.makeText(this, "Please enter zone's name", Toast.LENGTH_LONG).show()
            return
        }

        val mAuth = FirebaseAuth.getInstance()
        //REALTIME DATABASE SETUP CODE
        val database = FirebaseDatabase.getInstance()

        val ref = database.reference.child("Zones")

        val userID = mAuth.currentUser!!.uid

        val currentZone = ref.child(zoneName)

        currentZone.child("zone_name").setValue(zoneName)
        currentZone.child("width").setValue(zoneWidth.toInt())
        currentZone.child("height").setValue(zoneHeight.toInt())
        currentZone.child("length").setValue(zoneLength.toInt())
        currentZone.child("uid").setValue(userID)

        Toast.makeText(this, "Successful", Toast.LENGTH_LONG).show()

        val submitZone_intent = Intent(this, zonemanager::class.java)
        startActivity(submitZone_intent)
    }
}