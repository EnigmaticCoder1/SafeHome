package com.example.safehomeapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color


class DeviceAdapter(private val deviceList : ArrayList<Device>) : RecyclerView.Adapter<DeviceAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_item,parent,false)
        return MyViewHolder(itemView,deviceList)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = deviceList[position]

        holder.deviceName.text = currentitem.device_name
        holder.deviceType.text = currentitem.type
        holder.deviceLocation.text = currentitem.location

        //Possible area to allow buttons to do something with pinging a log and deleting the device.
        //val sharedPreferences: SharedPreferences = holder.itemView.getContext().getSharedPreferences("save", MODE_PRIVATE)

    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    class MyViewHolder(itemView : View, private val deviceList : ArrayList<Device>) : RecyclerView.ViewHolder(itemView){
        val deviceName : TextView = itemView.findViewById(R.id.tvdeviceName)
        val deviceType : TextView = itemView.findViewById(R.id.tvdeviceType)
        val deviceLocation : TextView = itemView.findViewById(R.id.tvdeviceLocation)
        val activeSwitch = itemView.findViewById<Switch>(R.id.armSwitch)
        val notiSwitch = itemView.findViewById<Switch>(R.id.notificationSwitch)

        //Variables and imports
        val sharedPreferences: SharedPreferences = itemView.getContext().getSharedPreferences("save", MODE_PRIVATE)

        lateinit var notificationManager: NotificationManager
        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: Notification.Builder
        val channelId = "i.apps.notifications"
        val description = "Notification Sent"

        //Methods used for buttons such as creating logs and deleting devices
        init {
            //Create a switch the deactivates or activates device (Allows log button to work)
            activeSwitch.setChecked(sharedPreferences.getBoolean("value",true))
            activeSwitch?.setOnCheckedChangeListener { _, isChecked ->
                var message = ""
                if (isChecked) {
                    message = "Device Armed"
                    val editor: SharedPreferences.Editor =
                        itemView.getContext().getSharedPreferences("save", MODE_PRIVATE).edit()
                    editor.putBoolean("value",true)
                    editor.apply()
                    activeSwitch.setChecked(true)
                }
                else {
                    message = "Device Disarmed"
                    val editor: SharedPreferences.Editor =
                        itemView.getContext().getSharedPreferences("save", MODE_PRIVATE).edit()
                    editor.putBoolean("value",false);
                    editor.apply();
                    activeSwitch.setChecked(false);
                }
                Toast.makeText(
                    itemView.context, message,
                    Toast.LENGTH_SHORT
                ).show()

            }

            //Create a switch that activates 10 sec notifications
            val notificationManager =
                itemView.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            notiSwitch?.setOnCheckedChangeListener { _, isChecked ->
                //CREATE NOTIFICATION COMMANDS HERE
                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder = Notification.Builder(itemView.context)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(itemView.getContext().resources, R.drawable.ic_launcher_background))
                } else {
                    builder = Notification.Builder(itemView.context)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(itemView.getContext().resources, R.drawable.ic_launcher_background))
                }
                if (notificationManager != null) {
                    notificationManager.notify(1234, builder.build())
                }
                 */

            }
            //Creating Logs with a button
            val deviceButton : Button = itemView.findViewById(R.id.logButton)
            deviceButton.setOnClickListener{v: View ->
                val position: Int = adapterPosition
                //Toast.makeText(itemView.context, "You clicked on item # ${position+1}", Toast.LENGTH_LONG).show()
                val chosenDevice = deviceList[position]

                //Database access
                val database = FirebaseDatabase.getInstance()
                val ref = database.reference.child("Logs")
                val user = FirebaseAuth.getInstance().currentUser
                val curuid = user?.uid

                //Creating a new log entry in database

                //Time the log was created
                val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now()
                } else {
                    TODO("VERSION.SDK_INT < O")
                }

                val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm")
                val formatted = current.format(formatter)
                val logTime = formatted.toString()
                //The device, user, and logID that created the log
                val logDevice = chosenDevice.device_name
                val logUser = curuid.toString()
                val logID = Random.nextInt(0,2000000000).toString()
                //put in database

                if(activeSwitch.isChecked) {
                    val newLog = ref.child(logID)
                    newLog.child("time").setValue(logTime)
                    newLog.child("parent_device").setValue(logDevice)
                    newLog.child("user").setValue(logUser)

                    Toast.makeText(itemView.context, "A log was produced", Toast.LENGTH_LONG).show()
                }
                else
                    Toast.makeText(itemView.context, "Device is disarmed, rearm to produce log", Toast.LENGTH_LONG).show()


            }
            //Deleting device with a button
            val deleteDeviceButton : Button = itemView.findViewById(R.id.deleteButton)
            deleteDeviceButton.setOnClickListener { v: View ->
                val position: Int = adapterPosition
                //Toast.makeText(itemView.context, "You clicked on item # ${position+1}", Toast.LENGTH_LONG).show()
                val chosenDevice = deviceList[position].device_name

                //Database access
                val database = FirebaseDatabase.getInstance()
                val ref = database.reference.child("Devices")

                //Delete device
                ref.child(chosenDevice.toString()).removeValue()

                Toast.makeText(itemView.context, "Device Removed", Toast.LENGTH_LONG).show()


            }
        }


    }
}