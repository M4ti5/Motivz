package com.uqac.motivz.ui.shop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AvatarShopViewModel : ViewModel(){

    private val _text = MutableLiveData<String>().apply {
        value = "This is Shop Fragment"
    }
    val text: LiveData<String> = _text
}