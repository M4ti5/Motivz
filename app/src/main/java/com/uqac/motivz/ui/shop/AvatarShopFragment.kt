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


class AvatarShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel
    private var _binding: FragmentAvatarShopBinding? = null

    private val binding get() = _binding!!

    private var avatarBundle : RelativeLayout? = null

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        shopViewModel =
            ViewModelProvider(this).get(ShopViewModel::class.java)

        _binding = FragmentAvatarShopBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart()  {
        super.onStart()
        avatarBundle  = view?.findViewById<RelativeLayout>(R.id.AvatarBundle)


        val clothes  = arrayOf(R.drawable.ic_clothe_1 )

        val imageView: ImageView = view?.findViewById(R.id.ClotheTop)!!


        val button = view?.findViewById<RelativeLayout>(R.id.buttonObject1)
        button?.setOnClickListener {
            var id : Int = avatarBundle?.getChildAt(0)?.id!!
            imageView.setImageResource(clothes[0])
        }
    }

    override fun onStop() {
        super.onStop()
        // Made save of avatar
    }

}