package com.uqac.motivz.ui.stats

import android.graphics.drawable.Drawable
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel :ViewModel(){
    val axis = MutableLiveData<Array<String>>()
    val goal = MutableLiveData<String>()
    lateinit var statButtonController : Button
    var currentDrawable = 0
    var firstCreate = true
    val previousConnexion = MutableLiveData<Int>()
    val currentConnexion = MutableLiveData<Int>()

    fun setSavedButton(button : Button){
        this.statButtonController = button
    }

    fun setFirstTofalse(){
        this.firstCreate = false
    }

    fun setSavedDrawable(id: Int){
        this.currentDrawable = id
    }


    fun setAxis(time:String){
        if(time.equals("week")){
            this.axis.value = arrayOf("DIM", "LUN", "MAR", "MER", "JEU", "VEN", "SAM")
        } else if(time.equals("month")){
            this.axis.value = arrayOf("SEM 1","SEM 2","SEM 3","SEM 4")
        } else {
            this.axis.value = arrayOf("JAN", "FEV", "MAR", "AVR", "MAI",
                "JUIN", "JUI","AOU","SEPT","OCT","NOV","DEC")
        }
    }

    fun setGoalName(name:String){
        this.goal.value = name
    }

}