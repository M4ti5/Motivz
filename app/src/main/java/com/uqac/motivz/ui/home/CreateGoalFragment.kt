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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.MainActivity
import com.uqac.motivz.R

class CreateGoalFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_create_goal, container, false)
        val auth = FirebaseAuth.getInstance()
        val database = Firebase.database.reference
        val userId = auth.uid.toString()

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

        var goalCount = -1
        val ref = database.child("users").child(userId).child("goalCount")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                goalCount = dataSnapshot.getValue<Int>()?.toInt() ?: -1
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

        val saveBtn = view.findViewById<Button>(R.id.BtnSaveCG)
        saveBtn.setOnClickListener{
            val name = view.findViewById<EditText>(R.id.EnterNameCG).text.toString()

            val value = view.findViewById<EditText>(R.id.EnterValueCG).text.toString().toInt()

            val textTest = view.findViewById<TextView>(R.id.TestCG)
            textTest.text = "goalCount: $goalCount"

            addToDatabase(database, userId, goalCount, name, selectedType, value)

        }

        // Inflate the layout for this fragment
        return view

    }

    private fun addToDatabase(database: DatabaseReference, userId: String, goalCount: Int,
                              name: String, type: String, value: Int) {
        // No changes if goalCount wasn't retrieved
        if (goalCount == -1) {
            // TODO : error feedback, goal couldn't be created
            return
        }

        val userPath = database.child("users").child(userId)
        // Create goal in database
        // TODO : get last "objectif" id and create the next (if "objectif 2", create "objectif 3")
        val goalId = goalCount + 1
        val goalPath = userPath.child("objectifs").child("objectif $goalId")
        goalPath.child("name").setValue(name)
        goalPath.child("type").setValue(type)
        goalPath.child("value").setValue(value)
        goalPath.child("progression").setValue(0)

        // Update user's goalCount
        userPath.child("goalCount").setValue(goalId)

        goBackToMainActivity()
    }


    // TODO : peut-être possible de juste trigger le retour à l'activity parent, comme avec la flèche retour
    private fun goBackToMainActivity(){
        val intent = Intent(activity, MainActivity::class.java)
        //intent.putExtra("NAV", pseudo)
        startActivity(intent)
    }

}