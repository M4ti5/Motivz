package com.uqac.motivz.ui.profil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.uqac.motivz.R
import com.uqac.motivz.databinding.ActivityProfilBinding

class ProfilActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityProfilBinding
    private lateinit var pseudo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pseudo = intent.getStringExtra("PSEUDONYME").toString()


    }
}