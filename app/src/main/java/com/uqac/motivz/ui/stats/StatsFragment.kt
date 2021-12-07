package com.uqac.motivz.ui.stats

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import com.uqac.motivz.R
import com.uqac.motivz.databinding.FragmentStatsBinding
import java.time.LocalDateTime


class StatsFragment : Fragment() {

    private lateinit var statsFragmentViewModel: StatsViewModel
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var statButtonController : Button
    private var currentDrawable = 0
    private val statModel : StatsViewModel by activityViewModels()
    private val dataModel : DataViewModel by activityViewModels()
    lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private lateinit var uid : String





    fun selectIdButton(name : String) : Int{
        if (name == "year"){
            return R.id.year
        } else if (name == "month"){
            return R.id.month
        } else {
            return R.id.week
        }
    }
    fun initDataBase(){
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            user = auth.currentUser!!
            uid = user?.uid
        }
    }


    /*get the background of a button when pressed or not pressed
    * button : the button whose background we want to find when pressed or not pressed
    * pressed : specifies what kind of background we want (pressed or not pressed)
    * returns : the drawable we want to set as a background
    * */
    fun getDrawableButton(button: Button, pressed: Boolean): Int {
        if (button.id.equals(R.id.week)) {
            return if (pressed) R.drawable.right_pressed_button
            else R.drawable.right_rounded_button;
        } else if (button.id.equals(R.id.month)) {
            return if (pressed) R.drawable.normal_pressed_button
            else R.drawable.normal_button;
        }
        return if (pressed) R.drawable.left_pressed_button
        else R.drawable.left_rounded_button;
    }



    fun updateHistogram(button: Button){
        if (button.id.equals(R.id.week)) {
            dataModel.setAxis("week")
        } else if (button.id.equals(R.id.month)) {
            dataModel.setAxis("month")
        } else {
            dataModel.setAxis("year")
        }
        val newHistogramFragment = HistogramFragment()
        val fragmentTransaction = requireFragmentManager().beginTransaction()
        fragmentTransaction.replace(R.id.fragment_chart, newHistogramFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }





    fun selectTime(view: View, id: Int) {
        Log.v("selectTime", "call")
        var time = id
        val layout = view.findViewById<Button>(id)
        var button = layout.findViewById<Button>(time)
        updateHistogram(button)
        statButtonController.setBackgroundDrawable(
            getResources()
                .getDrawable(currentDrawable)
        )

        button.setBackgroundDrawable(
            getResources().getDrawable(getDrawableButton(button, true))
        );

        statButtonController = button
        currentDrawable = getDrawableButton(button, false)

    }

    fun addButtonListeners(layout: View){
        val buttons = listOf("year","month","week")
        for(i in (0..buttons.size - 1)){
            var button = layout.findViewById<Button>(selectIdButton(buttons.get(i)))
            button.setOnClickListener{
                selectTime(layout, selectIdButton(buttons.get(i)))
            }
        }
    }

    override fun onStop() {
        dataModel.setSavedButton(statButtonController)
        dataModel.setSavedDrawable(currentDrawable)
        super.onStop()
    }

    //save the last clicked button
    override fun onDestroyView() {
        //dataModel.setSavedButton(statButtonController)
        //dataModel.setSavedDrawable(currentDrawable)
        super.onDestroyView()
    }

    fun useDefaultConfig(layout:View){
        statButtonController = layout.findViewById(R.id.week) as Button
        currentDrawable = R.drawable.right_rounded_button
        dataModel.setFirstTofalse()
        selectTime(statButtonController,statButtonController.id)
    }

    fun useSavedConfig(layout:View){
        currentDrawable = dataModel.currentDrawable
        statButtonController = layout.findViewById<Button>(dataModel.statButtonController.id)
        selectTime(statButtonController,statButtonController.id)
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
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initDataBase()
        if(auth.currentUser != null){
            val attendanceRef = database.child("users")
                .addListenerForSingleValueEvent( object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (data in snapshot.getChildren()) {
                            if(data.key.toString() == uid){
                                val textViewAttendance = view!!.findViewById<TextView>(R.id.attendance)
                                if (data.hasChild("assiduité")) {
                                    val attendance = data.child("assiduité").getValue().toString()
                                    if(attendance.toInt() < 10){
                                        textViewAttendance.setText("0" + attendance)
                                    } else {
                                        textViewAttendance.setText(attendance)
                                    }

                                } else {
                                    textViewAttendance.setText("0")
                                    //database.child("users").child(uid!!).child("assiduité").setValue("0")
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


        statsFragmentViewModel =
            ViewModelProvider(this).get(StatsViewModel::class.java)
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        var layout = binding.root
        if(dataModel.firstCreate){
           useDefaultConfig(layout)
        } else {
            useSavedConfig(layout)
        }
        addButtonListeners(layout)
        return layout
    }
}