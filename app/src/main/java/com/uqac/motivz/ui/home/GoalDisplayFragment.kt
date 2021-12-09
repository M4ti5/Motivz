package com.uqac.motivz.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.R
import android.widget.*
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.databinding.FragmentGoalDisplayBinding


class GoalDisplayFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentGoalDisplayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var uid:String

    private var displayLayout : LinearLayout? = null

    var goalDisplayNameList = ArrayList<String>()
    var goalProgressList = ArrayList<Int>()
//    private val homeModel : HomeViewModel by activityViewModels()
//    var cache = false

//    override fun onStop() {
//        if(!homeModel.cache){
//            homeModel.goalNameList = goalNameList
//            homeModel.goalNameList = goalNameList
//            homeModel.goalProgressList = goalProgressList
//            homeModel.completedGoalNameList = goalNameList
//            homeModel.completedGoalDisplayNameList = goalNameList
//        }
//
//        homeModel.cache = true
//
//        super.onStop()
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentGoalDisplayBinding.inflate(inflater, container, false)

        database = Firebase.database
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            user = auth.currentUser!!
            uid = user.uid
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        displayLayout = view?.findViewById<LinearLayout>(R.id.goalDisplayLinearLayout)


        // Goals display
        getDataBaseGoals()

        /*if(!homeModel.cache && auth.currentUser!=null){
            getDataBasGoals()
        } else {
            goalNameList = homeModel.goalNameList
            goalProgressList = homeModel.goalProgressList
            goalDisplayNameList = homeModel.goalDisplayNameList

            // Temporary values

            val lastIndex = homeModel.goalNameList.size - 1
            for (i in 0..lastIndex) {
                addGoal(homeModel.goalNameList.get(i), homeModel.goalDisplayNameList.get(i), homeModel.goalProgressList.get(i))
            }
        }*/

    }

    private fun getDataBaseGoals() {
        database.reference.child("users").child(uid).child("goals").get().addOnSuccessListener{
            if (context != null) {
                for (goal in it.children) {
                    if(!goal.child("_isFinished").value.toString().toBoolean()){
                        val goalName = goal.key.toString()
                        val displayName = goal.child("_name").value.toString()
                        val progress = (goal.child("_stateValue").value.toString().toFloat() / goal.child("_maxValue").value.toString().toFloat() * 100).toInt()
                        Log.e("debug2" , progress.toString())
                        goalDisplayNameList.add(displayName)
                        goalProgressList.add(progress)

                        addGoal(goalName, displayName, progress)

                    }
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addGoal(goalName: String, goalDisplayName: String, progress: Int) {
        // RelativeLayout (button and progress bar) to add to goalLinearLayout
        val parent = RelativeLayout(binding.root.context)
        parent.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)
        parent.setPadding(15)

        // Create Goal Button
        val button = Button(binding.root.context)
        button.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250)
        button.setPaddingRelative(250, 0, 10, 0)
        button.gravity = Gravity.CENTER_VERTICAL
        button.text = goalDisplayName
        button.setBackgroundColor(Color.LTGRAY)
        button.background = resources.getDrawable(R.drawable.goal_button, binding.root.context.theme)
        // Add Goal Button to RelativeLayout
        parent.addView(button)

        // Create ProgressCircle
        val progressCircle = ProgressBar(binding.root.context, null, android.R.attr.progressBarStyleHorizontal)
        //progressCircle.layoutParams = LinearLayout.LayoutParams(150, 150).setMargins(10, 10, 10, 10)
        val layoutParams = LinearLayout.LayoutParams( 150, 150)
        layoutParams.setMargins(50, 50, 25, 25)
        //margin
        progressCircle.elevation = 10.0F
        progressCircle.background = resources.getDrawable(R.drawable.circular_shape, binding.root.context.theme)
        progressCircle.progressDrawable = resources.getDrawable(R.drawable.circular_progress_bar, binding.root.context.theme)
        progressCircle.isIndeterminate = false
        progressCircle.max = 100
        progressCircle.progress = progress
        // Add ProgressCircle to RelativeLayout
        parent.addView(progressCircle, layoutParams)

        // Add everything to the goalLinearLayout
        displayLayout?.addView(parent)


        // Set button on clickMethod
//        button.setOnClickListener {
//            showDialog(goalName, button, progressCircle, index)
//        }
    }


//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }


//    private fun showDialog(goalName: String, button: Button, progressCircle: ProgressBar, index: Int) {
//        val builder = AlertDialog.Builder(this.context)
//        with(builder) {
//            // setIcon(R.drawable.ic_hello)
//            // setTitle("Hello")
//            setMessage("Voulez vous valider cet objectif ?")
//            setPositiveButton("Valider") { _, _ ->
//                toast("clicked valider button")
//                goalValidation(goalName, button, progressCircle, index)
//            }
//            setNegativeButton("Annuler", null)
//        }
//        val alertDialog = builder.create()
//        alertDialog.show()
//
//        val buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//        with(buttonPositive) {
//            setBackgroundColor(Color.BLACK)
//            setPadding(20, 0, 20, 0)
//            setTextColor(Color.WHITE)
//        }
//        val buttonNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
//        with(buttonNegative) {
//            setPadding(20, 0, 40, 0)
//            setTextColor(Color.BLACK)
//        }
//    }
//
//    private fun goalValidation(goalName: String, button: Button, progressCircle: ProgressBar, index: Int) {
//        // update database
//        goalRef.child(goalName).child("pourcentage").setValue(100)
//        // update cache
//        if (goalProgressList.size > 0) {
//            goalProgressList.set(index, 100)
//        } else if (homeViewModel.goalProgressList.size > 0) {
//            homeViewModel.goalProgressList.set(index, 100)
//        }
//        // update progress on circle
//        progressCircle.progress = 100
//        // disable button click
//        //button.isEnabled = false
//        button.isClickable = false
//    }
//
//    private fun toast(text: String) = Toast.makeText(this.context, text, Toast.LENGTH_SHORT).show()

}