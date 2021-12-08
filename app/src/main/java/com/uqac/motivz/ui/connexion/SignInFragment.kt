package com.uqac.motivz.ui.connexion

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.MainActivity
import com.uqac.motivz.R
import com.uqac.motivz.ui.shop.AvatarShopFragment

class SignInFragment : Fragment(R.layout.fragment_sign_in) {




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {


        val auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        val btn = view.findViewById<Button>(R.id.btnSignIn)

        btn.setOnClickListener{
            val pseudo = view.findViewById<EditText>(R.id.enterPseudoSI).text.toString()
            val mail = view.findViewById<EditText>(R.id.enterEmailSI).text.toString()
            val password = view.findViewById<EditText>(R.id.enterPasswordSI).text.toString()

            auth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val database = Firebase.database
                    val myRef = database.getReference("users").child(auth.uid.toString()).child("pseudo")
                    myRef.setValue(pseudo)

                    //Add avatar Cloths
                    database.getReference("users").child(auth.uid.toString()).child("cloths").setValue(AvatarShopFragment.Cloths("-1","-1","-1","-1"))
                    //Add assiduity
                    database.getReference("users").child(auth.uid.toString()).child("attendance").setValue(0)



                    goToMainActivity(pseudo, R.id.navigation_home)
                }
            }.addOnFailureListener { exception ->
                //val context = context as MainActivity
                //Toast.makeText(context,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun goToMainActivity(pseudo: String, fragmentSelected : Int ){
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra("PSEUDONYME", pseudo);
        intent.putExtra("NAV", fragmentSelected);
        startActivity(intent)
    }
}