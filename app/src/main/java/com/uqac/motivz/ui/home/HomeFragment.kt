package com.uqac.motivz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.R
import com.uqac.motivz.databinding.FragmentHomeBinding
import android.app.ProgressDialog




class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        //homeViewModel.text.observe(viewLifecycleOwner, Observer {
          //  textView.text = it
        //})

        val goalTitle: TextView = binding.goalTitle
        goalTitle.text = getString(R.string.goal_title)

        val goalLinearLayout: LinearLayout = binding.goalLinearLayout

        val goalList = listOf("Objectif 1", "Objectif 2", "Objectif 3", "Objectif 4", "Objectif 5",
            "Objectif 6", "Objectif 7", "Objectif 8", "Objectif 9", "Objectif 10")
        for (goal in goalList) {
            var button = Button(this.context)
            button.text = goal
            button.height = 200
            goalLinearLayout.addView(button);
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}