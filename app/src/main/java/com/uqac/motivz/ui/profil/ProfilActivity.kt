package com.uqac.motivz.ui.profil

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.MainActivity
import com.uqac.motivz.R
import com.uqac.motivz.databinding.ActivityProfilBinding


class ProfilActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityProfilBinding
    private lateinit var uid: String
    private lateinit var database:FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getSupportActionBar()?.hide()

        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_profil)


        val auth= FirebaseAuth.getInstance()
        database = Firebase.database
        uid = intent.getStringExtra("UID").toString()


        var pseudoLabel = findViewById<TextView>(R.id.pseudoProfil)

        database.reference.child("users").child(uid).child("pseudo").get().addOnSuccessListener {
            pseudoLabel.text =  it.value.toString()
        }

        findViewById<ImageButton>(R.id.goBackBtn).setOnClickListener() {
            goToMainActivity(R.id.navigation_home)
        }

        findViewById<Button>(R.id.personnaliserBtn).setOnClickListener() {
            goToMainActivity(R.id.navigation_shop)
        }


    }

    private fun goToMainActivity(fragmentSelected : Int){
        val intent = Intent(this,  MainActivity::class.java)
        intent.putExtra("NAV", fragmentSelected);
        startActivity(intent)
    }
}