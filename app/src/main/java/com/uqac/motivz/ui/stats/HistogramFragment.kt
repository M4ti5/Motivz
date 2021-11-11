package com.uqac.motivz.ui.stats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.uqac.motivz.R
import kotlin.random.Random


class HistogramFragment() : Fragment() {
    private var max_x_value = 0
    private var max_y_value = 10
    private var min_y_value = 0
    private var DAYS = arrayOf("DIM", "LUN", "MAR", "MER", "JEU", "VEN", "SAM")
    //private var chart = BarChart(context)
    private var SET_LABEL = "Objectif a"
    private lateinit var statButtonController : Button
    private val dataModel: DataViewModel by activityViewModels()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun prepareChartData(data: BarData, chart: BarChart) {
        data.setValueTextSize(10f)
        chart.data = data
        chart.invalidate()
    }

    private fun createChartData(): BarData? {
        val values: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until max_x_value) {
            val x = i.toFloat()
            val y = Random.nextDouble(min_y_value.toDouble(),max_y_value.toDouble())
            Log.v("x",""+x)
            values.add(BarEntry(x, y.toFloat()))
        }
        val set1 = BarDataSet(values, SET_LABEL)
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)
        Log.v("length",""+values.size)

        return BarData(dataSets)
    }

    private fun configureChartAppearance(chart: BarChart) {
        chart.description.isEnabled = false
        chart.setDrawValueAboveBar(false)
        chart.getXAxis().setLabelCount(DAYS.size);
        chart.getXAxis().setValueFormatter(IndexAxisValueFormatter(DAYS));
        val axisLeft = chart.axisLeft
        axisLeft.granularity = 10f
        axisLeft.axisMinimum = 0f
        val axisRight = chart.axisRight
        axisRight.granularity = 10f
        axisRight.axisMinimum = 0f
        var l = chart.legend
        l.setTextSize(20f);
    }
    fun selectIdButton(name : String) : Int{
        if (name == "year"){
            return R.id.year
        } else if (name == "month"){
            return R.id.month
        } else {
            return R.id.week
        }
    }

    fun updateGoalHistogram(view: View){
        dataModel.goal.observe(viewLifecycleOwner, Observer<String>{
                goal->
            SET_LABEL = goal
            var chart = view.findViewById<BarChart>(R.id.fragment_verticalbarchart_chart)
            val data = createChartData()
            prepareChartData(data!!,chart)
            configureChartAppearance(chart)

        })

    }

    fun updateAxisHistogram(view:View){
        dataModel.axis.observe(viewLifecycleOwner, Observer<Array<String>>{
                stat ->

            max_x_value = stat.size
            DAYS = stat
            var chart = view.findViewById<BarChart>(R.id.fragment_verticalbarchart_chart)
            val data = createChartData()
            prepareChartData(data!!,chart)
            configureChartAppearance(chart)
        })

    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_histogram, container, false)

        updateGoalHistogram(view)
        updateAxisHistogram(view)

        return view
    }
}