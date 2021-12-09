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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.uqac.motivz.R
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import kotlin.random.Random
import android.os.Build
import androidx.fragment.app.FragmentTransaction
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

import androidx.lifecycle.LifecycleOwner

import androidx.annotation.NonNull





class HistogramFragment() : Fragment() {
    private var max_x_value = 7
    private var DAYS = arrayOf("LUN", "MAR", "MER", "JEU", "VEN", "SAM","DIM")
    private var SET_LABEL = "Pas"
    private lateinit var statButtonController : Button
    private val dataModel: DataViewModel by activityViewModels()
    lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private lateinit var uid : String
    private var goals = HashMap<String,ArrayList<Goal>>()
    var dontCreate = false
    lateinit var data : ArrayList<Int>
    var hasObserved = false
    lateinit var goalObserver: Observer<HashMap<String,ArrayList<Goal>>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun initArray(n : Int) : ArrayList<Int>{
        var list = ArrayList<Int>()
       for(i in 1..n){
           list.add(0)
       }
        return list
    }

    /*
    * N'est pas reliée à la base de données
    * Génère une liste qui contient le nombre d'objectifs terminés par jour, par semaine ou par mois
    * time : pour préciser si on veut les données par jour, par semaines ou par années
    * dataGoals : une hashmap, chaque type d'objectif(clé) est associé à plusieurs objectifs(valeur)
    * */
    fun generateGoalData(time : String, dataGoals: HashMap<String,ArrayList<Goal>>) : ArrayList<Int>{
        var currentDate = LocalDateTime.now()
        var firstDayOfWeek = currentDate.dayOfMonth.toString().toInt() -
                dayOfWeekToInt(currentDate.dayOfWeek.toString()) + 1
        var listData = initArray(max_x_value)
        val key = SET_LABEL
        for(goal in dataGoals!![key]!!){
            var date = goal.date.split("/")
            if(time == "week"){
                    listData[Math.abs(- firstDayOfWeek + date[0].toInt())]++
            } else if(time == "month") {
                var weekGoal = date[0].toInt()
                if(weekGoal > 0 && weekGoal <= 8){
                    listData[0]++
                } else if(weekGoal > 8 && weekGoal <= 15){
                    listData[1]++
                } else if(weekGoal > 15 && weekGoal <= 22){
                    listData[2]++
                } else {
                    listData[3]++
                }
            } else {
                listData[date[1].toInt() - 1]++
            }
        }
        return listData
    }

    fun printList(list : ArrayList<Int>){
        var s = ""
        for (i in 0..list.count() - 1){
            s = s + list[i] + "; "
        }
    }

    fun initDatabase(){
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            user = auth.currentUser!!
            uid = user?.uid
        }
    }


    private fun dayOfWeekToInt(day : String) : Int{
        if(day =="MONDAY"){
            return 1;
        } else if (day == "TUESDAY"){
            return 2
        } else if (day == "WEDNESDAY"){
            return 3
        } else if (day == "THURSDAY"){
            return 4
        } else if (day == "FRIDAY"){
            return 5
        } else if (day == "SATURDAY"){
            return 6
        } else if (day == "SUNDAY") {
            return 7
        }
        return -1;
    }

    private fun prepareChartData(data: BarData, chart: BarChart) {
        data.setValueTextSize(10f)
        chart.data = data
        chart.invalidate()
    }

    private fun createChartData(dataGoals : HashMap<String,ArrayList<Goal>>): BarData? {
        val values: ArrayList<BarEntry> = ArrayList()
        data = generateGoalData(sizeXaxisToTime(max_x_value),dataGoals)
        for (i in 0 until max_x_value) {
            val x = i.toFloat()
            val y = data[i]
            values.add(BarEntry(x, y.toFloat()))
        }
        val set1 = BarDataSet(values, SET_LABEL)
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)
        return BarData(dataSets)
    }

    private fun createInitChartData(): BarData? {
        val values: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until max_x_value) {
            val x = i.toFloat()
            val y = 0
            values.add(BarEntry(x, y.toFloat()))
        }
        val set1 = BarDataSet(values, SET_LABEL)
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)
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

    fun updateGoalHistogram(view: View, dataGoals : HashMap<String,ArrayList<Goal>>){
        dataModel.goal.observe(viewLifecycleOwner, Observer<String>{
                goal->
            SET_LABEL = goal
            var chart = view.findViewById<BarChart>(R.id.fragment_verticalbarchart_chart)
            var data = createInitChartData()
            if(dataGoals[goal] != null){
                data = createChartData(dataGoals)
            }
            prepareChartData(data!!,chart)
            configureChartAppearance(chart)

        })

    }


    fun sizeXaxisToTime(n:Int) : String{
        if(n == 7){
            return "week"
        } else if(n == 4) {
            return "month"
        } else {
            return "year"
        }
    }

    fun sizeXaxisToInt(n:Int) : Int{
        if(n == 7){
            return 0
        } else if(n == 4) {
            return 1
        } else {
            return 2
        }
    }

    fun updateAxisHistogram(view:View, dataGoals: HashMap<String,ArrayList<Goal>>){
            var observer = Observer<Array<String>>{
                stat ->
                max_x_value = stat.size
                DAYS = stat
                var chart = view.findViewById<BarChart>(R.id.fragment_verticalbarchart_chart)
                var data = createInitChartData()
                if(dataGoals[SET_LABEL] != null){
                    data = createChartData(dataGoals)
                }
                prepareChartData(data!!,chart)
                configureChartAppearance(chart)
                //var refreshButtonView = view.findViewById<Button>(R.id.stat)
            }
        dataModel.axis.observe(viewLifecycleOwner,observer)


    }

    override fun onDestroy() {
        Log.v("destroyed","onDestroy")
        super.onDestroy()
    }

    override fun onStop() {
        Log.v("observers?",dataModel.goals.hasObservers().toString())
        //requireFragmentManager().beginTransaction().detach(this)
        super.onStop()
    }

    override fun onDetach() {
        Log.v("detached","onDetach")
        super.onDetach()
    }

    override fun onDestroyView() {
        Log.v("destroy view","onDestroyview")
        super.onDestroyView()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initDatabase()

        var view = inflater.inflate(R.layout.fragment_histogram, container, false)
        return view
    }

    override fun onStart() {

        if(!hasObserved){
            goalObserver = Observer<HashMap<String,ArrayList<Goal>>>{
                    goal ->
                if(dataModel.goals.value != null){
                    updateGoalHistogram(requireView(),dataModel.goals.value!!)
                    updateAxisHistogram(requireView(),dataModel.goals.value!!)
                }
            }
            dataModel.goals.observe(viewLifecycleOwner,goalObserver)
            hasObserved = true
        }

        if(dataModel.goals.value == null){
            var chart = requireView().findViewById<BarChart>(R.id.fragment_verticalbarchart_chart)
            var data = createInitChartData()
            prepareChartData(data!!,chart)
            configureChartAppearance(chart)
       } else {

        }

        super.onStart()
    }







}