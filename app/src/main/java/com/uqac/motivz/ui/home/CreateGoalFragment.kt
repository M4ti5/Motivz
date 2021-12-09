package com.uqac.motivz.ui.home

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.MainActivity
import com.uqac.motivz.R
import java.time.LocalDateTime

class CreateGoalFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var uid:String


    private fun addToDatabase( goalCount: Int, name: String, type: String, maxValue: Int) {
        database.reference.child("users").child(uid).child("goals").child(goalCount.toString()).setValue(
            GoalManagementActivity().setGoal(name , type , "0", maxValue.toString(), LocalDateTime.now().dayOfMonth.toString()+"/"+LocalDateTime.now().monthValue.toString()+"/"+LocalDateTime.now().year.toString(), "null", false )
        )

        goBackToMainActivity()
    }


    private fun goBackToMainActivity(){
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_create_goal, container, false)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(false)

        database = Firebase.database
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            user = auth.currentUser!!
            uid = user.uid
        }

        // access spinner
        val spinner = view.findViewById(R.id.EnterGoalTypeCG) as Spinner
        val goalTypes = resources.getStringArray(R.array.goal_type_list)
        var selectedType = goalTypes.first() // as default

//        if (spinner != null) {
            val adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_dropdown_item, goalTypes )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    selectedType = goalTypes[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
//        }

        var goalCount = 0
        val ref = database.reference.child("users").child(uid).child("goals").get().addOnSuccessListener{
            goalCount = it.childrenCount.toInt()
        }


        view.findViewById<Button>(R.id.BtnSaveCG).setOnClickListener{
            val name = view.findViewById<EditText>(R.id.EnterNameCG).text.toString()

            val value = view.findViewById<EditText>(R.id.EnterValueCG).text.toString().toInt()

            //val textTest = view.findViewById<TextView>(R.id.TestCG)
            //textTest.text = "goalCount: $goalCount"

            addToDatabase( goalCount, name, selectedType, value)
        }

        // Inflate the layout for this fragment
        return view

    }

    override fun onStart() {
        super.onStart()

    }



}