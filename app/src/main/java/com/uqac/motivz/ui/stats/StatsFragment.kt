package com.uqac.motivz.ui.stats

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import com.uqac.motivz.R
import com.uqac.motivz.databinding.FragmentStatsBinding
import java.time.LocalDateTime
import com.uqac.motivz.ui.stats.Goal
import com.uqac.motivz.ui.stats.StatsFragment.OnGetDataListener
import android.os.Build
import com.google.android.material.bottomnavigation.BottomNavigationView


class StatsFragment : Fragment() {
    /*
    * Fonctions qui lisent dans la base de données :
    * getFinishedGoals : HashMap<String,ArrayList<Goal>>
    * readAndDisplayAttendance : Unit
    * */

    private lateinit var statsFragmentViewModel: StatsViewModel
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var statButtonController : Button
    private var currentDrawable = 0
    private val statModel : StatsViewModel by activityViewModels()
    private val dataModel : DataViewModel by activityViewModels()
    lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var user : FirebaseUser
    private lateinit var uid : String
    private var goals = HashMap<String,ArrayList<Goal>>()


    interface OnGetDataListener {
        fun onStart()
        fun onSuccess(data: HashMap<String,ArrayList<Goal>>)
        fun onFailed(databaseError: DatabaseError?)
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
    fun initDataBase(){
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            user = auth.currentUser!!
            uid = user?.uid
        }
    }


    /*get the background of a button when pressed or not pressed
    * button : the button whose background we want to find when pressed or not pressed
    * pressed : specifies what kind of background we want (pressed or not pressed)
    * returns : the drawable we want to set as a background
    * */
    fun getDrawableButton(button: Button, pressed: Boolean): Int {
        if (button.id.equals(R.id.week)) {
            return if (pressed) R.drawable.right_pressed_button
            else R.drawable.right_rounded_button;
        } else if (button.id.equals(R.id.month)) {
            return if (pressed) R.drawable.normal_pressed_button
            else R.drawable.normal_button;
        }
        return if (pressed) R.drawable.left_pressed_button
        else R.drawable.left_rounded_button;
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


    private fun validGoal(timeId : Int, dateGoal : List<String>):Boolean{
        var currentDate = LocalDateTime.now()
        var firstDayOfWeek = currentDate.dayOfMonth.toString().toInt() -
                dayOfWeekToInt(currentDate.dayOfWeek.toString()) + 1
        var currentMonth = currentDate.monthValue.toString().toInt()
        var currentYear = currentDate.year.toInt()
        val sameWeek = dateGoal[0].toInt() - firstDayOfWeek >= 0
        val sameMonth = dateGoal[1].toInt() == currentMonth
        val sameYear = dateGoal[2].toInt() == currentYear
        if(timeId == 0){
            return sameWeek && sameMonth && sameYear
        } else if(timeId == 1){
            return sameMonth && sameYear
        }
        return sameYear
    }

    /*Fonction qui execute getFinishedGoals (récupère les données)
    * et enregistre les données dans le viewModel dès que la lecture est terminée
    * period : 0 pour semaine, 1 pour mois et 2 pour année
    * */
    private fun mCheckInforInServer(period:Int) {
        getFinishedGoals(period, object : OnGetDataListener {
            override fun onStart() {
                //DO SOME THING WHEN START GET DATA HERE
            }

            override fun onSuccess(data: HashMap<String,ArrayList<Goal>>) {
                dataModel.goals.value = data
            }

            override fun onFailed(databaseError: DatabaseError?) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        })
    }

    /*Se connecte à la base de données
    * Récupère les objectifs terminés de l'utilisateur et construit un hashmap
    * à partir de ces données.
    * Chaque type d'objectifs (clé) est associé à une liste d'objectifs terminés (valeur).
    * Un objectif est ajouté au hashmap si elle appartient à la fenêtre de temps définit par timeId
    * timeId : 0 pour semaine, 1 pour mois et 2 pour année
    * listener : pour détecter le moment où toutes les données ont été récupérées
    * */
    private fun getFinishedGoals(timeId : Int, listener : StatsFragment.OnGetDataListener){
        listener.onStart()
        val finishedGoalsRef = database.child("users").child(uid).child("objectifs terminés")
        finishedGoalsRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var processedGoals = 0
                for (goal in snapshot.getChildren()) {
                    var nbGoals = snapshot.getChildren().count()
                    var goalId = goal.key.toString()
                    var newGoal = Goal(goal.getValue().toString(),goal.key.toString())
                    var goalRef = database.child("objectifs")
                    goalRef.child(goalId).addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            processedGoals ++
                            var typeGoal = snapshot.child("type").getValue().toString()
                            if(validGoal(timeId,goal.getValue().toString().split("/"))){
                                if(goals[typeGoal] == null){
                                    goals[typeGoal] = ArrayList()
                                    goals[typeGoal]!!.add(newGoal)
                                } else{
                                    goals[typeGoal]!!.add(newGoal)
                                }
                            }
                            if(processedGoals == nbGoals){
                                listener.onSuccess(goals)
                            }


                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                    //goals[goal.key.toString()] = goal.getValue().toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

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

    /*
    * Met à jour l'histogramme
    * et execute la lecture asynchrone des données dans firebase
     */
    fun updateHistogram(button: Button){
        if (button.id.equals(R.id.week)) {
            dataModel.setAxis("week")
        } else if (button.id.equals(R.id.month)) {
            dataModel.setAxis("month")
        } else {
            dataModel.setAxis("year")
        }
        dataModel.goals.value = null

        val newHistogramFragment = HistogramFragment()
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_chart, newHistogramFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        mCheckInforInServer(sizeXaxisToInt(dataModel.axis.value!!.size))


    }




    /*
    * S'execute lorsque l'utilisateur clique sur un des boutons : semaine, mois ou année
    * Met à jour les vues associées aux boutons
    * Met à jour l'histogramme
    */
    fun selectTime(view: View, id: Int) {
        var time = id
        val layout = view.findViewById<Button>(id)
        var button = layout.findViewById<Button>(time)
        updateHistogram(button)
        statButtonController.setBackgroundDrawable(
            getResources()
                .getDrawable(currentDrawable)
        )

        button.setBackgroundDrawable(
            getResources().getDrawable(getDrawableButton(button, true))
        );

        statButtonController = button
        currentDrawable = getDrawableButton(button, false)

    }

    fun addButtonListeners(layout: View){
        val buttons = listOf("year","month","week")
        for(i in (0..buttons.size - 1)){
            var button = layout.findViewById<Button>(selectIdButton(buttons.get(i)))
            button.setOnClickListener{
                selectTime(layout, selectIdButton(buttons.get(i)))
            }
        }
    }

    override fun onStop() {
        dataModel.setSavedButton(statButtonController)
        dataModel.setSavedDrawable(currentDrawable)
        super.onStop()
    }

    //save the last clicked button
    override fun onDestroyView() {
        //dataModel.setSavedButton(statButtonController)
        //dataModel.setSavedDrawable(currentDrawable)
        super.onDestroyView()
    }

    fun useDefaultConfig(layout:View){
        statButtonController = layout.findViewById(R.id.week) as Button
        currentDrawable = R.drawable.right_rounded_button
        dataModel.setFirstTofalse()
        selectTime(statButtonController,statButtonController.id)
    }

    fun useSavedConfig(layout:View){
        currentDrawable = dataModel.currentDrawable
        statButtonController = layout.findViewById<Button>(dataModel.statButtonController.id)
        selectTime(statButtonController,statButtonController.id)
    }

    fun updateAttendance(data : DataSnapshot){
        val currentDay = LocalDateTime.now().dayOfYear
        val currentYear = LocalDateTime.now().year
        val lastConnexion = data.child("dernière connexion").getValue().toString().split("/")
        val lastConnexionDay = lastConnexion[0].toInt()
        val lastConnexionYear = lastConnexion[1].toInt()
        var attendance = data.child("assiduité").getValue().toString().toInt()
        if(lastConnexionYear == currentYear){
            if(currentDay - lastConnexionDay == 1){
                attendance = attendance + 1
                database
                    .child("users")
                    .child(uid!!)
                    .child("assiduité")
                    .setValue(attendance.toString())
            }
        }

    }

    /*
     * Lit et affiche assuidité
     */
    fun readAndDisplayAttendance(){
        val attendanceRef = database.child("users")
            .addListenerForSingleValueEvent( object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (data in snapshot.getChildren()) {
                        if(data.key.toString() == uid){
                            val textViewAttendance = view!!.findViewById<TextView>(R.id.attendance)
                            if (data.hasChild("assiduité")) {
                                val attendance = data.child("assiduité").getValue().toString()
                                if(attendance.toInt() < 10){
                                    textViewAttendance.setText("0" + attendance)
                                } else {
                                    textViewAttendance.setText(attendance)
                                }

                            } else {
                                textViewAttendance.setText("0")
                                //database.child("users").child(uid!!).child("assiduité").setValue("0")
                                //do something if not exists
                            }

                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }

            )

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        initDataBase()

        /* Réinitialiser la variable locale goals utilisée
         * pour construire le hashMap contenant les données
        */
        dataModel.goals.observe(viewLifecycleOwner, Observer<HashMap<String,ArrayList<Goal>>>{
                goal->
            goals = HashMap<String,ArrayList<Goal>>()
        })

        if(auth.currentUser != null){
            readAndDisplayAttendance()
        }

        statsFragmentViewModel =
            ViewModelProvider(this).get(StatsViewModel::class.java)
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        var layout = binding.root
        if(dataModel.firstCreate){
           useDefaultConfig(layout)
        } else {
            useSavedConfig(layout)
        }
        addButtonListeners(layout)
        return layout
    }




}