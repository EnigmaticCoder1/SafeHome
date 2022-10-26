package com.example.safehomeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_device_information.*

class activity_deviceInformation : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var deviceTypes = arrayOf<String?>("Sensor", "Alarm",
        "Camera", "Audio")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_information)
        val spin = findViewById<Spinner>(R.id.inputDeviceType2)
        spin.onItemSelectedListener = this

        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_item,
            deviceTypes)

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spin.adapter = ad

        submitDevice2.setOnClickListener{
            registerNewDevice()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Toast.makeText(getApplicationContext(),
            deviceTypes[p2],
            Toast.LENGTH_LONG)
            .show()
    }
    private fun registerNewDevice() {
        val deviceName = inputDeviceName2.text.toString()
        val deviceLocation = inputDeviceLocation2.text.toString()
        val deviceType = inputDeviceType2.selectedItem.toString()

        if (TextUtils.isEmpty(deviceName)){
            Toast.makeText(this, "Please enter device's Name", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(deviceLocation)){
            Toast.makeText(this, "Please enter device's Location", Toast.LENGTH_LONG).show()
            return
        }

        val mAuth = FirebaseAuth.getInstance()
        //REALTIME DATABASE SETUP CODE
        val database = FirebaseDatabase.getInstance()

        val ref = database.reference.child("Devices")

        val userID = mAuth.currentUser!!.uid

        val currentDevice = ref.child(deviceName)

        currentDevice.child("location").setValue(deviceLocation)
        currentDevice.child("type").setValue(deviceType)
        currentDevice.child("uid").setValue(userID)

        Toast.makeText(this, "Successful", Toast.LENGTH_LONG).show()

        val adddevice_intent = Intent(this, activity_adddevice::class.java)
        startActivity(adddevice_intent)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}