package com.example.safehomeapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeviceAdapter(private val deviceList : ArrayList<Device>) : RecyclerView.Adapter<DeviceAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = deviceList[position]

        holder.deviceName.text = currentitem.device_name
        holder.deviceType.text = currentitem.type
        holder.deviceLocation.text = currentitem.location

    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val deviceName : TextView = itemView.findViewById(R.id.tvdeviceName)
        val deviceType : TextView = itemView.findViewById(R.id.tvdeviceType)
        val deviceLocation : TextView = itemView.findViewById(R.id.tvdeviceLocation)

    }
}