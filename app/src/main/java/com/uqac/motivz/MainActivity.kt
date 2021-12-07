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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.databinding.ActivityMainBinding
import com.uqac.motivz.ui.home.HomeFragment
import com.uqac.motivz.ui.stats.DataViewModel
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pseudo: String
    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private lateinit var uid : String

    fun resetAttendance(){
        database
            .child("users")
            .child(uid!!)
            .child("assiduité")
            .setValue(0)
    }

    fun updateAttendance(data : DataSnapshot){
        val currentDay = LocalDateTime.now().dayOfYear
        val currentYear = LocalDateTime.now().year
        val lastConnexion = data.child("dernière connexion").getValue().toString().split("/")
        val lastConnexionDay = lastConnexion[0].toInt()
        val lastConnexionYear = lastConnexion[1].toInt()
        var attendance = data.child("assiduité").getValue().toString().toInt()
        if(lastConnexionYear == currentYear){
            if(currentDay - lastConnexionDay == 1){
                attendance = attendance + 1
                database
                    .child("users")
                    .child(uid!!)
                    .child("assiduité")
                    .setValue(attendance.toString())
            } else {
                resetAttendance()
            }
        } else {
            resetAttendance()
        }

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

    fun initDatabase(){
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            user = auth.currentUser!!
            uid = user?.uid
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDatabase()


        if(auth.currentUser != null){
            val attendanceRef = database.child("users")
                .addListenerForSingleValueEvent( object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (data in snapshot.getChildren()) {
                            if(data.key.toString() == uid){
                                if (data.hasChild("assiduité")) {
                                    updateAttendance(data)
                                    /* val lastConnexion = data.child("dernière connexion").getValue().toString().split("/")
                                     val lastConnexionDay = lastConnexion[0].toInt()
                                     val lastConnexionYear = lastConnexion[1].toInt()
                                     var attendance = data.child("assiduité").getValue().toString().toInt()
                                     if(lastConnexionYear == currentYear){
                                         if(currentDay - lastConnexionDay == 1){
                                             attendance = attendance + 1
                                             database
                                                 .child("users")
                                                 .child(uid!!)
                                                 .child("assiduité")
                                                 .setValue(attendance.toString())
                                         }
                                     }*/
                                } else {

                                    database.child("users").child(uid!!).child("assiduité").setValue("0")
                                    //do something if not exists
                                }

                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }

                )


        }


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

        val auth= FirebaseAuth.getInstance()

        val database = Firebase.database

        val myRef = database.getReference("users").child(auth.uid.toString()).child("pseudo")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                val value = dataSnapshot.getValue<String>()
                pseudo = dataSnapshot.getValue<String>().toString()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })



    }



    fun getPseudo(): String {
        return pseudo
    }
}