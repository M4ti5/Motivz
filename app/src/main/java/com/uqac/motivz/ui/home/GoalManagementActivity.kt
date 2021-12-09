package com.uqac.motivz.ui.home

import android.os.Bundle
import com.uqac.motivz.databinding.ActivityGoalManagementBinding
import com.uqac.motivz.R
import androidx.fragment.app.Fragment

class GoalManagementActivity : androidx.appcompat.app.AppCompatActivity() {

    private lateinit var binding: com.uqac.motivz.databinding.ActivityGoalManagementBinding
    private lateinit var actionId: String

    data class Goal (val _name:String? = null , val _type:String? = null, var _stateValue:String? = null, val _maxValue:String? = null, val _createdAt:String? = null, var _finishedAt:String? = null , var _isFinished:Boolean = false  )
    fun setGoal( name:String, type:String, stateValue:String, maxValue:String, createdAt:String, finishedAt:String, isFinished:Boolean) : Goal{
        return Goal(name, type , stateValue, maxValue, createdAt,finishedAt, isFinished)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val action = intent.getStringExtra("ACTION")

        if (action == "create") {
            replaceFragment(CreateGoalFragment())
        }

        // TODO : Add fragment EditGoal
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.goalManagementFragmentContainer, fragment)
        fragmentTransaction.commit()
    }

}