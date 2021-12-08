package com.uqac.motivz

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.databinding.ActivityMainBinding
import com.uqac.motivz.ui.home.HomeFragment
import com.uqac.motivz.ui.stats.DataViewModel
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pseudo: String
    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private lateinit var uid : String

    fun resetAttendance(){
        database.reference.child("users").child(uid!!).child("assiduité").setValue(0)
    }

    fun updateAttendance(){
        val currentDay = LocalDateTime.now().dayOfYear
        val currentYear = LocalDateTime.now().year
        val lastConnexion = data.child("dernière connexion").getValue().toString().split("/")
        val lastConnexionDay = lastConnexion[0].toInt()
        val lastConnexionYear = lastConnexion[1].toInt()
        var attendance = data.child("assiduité").getValue().toString().toInt()
        if(lastConnexionYear == currentYear){
            if(currentDay - lastConnexionDay == 1){
                attendance = attendance + 1
                database.reference.child("users").child(uid!!).child("assiduity").setValue(attendance.toString())
            } else {
                resetAttendance()
            }
        } else {
            resetAttendance()
        }
    }

    fun getPseudo(): String {
        return pseudo
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ////Init database
        database = Firebase.database
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            user = auth.currentUser!!
            uid = user.uid
        }

        if(auth.currentUser != null){
            database.reference.child("users").child(uid).get().addOnSuccessListener{
                updateAttendance(data)
                val lastConnexion = data.child("lastLog").getValue().toString().split("/")
                val lastConnexionDay = lastConnexion[0].toInt()
                val lastConnexionYear = lastConnexion[1].toInt()
                var attendance = data.child("assiduity").getValue().toString().toInt()
                if(lastConnexionYear == currentYear){
                    if(currentDay - lastConnexionDay == 1){
                        attendance = attendance + 1
                        database.child("users").child(uid).child("assiduity").setValue(attendance.toString())
                    }
                }
            }
        }


        database.reference.child("users").child(uid).child("pseudo").get().addOnSuccessListener{
            pseudo = it.value.toString()
        }

        //// Nav-Bar
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide();
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_shop, R.id.navigation_home,  R.id.navigation_stats ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val nav = intent.getIntExtra("NAV", R.id.navigation_home)
        navView.selectedItemId = nav
    }

    override fun onStop() {
        //auth.signOut()
        if(auth.currentUser != null){
            database.child("users")
                .child(uid!!)
                .child("dernière connexion")
                .setValue(LocalDateTime.now()
                    .dayOfYear
                    .toString()
                        + "/"
                        + LocalDateTime.now().year.toString())
        }

        super.onStop()
    }

    override fun onDestroy() {

        auth.signOut()
        super.onDestroy()
    }
}