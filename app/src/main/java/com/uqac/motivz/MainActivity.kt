package com.uqac.motivz

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.databinding.ActivityMainBinding
import com.uqac.motivz.ui.home.HomeFragment
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pseudo: String
    val database = Firebase.database.reference

    override fun onStop() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        var uid = user?.uid
        database.child("users").child(uid!!).child("dernière connexion").setValue(LocalDateTime.now().toString())
        super.onStop()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide();

        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_shop, R.id.navigation_home,  R.id.navigation_stats ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val nav = intent.getIntExtra("NAV", R.id.navigation_home)
        navView.selectedItemId = nav

        pseudo = intent.getStringExtra("PSEUDONYME").toString()



    }

    fun getPseudo(): String {
        return pseudo
    }
}