package com.uqac.motivz.ui.profil

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uqac.motivz.MainActivity
import com.uqac.motivz.R
import com.uqac.motivz.databinding.ActivityProfilBinding


class ProfilActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityProfilBinding
    private lateinit var pseudo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_profil)

        pseudo = intent.getStringExtra("PSEUDONYME").toString()

        binding.pseudoProfil.text = pseudo
        /*
        binding.goBackBtn.setOnClickListener(){
            goToMainActivity(R.id.navigation_home)
        }
        */

        binding.personnaliserBtn.setOnClickListener(){
            goToMainActivity(R.id.navigation_shop)
        }

    }

    private fun goToMainActivity(fragmentSelected : Int){
        val intent = Intent(this,  MainActivity::class.java)
        intent.putExtra("PSEUDONYME", pseudo);
        intent.putExtra("NAV", fragmentSelected);
        startActivity(intent)
    }
}