package com.uqac.motivz.ui.connexion

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.uqac.motivz.MainActivity
import com.uqac.motivz.R

class SignInFragment : Fragment(R.layout.fragment_sign_in) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        val btn = view.findViewById<Button>(R.id.btnSignIn)

        btn.setOnClickListener{
            val pseudo = view.findViewById<EditText>(R.id.enterPseudoSI).getText().toString()
            goToMainActivity(pseudo, R.id.navigation_home)
        }

        // Inflate the layout for this fragment
        return view

    }

    private fun goToMainActivity(pseudo: String, fragmentSelected : Int ){
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra("PSEUDONYME", pseudo);
        intent.putExtra("NAV", fragmentSelected);
        startActivity(intent)
    }
}