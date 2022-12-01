package com.example.safehomeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*


class activity_register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Allow users the button to register
        confirm_register_button.setOnClickListener{
            registerNewUser()
        }
    }
    //Method takes user name, email address, and password and stores it in the firebase database.
    private fun registerNewUser() {
        val firstName = fnameRegister.text.toString()
        val lastName = lnameregister.text.toString()
        val mailAddr = emailregister.text.toString()
        val pwd = passwordregister.text.toString()
        val confirmPWD = confirmPassword.text.toString()

        if (TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "Please enter First Name", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Please enter Last Name", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(mailAddr)){
            Toast.makeText(this, "Please enter E-mail", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(confirmPWD) or (pwd != confirmPWD)){
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_LONG).show()
            return
        }

        val mAuth = FirebaseAuth.getInstance()
        //REALTIME DATABASE SETUP CODE
        val database = FirebaseDatabase.getInstance()

        val ref = database.reference.child("Users")

        //Firestore DATABASE SETUP CODE
        //val db = Firebase.firestore

        mAuth.createUserWithEmailAndPassword(mailAddr, pwd).addOnCompleteListener{task: Task<AuthResult> ->
            if(task.isSuccessful){
                val userID = mAuth.currentUser!!.uid
                //Realtime Database input code
                val currentUser = ref.child(userID)

                currentUser.child("firstName").setValue(firstName)
                currentUser.child("lastName").setValue(lastName)
                currentUser.child("e-mail").setValue(mailAddr)

                //Firestore Database Input Code
                /*
                val user = hashMapOf(
                    "fname" to firstName,
                    "lname" to lastName,
                    "email" to mailAddr
                )

                db.collection("Users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
                */
                Toast.makeText(this, "Successful", Toast.LENGTH_LONG).show()

                val loginIntent = Intent(this, MainActivity::class.java)
                startActivity(loginIntent)

            } else {
                Toast.makeText(this, "SignUp Failed!", Toast.LENGTH_LONG).show()
            }
        }

    }

}