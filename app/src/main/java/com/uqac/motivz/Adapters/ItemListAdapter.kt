package com.uqac.motivz.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.uqac.motivz.Model.Item
import com.uqac.motivz.R

class ItemListAdapter(private val context : Activity, private val arrayList: ArrayList<Item>  ): ArrayAdapter<Item>(context, R.layout.list_item_shop, arrayList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {


        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item_shop, null)

        val itemName : TextView = view.findViewById(R.id.itemName)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)

        itemName.text = arrayList[position].name
        itemPrice.text = arrayList[position].price.toString()
        return view
    }

}