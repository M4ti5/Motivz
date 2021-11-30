package com.uqac.motivz.ui.home

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Bundle
import com.uqac.motivz.R

class CreateGoalFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_create_goal, container, false)

//        // Create an ArrayAdapter
//        val adapter = ArrayAdapter.createFromResource(this, R.array.goal_type_list, android.R.layout.simple_spinner_item)
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        // Apply the adapter to the spinner
//        spinner.adapter = adapter

        // Inflate the layout for this fragment
        return view

    }

//    fun getValues(view: View) {
//        Toast.makeText(this,"" + spinner2.selectedItem.toString(), Toast.LENGTH_LONG).show()
//    }

}