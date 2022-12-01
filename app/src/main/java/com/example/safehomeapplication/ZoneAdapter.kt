package com.example.safehomeapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ZoneAdapter(private val zoneList : ArrayList<Zone>) : RecyclerView.Adapter<ZoneAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZoneAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.zone_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = zoneList[position]

        holder.zoneName.text = currentitem.zone_name

        //This allows the delete button to function
        holder.deleteZoneButton.setOnClickListener { v: View ->
            val chosenDevice = zoneList[position].zone_name

            //Database access
            val database = FirebaseDatabase.getInstance()
            val ref = database.reference.child("Zones")

            //Delete device
            ref.child(chosenDevice.toString()).removeValue()

            Toast.makeText(holder.itemView.context, "Zone Removed", Toast.LENGTH_LONG).show()


        }
    }

    override fun getItemCount(): Int {
        return zoneList.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val zoneName: TextView = itemView.findViewById(R.id.zoneName)
        val deleteZoneButton : Button = itemView.findViewById(R.id.zoneDeleteButton)

    }

}