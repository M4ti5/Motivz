package com.uqac.motivz.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.databinding.FragmentItemListBinding

import android.widget.ListView
import com.uqac.motivz.Adapters.ItemListAdapter
import com.uqac.motivz.MainActivity
import com.uqac.motivz.Model.Item


class ItemShopFragment : Fragment() {

    val items = ArrayList<Item>()

    private lateinit var shopViewModel: ShopViewModel
    private var _binding: FragmentItemListBinding? = null

    private val binding get() = _binding!!
    private var listView : ListView? = null


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {

        shopViewModel =
            ViewModelProvider(this).get(ShopViewModel::class.java)

        _binding = FragmentItemListBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onStart()  {
        super.onStart()
        items.add(Item("couleur de cheveux", 300))
        items.add(Item("bague", 200))
        items.add(Item("chaussures", 100))
        items.add(Item("coupe de cheveux", 500))
        items.add(Item("pantalon", 150))

        listView = binding.itemList
        val context = context as MainActivity
        val adapter = ItemListAdapter(context, items)
        listView?.adapter = adapter


    }

    override fun onStop() {
        super.onStop()
        // Made save of avatar
    }

}