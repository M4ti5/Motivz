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
    var goalDisplayNameList = ArrayList<String>()
    var goalProgressList =  ArrayList<Int>()
    var completedGoalNameList = ArrayList<String>()
    var completedGoalDisplayNameList = ArrayList<String>()
    val text: LiveData<String> = _text
}