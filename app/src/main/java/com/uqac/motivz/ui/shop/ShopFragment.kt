package com.uqac.motivz.ui.shop
import android.os.Bundle


import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.R
import com.uqac.motivz.databinding.ActivityConnexionBinding

import com.uqac.motivz.databinding.FragmentShopBinding
import com.uqac.motivz.ui.connexion.LogInFragment
import com.uqac.motivz.ui.connexion.SignInFragment

class ShopFragment : Fragment() {

    private lateinit var shopViewModel: ShopViewModel
    private var _binding: FragmentShopBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insertNestedFragment()
    }

    override fun onStart() {
        super.onStart()
        replaceFragment(AvatarShopFragment())

        binding.avatarBtn.setOnClickListener(){
            replaceFragment(AvatarShopFragment())
        }

        binding.itemsBtn.setOnClickListener(){
            replaceFragment(ItemShopFragment())
        }
    }
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
}