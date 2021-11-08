package com.uqac.motivz.ui.connexion

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.uqac.motivz.MainActivity
import com.uqac.motivz.R




class LogInFragment : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_log_in, container, false)
        val btn = view.findViewById<Button>(R.id.btnLogIn)
        btn.setOnClickListener{
            goToMainActivity()
        }

        // Inflate the layout for this fragment
        return view

    }

    private fun goToMainActivity(){
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

}