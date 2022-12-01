package com.example.safehomeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_zonemanager.*

class zonemanager : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var zoneRecyclerView : RecyclerView
    private lateinit var zoneArrayList : ArrayList<Zone>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zonemanager)

        homeZoneButton.setOnClickListener{
            val home_intent = Intent(this, activity_home::class.java)
            startActivity(home_intent)
        }

        addZoneButton.setOnClickListener{
            val addZone_intent = Intent(this, addzone::class.java)
            startActivity(addZone_intent)
        }

        //Establish zone recycler view manager variables
        zoneRecyclerView = findViewById(R.id.zoneLayout)
        zoneRecyclerView.layoutManager = LinearLayoutManager(this)
        zoneRecyclerView.setHasFixedSize(true)

        zoneArrayList = arrayListOf<Zone>()

        //get zone data from firebase
        getZoneData()
    }
    private fun getZoneData(){
        dbref = FirebaseDatabase.getInstance().getReference("Zones")
        val user = FirebaseAuth.getInstance().currentUser
        val curuid = user?.uid

        //Collects Firebase data on Zones
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (zoneSnapshot in snapshot.children){
                        val zone = zoneSnapshot.getValue(Zone::class.java)
                        if (zone != null) {
                            if(zone.uid == curuid) {//Filter the database to devices that is owned by current user
                                zoneArrayList.add(zone!!)
                            }
                        }
                    }

                    zoneRecyclerView.adapter = ZoneAdapter(zoneArrayList) // Uses Device Adaptor

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}