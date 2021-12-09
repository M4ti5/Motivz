package com.uqac.motivz

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
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
import kotlinx.coroutines.awaitAll
import java.time.LocalDateTime
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth

    private lateinit var user : FirebaseUser
    private lateinit var uid : String
    private lateinit var pseudo: String

    private var attendance:Int = 0


    fun updateAttendance(){
        lateinit var lastConnexion:List<String>

        database.reference.child("users").child(uid).get().addOnSuccessListener {

            attendance = it.child("attendance").value.toString().toInt()

            lastConnexion = it.child("lastLog").value.toString().split("/")

            var lastConnexionDay:Int = lastConnexion.get(0).toInt()

            var lastConnexionYear:Int = lastConnexion.get(1).toInt()

            val currentDay = LocalDateTime.now().dayOfYear
            val currentYear = LocalDateTime.now().year

            if(lastConnexionYear == currentYear && lastConnexionDay >= currentDay -1){
                if(lastConnexionDay != currentDay){
                    attendance += 1
                }
            } else {
                attendance = 0
            }

            database.reference.child("users").child(uid).child("attendance").setValue(attendance.toString())
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

        ////Other
        updateAttendance()

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

    override fun onStart() {
        super.onStart()

        if(auth.currentUser != null){
            database.reference.child("users").child(uid).child("lastLog").setValue(
                LocalDateTime.now().dayOfYear.toString() + "/" + LocalDateTime.now().year.toString())
        }
    }


    override fun onDestroy() {

        auth.signOut()
        super.onDestroy()
    }
}