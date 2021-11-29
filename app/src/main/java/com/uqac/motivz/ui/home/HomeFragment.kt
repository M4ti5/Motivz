package com.uqac.motivz.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.R
import com.uqac.motivz.databinding.FragmentHomeBinding
import android.app.ProgressDialog
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.view.*
import com.uqac.motivz.MainActivity
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.ui.profil.ProfilActivity
import com.uqac.motivz.ui.stats.DataViewModel


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val database = Firebase.database.reference
    private val auth = FirebaseAuth.getInstance()
    private val user = auth.currentUser
    private var uid = user?.uid
    private var goalRef = database.child("objectifs")
    private var goalUser = database.child("users").child(uid!!).child("objectifs")
    var goalNameList = ArrayList<String>()
    var goalProgressList = ArrayList<Int>()
    private val homeModel : HomeViewModel by activityViewModels()
    var cache = false


    override fun onStop() {
        if(!homeModel.cache){
            homeModel.goalNameList = goalNameList
            homeModel.goalProgressList = goalProgressList
        }

        homeModel.cache = true

        super.onStop()
    }

    fun getGoalsFromDatabase(goalLinearLayout: LinearLayout){
        goalUser.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (goal in snapshot.getChildren()) {
                    val goalName = goal.key.toString()
                    if(!goalNameList.contains(goalName)){
                        goalNameList.add(goalName)
                        goalRef.child(goalName).
                        child("pourcentage").addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val progress = snapshot.getValue().toString().toInt()
                                goalProgressList.add(progress)
                                addGoal(goalName, progress, goalLinearLayout)
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    } else {

                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Access to profile from profileButton
        val profileButton: Button = binding.profileButton
        profileButton.setOnClickListener {
            val pseudo: String = (activity as MainActivity).getPseudo()
            goToProfilActivity(pseudo)
        }
        
        val goalLinearLayout: LinearLayout = binding.goalLinearLayout

        if(!homeModel.cache){
            getGoalsFromDatabase(goalLinearLayout)

        } else {
            // Temporary values

            val lastIndex = homeModel.goalNameList.size - 1
            for (i in 0..lastIndex) {
                addGoal(homeModel.goalNameList.get(i), homeModel.goalProgressList.get(i), goalLinearLayout)
            }

        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun goToProfilActivity(pseudo: String){
        val intent = Intent(activity, ProfilActivity::class.java)
        intent.putExtra("PSEUDONYME", pseudo)
        startActivity(intent)
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addGoal(goalName: String, progress: Int, goalLinearLayout: LinearLayout) {
        // RelativeLayout (button and progress bar) to add to goalLinearLayout
        val parent = RelativeLayout(this.context)
        parent.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT)
        parent.setPadding(25)

        // Create Goal Button
        val button = Button(this.context)
        button.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250)
        button.setPaddingRelative(250, 10, 10, 10)
        button.gravity = Gravity.CENTER_VERTICAL
        button.text = goalName
        // Add Goal Button to RelativeLayout
        parent.addView(button)

        // Create ProgressCircle
        val progressCircle = ProgressBar(this.context, null, android.R.attr.progressBarStyleHorizontal)
        //progressCircle.layoutParams = LinearLayout.LayoutParams(150, 150).setMargins(10, 10, 10, 10)
        val layoutParams = LinearLayout.LayoutParams( 150, 150)
        layoutParams.setMargins(50, 50, 25, 25)
        //margin
        progressCircle.elevation = 10.0F
        progressCircle.background = resources.getDrawable(R.drawable.circular_shape, requireContext().theme)
        progressCircle.progressDrawable = resources.getDrawable(R.drawable.circular_progress_bar, requireContext().theme)
        progressCircle.isIndeterminate = false
        progressCircle.max = 100
        progressCircle.progress = progress
        // Add ProgressCircle to RelativeLayout
        parent.addView(progressCircle, layoutParams)

        // Add everything to the goalLinearLayout
        goalLinearLayout.addView(parent)
    }

}