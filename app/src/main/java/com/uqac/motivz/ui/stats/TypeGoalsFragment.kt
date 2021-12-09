package com.uqac.motivz.ui.stats
import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.uqac.motivz.R
import com.uqac.motivz.databinding.FragmentStatsBinding

class TypeGoalsFragment : Fragment() {
    private var listGoals = listOf("Pas","Distance parcourue","Pompes","Squats","Abdos");
    private val dataModel: DataViewModel by activityViewModels()

    fun updateHistogram(button: Button){
        var selectedTypeGoal = button.text
        dataModel.goal.value = selectedTypeGoal.toString()
        val newHistogramFragment = HistogramFragment()
        val fragmentTransaction = requireFragmentManager().beginTransaction()
        fragmentTransaction.replace(R.id.fragment_verticalbarchart_chart, newHistogramFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun selectButton(button: Button) {
        updateHistogram(button)
    }

    fun addButtons(layout : LinearLayout, listGoals : List<String>){
        for (goal in listGoals){
            var button = Button(this.context)
            button.setText(goal)

            layout.addView(button);
            button.setOnClickListener{
                selectButton(button)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_typegoals, container, false)
        var listGoalLayout = view.findViewById<LinearLayout>(R.id.list_goal)
        addButtons(listGoalLayout,listGoals)
        return view
    }
}