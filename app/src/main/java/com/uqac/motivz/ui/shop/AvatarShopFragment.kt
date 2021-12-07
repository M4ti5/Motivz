package com.uqac.motivz.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.R
import com.uqac.motivz.databinding.FragmentAvatarShopBinding

import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class AvatarShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel
    private var _binding: FragmentAvatarShopBinding? = null

    private val binding get() = _binding!!

    private var avatarBundle : RelativeLayout? = null

    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var uid:String

    data class Cloths(val _skin:String? = null, val _top:String? = null, val _bottom:String? = null, val _shoes:String? = null)

    private fun setCloths(skin:String, top:String, bottom:String, shoes:String ){
        val cloths = Cloths(skin,top,bottom,shoes)
        database.reference.child("users").child(uid).child("cloths").setValue(cloths)
    }

    fun getCloths(uid:String){
        database.reference.child("users").child(uid).child("cloths").get()
    }


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        database = Firebase.database

        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)

        _binding = FragmentAvatarShopBinding.inflate(inflater, container, false)

        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            user = auth.currentUser!!
            uid = user.uid
        }

        return binding.root
    }


    override fun onStart()  {
        super.onStart()
        avatarBundle  = view?.findViewById<RelativeLayout>(R.id.AvatarBundle)


        val clothes  = arrayOf(R.drawable.ic_clothe_1, R.drawable.shirt_blue)
        val skins = arrayOf(R.drawable.shirt_red)


        setCloths("0","0","0","0")
        val imageView: ImageView = view?.findViewById(R.id.ClotheTop)!!

        val button = view?.findViewById<RelativeLayout>(R.id.buttonObject1)
        button?.setOnClickListener {
            var id : Int = avatarBundle?.getChildAt(0)?.id!!
            imageView.setImageResource(clothes[0])
        }


        val button2 = view?.findViewById<RelativeLayout>(R.id.buttonObject3)
        button2?.setOnClickListener {
            var id : Int = avatarBundle?.getChildAt(0)?.id!!
            imageView.setImageResource(clothes[1])
        }
    }

    override fun onStop() {
        super.onStop()
        // Made save of avatar
    }

}