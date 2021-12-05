package com.uqac.motivz.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }


    var cache = false
    var goalNameList = ArrayList<String>()
    var goalProgressList =  ArrayList<Int>()
    val text: LiveData<String> = _text
}