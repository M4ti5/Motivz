package com.uqac.motivz.ui.connexion

import android.app.Activity;
import com.uqac.motivz.databinding.ActivityConnexionBinding
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.uqac.motivz.R
import com.uqac.motivz.databinding.FragmentAvatarShopBinding

class ConnexionActivity : AppCompatActivity() {

    lateinit var binding : ActivityConnexionBinding

    override fun onCreate (savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityConnexionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(SignInFragment())

        binding.logInBtn.setOnClickListener(){
            replaceFragment(LogInFragment())
        }

        binding.signInBtn.setOnClickListener(){
            replaceFragment(SignInFragment())
        }
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.connexionFragContainer, fragment)
        fragmentTransaction.commit()
    }


}