package com.uqac.motivz.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.R
import com.uqac.motivz.databinding.FragmentCompletedGoalDisplayBinding


class CompletedGoalDisplayFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentCompletedGoalDisplayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var uid: String

    private var displayLayout: LinearLayout? = null

    var completedGoalDisplayNameList = ArrayList<String>()
//    private val homeModel : HomeViewModel by activityViewModels()
//    var cache = false
//
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentCompletedGoalDisplayBinding.inflate(inflater, container, false)

        database = Firebase.database
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            user = auth.currentUser!!
            uid = user.uid
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        displayLayout = view?.findViewById(R.id.completedGoalDisplayLinearLayout)

        // Goals display
        getDataBaseCompletedGoals()

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

    private fun getDataBaseCompletedGoals() {
        database.reference.child("users").child(uid).child("goals").get().addOnSuccessListener {
            if (context != null) {
                for (goal in it.children) {
                    if (goal.child("_isFinished").value.toString().toBoolean() && (goal.key.toString() != "init") ) {
                        val goalName = goal.key.toString()
                        val displayName = goal.child("_name").value.toString()

                        completedGoalDisplayNameList.add(displayName)

                        addGoal(goalName, displayName)
                    }
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addGoal(goalName: String, goalDisplayName: String) {
        // RelativeLayout (button and progress bar) to add to goalLinearLayout
        val parent = RelativeLayout(binding.root.context)
        parent.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        parent.setPadding(15)

        // Create Goal Button
        val button = Button(binding.root.context)
        button.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250)
        button.setPaddingRelative(250, 5, 10, 5)
        button.gravity = Gravity.CENTER_VERTICAL
        button.text = goalDisplayName
        button.isClickable = false
        button.background = resources.getDrawable(R.drawable.goal_completed_button, binding.root.context.theme)
        //button.color
        // Add Goal Button to RelativeLayout
        parent.addView(button)

        // Add everything to the goalLinearLayout
        displayLayout?.addView(parent)
    }


//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

}