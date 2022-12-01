package com.example.safehomeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_logs.*

class activity_logs : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var logRecyclerView : RecyclerView
    private lateinit var logArrayList : ArrayList<Log>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logs)

        homeButton2.setOnClickListener{
            val home_intent = Intent(this, activity_home::class.java)
            startActivity(home_intent)
        }

        logout_button.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val login_intent = Intent(this, MainActivity::class.java)
            startActivity(login_intent)
            finish()
        }

        logRecyclerView = findViewById(R.id.logLayout)
        logRecyclerView.layoutManager = LinearLayoutManager(this)
        logRecyclerView.setHasFixedSize(true)

        logArrayList = arrayListOf<Log>()

        getLogsData()
    }
    private fun getLogsData(){
        dbref = FirebaseDatabase.getInstance().getReference("Logs")
        val user = FirebaseAuth.getInstance().currentUser
        val curuid = user?.uid

        //Collects Log data from Firebase
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (logSnapshot in snapshot.children){
                        val log = logSnapshot.getValue(Log::class.java)
                        if (log != null) {
                            if(log.user == curuid) {
                                logArrayList.add(log!!)
                            }
                        }
                    }
                    logRecyclerView.adapter = LogAdapter(logArrayList) // Uses Device Adaptor

                    /*
                    LogAdapter(logArrayList).setonItemClickListener(object:LogAdapter.onItemClickListener){

                    }
                    */
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}