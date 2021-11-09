package com.uqac.motivz.ui.profil

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uqac.motivz.MainActivity
import com.uqac.motivz.databinding.ActivityProfilBinding


class ProfilActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityProfilBinding
    private lateinit var pseudo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pseudo = intent.getStringExtra("PSEUDONYME").toString()

        binding.pseudoProfil.text = pseudo

        binding.goBackBtn.setOnClickListener(){
            goToMainActivity()
        }

        binding.personnaliserBtn.setOnClickListener(){
            goToMainActivity()
        }

    }

    private fun goToMainActivity(){
        val intent = Intent(this,  MainActivity::class.java)
        intent.putExtra("PSEUDONYME", pseudo);
        startActivity(intent)
    }
}