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
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_device_information.*

class activity_deviceInformation : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    /*
    var deviceTypes = arrayOf<String>("Sensor", "Alarm",
        "Camera")

     */

    var deviceTypes = ArrayList<String>()

    //INSERT USER CREATED ZONES
    override fun onCreate(savedInstanceState: Bundle?) {
        //Fetch zone data
        //getZones()


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_information)

        deviceTypes.add("Sensor")
        deviceTypes.add("Alarm")
        deviceTypes.add("Camera")

        //Spinner for device types
        val spin = findViewById<Spinner>(R.id.inputDeviceType2)
        //spin.onItemSelectedListener = this

        val ad: ArrayAdapter<String> = ArrayAdapter<String>(
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


        // CODE TO GATHER ZONE DATA FROM FIREBASE
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Zones")

        val user = FirebaseAuth.getInstance().currentUser
        val curuid = user?.uid

        var zoneList = ArrayList<String>()

        //Collects Firebase data on Zones
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (zoneSnapshot in snapshot.children) {
                        val zone = zoneSnapshot.getValue(Zone::class.java)
                        if (zone != null) {
                            if (zone.uid == curuid) {//Filter the database to zonews that is owned by current user
                                zoneList.add(zone.zone_name.toString())
                            }
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        //Spinner for Zone selection
        val zoneSpin = findViewById<Spinner>(R.id.inputDeviceZone)
        //zoneSpin.onItemSelectedListener = this

        val ad2:ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            zoneList
        )

        ad2.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)

        zoneSpin.adapter = ad2

        submitDevice2.setOnClickListener{
            registerNewDevice()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        /*
        when(p0){
            inputDeviceType2 -> Toast.makeText(getApplicationContext(),
                deviceTypes[p2],
                Toast.LENGTH_LONG)
                .show()

            inputDeviceZone -> Toast.makeText(getApplicationContext(),
                zoneArrayList[p2],
                Toast.LENGTH_LONG)
                .show()
        }

         */
    }

    private fun getZones() : Array<String>{
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Zones")

        val user = FirebaseAuth.getInstance().currentUser
        val curuid = user?.uid

        val zoneList = ArrayList<String>()

        //Collects Firebase data on Zones
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (zoneSnapshot in snapshot.children) {
                        val zone = zoneSnapshot.getValue(Zone::class.java)
                        if (zone != null) {
                            if (zone.uid == curuid) {//Filter the database to zonews that is owned by current user
                                zoneList.add(zone.zone_name.toString())
                            }
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        val arrayCopy = Array(zoneList.size, {i -> i.toString()})

        for(i in 0..arrayCopy.size-1){
            arrayCopy[i] = zoneList[i]
        }

        return arrayCopy.clone()
    }

    private fun registerNewDevice() {
        val deviceName = inputDeviceName2.text.toString()
        val deviceLocation = inputDeviceLocation2.text.toString()
        val deviceType = inputDeviceType2.selectedItem.toString()
        //val deviceZone = inputDeviceZone.selectedItem.toString()

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

        currentDevice.child("device_name").setValue(deviceName)
        currentDevice.child("location").setValue(deviceLocation)
        currentDevice.child("type").setValue(deviceType)
        //currentDevice.child("zone").setValue(deviceZone)
        currentDevice.child("uid").setValue(userID)

        Toast.makeText(this, "Successful", Toast.LENGTH_LONG).show()

        val adddevice_intent = Intent(this, activity_adddevice::class.java)
        startActivity(adddevice_intent)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(this, "Please enter device's Type", Toast.LENGTH_LONG).show()
        return
    }
}