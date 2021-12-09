package com.uqac.motivz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.R
import com.uqac.motivz.databinding.FragmentHomeBinding
import android.content.Intent
import android.widget.*
import androidx.fragment.app.FragmentTransaction
import com.uqac.motivz.ui.profil.ProfilActivity
import com.uqac.motivz.MainActivity


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insertNestedFragment()
    }
    override fun onStart() {
        super.onStart()

        // Access to profile from profileButton
        val profileButton: Button = binding.profileButton
        profileButton.setOnClickListener {
            val pseudo: String = (activity as MainActivity).getPseudo()
            goToProfilActivity(pseudo)
        }

        replaceFragment(GoalDisplayFragment())

        binding.goalListButton.setOnClickListener {
            replaceFragment(GoalDisplayFragment())
        }

        binding.completedGoalListButton.setOnClickListener {
            replaceFragment(CompletedGoalDisplayFragment())
        }

        // Access goal creation fragment from + button
        val createGoalButton : Button = binding.createGoalButton
        createGoalButton.setOnClickListener {
            goToCreateGoalActivity()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun goToProfilActivity(pseudo: String){
        val intent = Intent(activity, ProfilActivity::class.java)
        //intent.putExtra("PSEUDONYME", pseudo)
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