package com.uqac.motivz.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.databinding.FragmentAvatarShopBinding

class AvatarShopFragment : Fragment() {

    private lateinit var avatarShopViewModel: AvatarShopViewModel
    private var _binding: FragmentAvatarShopBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        avatarShopViewModel =
            ViewModelProvider(this).get(AvatarShopViewModel::class.java)

        _binding = FragmentAvatarShopBinding.inflate(inflater, container, false)

        return binding.root
    }
}