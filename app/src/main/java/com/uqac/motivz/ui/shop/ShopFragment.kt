package com.uqac.motivz.ui.shop
import android.os.Bundle


import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.MainActivity
import com.uqac.motivz.R
import com.uqac.motivz.databinding.ActivityConnexionBinding

import com.uqac.motivz.databinding.FragmentShopBinding
import com.uqac.motivz.ui.connexion.LogInFragment
import com.uqac.motivz.ui.connexion.SignInFragment

class ShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!


    private lateinit var database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth

    private lateinit var user : FirebaseUser
    private lateinit var uid : String
    private lateinit var pseudo: String



    private fun insertNestedFragment() {
        val childFragment: Fragment = AvatarShopFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.connexionShopFrag, childFragment).commit()
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.connexionShopFrag, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        _binding = FragmentShopBinding.inflate(inflater, container, false)


        database = Firebase.database
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            user = auth.currentUser!!
            uid = user.uid
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insertNestedFragment()
    }

    override fun onStart() {
        super.onStart()

        ////Fragment Management
        replaceFragment(AvatarShopFragment())

        binding.avatarBtn.setOnClickListener(){
            replaceFragment(AvatarShopFragment())
        }

        binding.itemsBtn.setOnClickListener(){
            replaceFragment(ItemShopFragment())
        }

        ////Coin Management
        database.reference.child("users").child(uid).child("coins").get().addOnSuccessListener {
            view?.findViewById<TextView>(R.id.nb_coins)?.text = it.value.toString()
        }


    }


}