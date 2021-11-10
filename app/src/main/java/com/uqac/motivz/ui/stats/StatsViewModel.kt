package com.uqac.motivz.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.uqac.motivz.R
import kotlin.random.Random
import com.github.mikephil.charting.components.YAxis
import android.content.Context
import android.widget.Button
import android.widget.LinearLayout

import com.github.mikephil.charting.formatter.ValueFormatter

import com.github.mikephil.charting.components.XAxis

class StatsViewModel : ViewModel(){

     val x_axis= MutableLiveData<Array<String>>()
    //private var x_axis = arrayOf("DIM", "LUN", "MAR", "MER", "JEU", "VEN", "SAM")

    fun setAxis(time : String){
        if(time.equals("week")){
            this.x_axis.value = arrayOf("DIM", "LUN", "MAR", "MER", "JEU", "VEN", "SAM")
        } else if(time.equals("month")){
            this.x_axis.value = arrayOf("SEM 1","SEM 2","SEM 3","SEM 4")
        } else {
            this.x_axis.value = arrayOf("JAN", "FEV", "MAR", "AVR", "MAI",
                "JUIN", "JUI","AOU","SEPT","OCT","NOV","DEC")
        }
    }



}
