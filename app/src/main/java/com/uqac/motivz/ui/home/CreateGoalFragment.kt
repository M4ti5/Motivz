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

//        val spinner = view.findViewById<Spinner>(R.id.spinner1)
//        val goalNameList = listOf("Nombre de pas", "Distance parcourue", "Nombre de pompes", "Nombres de squats", "Nombre d'abdos")


        // Inflate the layout for this fragment
        return view

    }

}