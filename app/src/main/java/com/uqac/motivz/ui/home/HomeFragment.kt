package com.uqac.motivz.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.R
import com.uqac.motivz.databinding.FragmentHomeBinding
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.core.view.*
import android.widget.LinearLayout
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.ui.profil.ProfilActivity
import com.uqac.motivz.MainActivity
import com.uqac.motivz.ui.shop.AvatarShopFragment


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    var goalNameList = ArrayList<String>()
    var goalProgressList = ArrayList<Int>()

    private lateinit var database:FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var uid:String

    private val homeModel : HomeViewModel by activityViewModels()

    override fun onStop() {
        if(!homeModel.cache){
            homeModel.goalNameList = goalNameList
            homeModel.goalNameList = goalNameList
            homeModel.goalProgressList = goalProgressList
            homeModel.completedGoalNameList = goalNameList
            homeModel.completedGoalDisplayNameList = goalNameList
        }

        homeModel.cache = true

        super.onStop()
    }


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database
        var auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null) {
            user = auth.currentUser!!
            uid = user.uid
        }
        insertNestedFragment()


    }
    override fun onStart() {
        super.onStart()
        replaceFragment(GoalDisplayFragment())

        binding.goalListButton.setOnClickListener() {
            replaceFragment(GoalDisplayFragment())
        }

        binding.completedGoalListButton.setOnClickListener() {
            replaceFragment(CompletedGoalDisplayFragment())
        }

        // Access to profile from profileButton
        binding.profileButton.setOnClickListener {
            goToProfilActivity()
        }

        // Access goal creation fragment from + button
        binding.createGoalButton.setOnClickListener {
            goToCreateGoalActivity()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun goToProfilActivity(){
        val intent = Intent(activity, ProfilActivity::class.java)
        intent.putExtra("UID", uid)
        startActivity(intent)
    }

    private fun goToCreateGoalActivity(){
        val intent = Intent(activity, GoalManagementActivity::class.java)
        intent.putExtra("ACTION", "create")
        startActivity(intent)
    }

    private fun insertNestedFragment() {
        val childFragment: Fragment = GoalDisplayFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.homeFragmentContainer, childFragment).commit()
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.homeFragmentContainer, fragment)
        fragmentTransaction.commit()
    }

}