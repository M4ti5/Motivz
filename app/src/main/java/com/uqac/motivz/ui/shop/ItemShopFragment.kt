package com.uqac.motivz.ui.shop

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.databinding.FragmentItemListBinding

import android.widget.ListView
import android.widget.Toast
import com.uqac.motivz.Adapters.ItemListAdapter
import com.uqac.motivz.MainActivity
import com.uqac.motivz.Model.Item
import com.uqac.motivz.R


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
        items.add(Item("Freeze", 300))
        items.add(Item("Double score", 200))

        listView = binding.itemList
        val context = context as MainActivity
        val adapter = ItemListAdapter(context, items)
        listView?.adapter = adapter

        listView!!.setOnItemClickListener { parent, view, pos, id ->
            askToBuyItem(parent, pos)
        }
    }

    override fun onStop() {
        super.onStop()
        // Made save of avatar
    }


    fun askToBuyItem(parent : AdapterView<*>, pos : Int){
        val selectedItem = parent.getItemAtPosition(pos) as Item

        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    R.string.yes,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK button
                        buyItem(selectedItem)
                    })
                setNegativeButton(R.string.no,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            }

            builder?.setMessage("Acheter " + selectedItem.name + " ?")

            // Create the AlertDialog
            builder.create()
        }

        alertDialog?.show()


    }

    fun buyItem(item : Item){
        Toast.makeText(context, item.name, Toast.LENGTH_SHORT).show()
    }

}