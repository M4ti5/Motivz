package com.uqac.motivz.ui.shop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.R
import com.uqac.motivz.databinding.FragmentAvatarShopBinding

import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.MainActivity


class AvatarShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel
    private var _binding: FragmentAvatarShopBinding? = null

    private val binding get() = _binding!!

    private var avatarBundle : RelativeLayout? = null

    private lateinit var database: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var uid:String


    // Layer Avatar Cloth
    private val listOfClothArea = mapOf("skin" to 0, "hair" to 1 , "top" to 2, "bottom" to 3, "shoes" to 4, "pet" to 5)
    private var currentCloths:Cloths = setCloths("-1","-1","-1","-1")
    private val allCloths = arrayOf(R.drawable.ic_clothe_1, R.drawable.shirt_blue)

    data class Cloths(val _skin:String? = null, val _top:String? = null, val _bottom:String? = null, val _shoes:String? = null)
    enum class AreaCloth(val area: String){skin("skin"),top("top"), bottom("bottom"), shoes("shoes")}

    fun setCloths(skin:String, top:String, bottom:String, shoes:String ): Cloths {
        return Cloths(skin,top,bottom,shoes)
    }

    fun setCloths(cloths: Cloths, area:String, value:String ): Cloths {
        when (area) {
            "skin"-> return Cloths(value,cloths._top,cloths._bottom,cloths._shoes)
            "top" -> return Cloths(cloths._skin,value,cloths._bottom,cloths._shoes)
            "bottom" -> return Cloths(cloths._skin,cloths._top,value,cloths._shoes)
            "shoes" -> return Cloths(cloths._skin,cloths._top,cloths._bottom,value)
        }
        return cloths
    }

    fun setDataBaseCloths(){
        database.reference.child("users").child(uid).child("cloths").setValue(currentCloths)
    }

    fun getCloths(cloths: Cloths, cloth:String):String{
        when (cloth) {
            "skin"-> return cloths._skin.toString()
            "top" -> return cloths._top.toString()
            "bottom" -> return cloths._bottom.toString()
            "shoes" -> return cloths._shoes.toString()
        }
        return "null"

    }

    fun getDataBaseCloths() {
        database.reference.child("users").child(uid).child("cloths").get().addOnSuccessListener{
            currentCloths = setCloths(
                it.child("_skin").value.toString(),
                it.child("_top").value.toString(),
                it.child("_bottom").value.toString(),
                it.child("_shoes").value.toString()
            )
        }
    }

    fun toChangeCloth(clothArea:String , clothIndex:Int ){

        var tempIndex:Int = clothIndex
        val id : Int = avatarBundle?.getChildAt(listOfClothArea.getValue(clothArea))?.id!!
        val imageView: ImageView? = view?.findViewById(id)

        if(getCloths(currentCloths,clothArea).toInt() != clothIndex ){
            imageView?.setImageResource(allCloths[tempIndex])
        }else{
            tempIndex = -1
            imageView?.setImageResource(0)
        }

        currentCloths = setCloths(currentCloths ,clothArea, tempIndex.toString())
    }

    fun printCurrentCloths (){
        enumValues<AreaCloth>().forEach {
            val clothIndex:Int = getCloths(currentCloths, it.area).toInt()
            if(clothIndex != -1) {
                val id: Int = avatarBundle?.getChildAt(listOfClothArea.getValue(it.area))?.id!!
                val imageView: ImageView? = view?.findViewById(id)
                imageView?.setImageResource(allCloths[clothIndex])
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {

        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        _binding = FragmentAvatarShopBinding.inflate(inflater, container, false)


        database = Firebase.database
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

        val button = view?.findViewById<RelativeLayout>(R.id.buttonObject1)
        button?.setOnClickListener {
            toChangeCloth("bottom", 0)
        }


        val button2 = view?.findViewById<RelativeLayout>(R.id.buttonObject2)
        button2?.setOnClickListener {
            toChangeCloth("top",1)
        }

        getDataBaseCloths()
    }

    override fun onResume() {
        super.onResume()
        printCurrentCloths()
    }

    override fun onPause() {
        super.onPause()
        setDataBaseCloths()
    }

}