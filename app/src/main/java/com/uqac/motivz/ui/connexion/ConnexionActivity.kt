package com.uqac.motivz.ui.connexion

import com.uqac.motivz.databinding.ActivityConnexionBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.uqac.motivz.R

class ConnexionActivity : AppCompatActivity() {

    lateinit var binding : ActivityConnexionBinding

    override fun onCreate (savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityConnexionBinding.inflate(layoutInflater)
        supportActionBar?.hide();
        setContentView(binding.root)
        replaceFragment(SignInFragment())

        binding.logInBtn.setOnClickListener(){
            replaceFragment(LogInFragment())
        }

        binding.signInBtn.setOnClickListener(){
            replaceFragment(SignInFragment())
        }
    }

//    override fun onStart() {
//        auth.signOut()
//        super.onStart()
//    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.connexionFragContainer, fragment)
        fragmentTransaction.commit()
    }


}