package com.uqac.motivz.ui.profil

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.MainActivity
import com.uqac.motivz.R
import com.uqac.motivz.databinding.ActivityProfilBinding


class ProfilActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityProfilBinding
    private lateinit var pseudo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_profil)

        var pseudoLbl = findViewById<TextView>(R.id.pseudoProfil)

        val auth= FirebaseAuth.getInstance()

        val database = Firebase.database

        val myRef = database.getReference("users").child(auth.uid.toString()).child("pseudo")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                val value = dataSnapshot.getValue<String>()
                pseudo = dataSnapshot.getValue<String>().toString()
                pseudoLbl.setText(pseudo)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })



        /*
        binding.goBackBtn.setOnClickListener(){
            goToMainActivity(R.id.navigation_home)
        }
        */

        binding.personnaliserBtn.setOnClickListener(){
            goToMainActivity(R.id.navigation_shop)
        }

    }

    private fun goToMainActivity(fragmentSelected : Int){
        val intent = Intent(this,  MainActivity::class.java)
        intent.putExtra("PSEUDONYME", pseudo);
        intent.putExtra("NAV", fragmentSelected);
        startActivity(intent)
    }
}