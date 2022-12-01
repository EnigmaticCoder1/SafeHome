package com.example.safehomeapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LogAdapter(private val logList : ArrayList<Log>) : RecyclerView.Adapter<LogAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.log_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = logList[position]

        holder.logDevice.text = currentitem.parent_device
        holder.logTime.text = currentitem.time
    }

    override fun getItemCount(): Int {
        return logList.size
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val logDevice: TextView = itemView.findViewById(R.id.logDeviceName)
        val logTime: TextView = itemView.findViewById(R.id.logDeviceDate)
    }

}
