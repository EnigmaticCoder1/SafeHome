package com.example.safehomeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_adddevice.*
import android.text.TextUtils
import android.view.inputmethod.TextSnapshot
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class activity_adddevice : AppCompatActivity() {
    private lateinit var dbref:DatabaseReference
    private lateinit var deviceRecyclerView : RecyclerView
    private lateinit var deviceArrayList : ArrayList<Device>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adddevice)

        deviceRecyclerView = findViewById(R.id.deviceLayout)
        deviceRecyclerView.layoutManager = LinearLayoutManager(this)
        deviceRecyclerView.setHasFixedSize(true)

        deviceArrayList = arrayListOf<Device>()

        //get Device data from firebase
        getDeviceData()




        homeButton.setOnClickListener{
            val home_intent = Intent(this, activity_home::class.java)
            startActivity(home_intent)
        }

        addDeviceButton.setOnClickListener{
            val addDevice_intent = Intent(this, activity_deviceInformation::class.java)
           startActivity(addDevice_intent)
        }
    }
    private fun getDeviceData(){
        dbref = FirebaseDatabase.getInstance().getReference("Devices")

        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (deviceSnapshot in snapshot.children){
                        val device = deviceSnapshot.getValue(Device::class.java)
                        deviceArrayList.add(device!!)
                    }
                    deviceRecyclerView.adapter = DeviceAdapter(deviceArrayList) // Uses Device Adaptor
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



}
