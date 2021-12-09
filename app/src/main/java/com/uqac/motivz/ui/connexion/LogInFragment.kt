package com.uqac.motivz.ui.connexion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.uqac.motivz.MainActivity
import com.uqac.motivz.R




class LogInFragment : Fragment()  {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val auth= FirebaseAuth.getInstance()
        val view = inflater.inflate(R.layout.fragment_log_in, container, false)
        val btn = view.findViewById<Button>(R.id.btnLogIn)

        btn.setOnClickListener{

            val mail = view.findViewById<EditText>(R.id.enterEmailLI).text.toString()
            val password = view.findViewById<EditText>(R.id.enterPasswordLI).text.toString()
            if(mail!= "" && password !=""){

                auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        goToMainActivity(R.id.navigation_home)
                    }else{
                        Toast.makeText(context, "Mauvais Identifiant", Toast.LENGTH_LONG).show()
                    }
                }
            }


        }

        // Inflate the layout for this fragment
        return view

    }

    private fun goToMainActivity(fragmentSelected : Int){
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra("NAV", fragmentSelected);
        startActivity(intent)

    }

}