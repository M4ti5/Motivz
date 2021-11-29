package com.uqac.motivz.ui.home

import android.os.Bundle
import android.widget.TextView
import com.uqac.motivz.databinding.ActivityGoalManagementBinding
import com.uqac.motivz.R
import androidx.fragment.app.Fragment

class GoalManagementActivity : androidx.appcompat.app.AppCompatActivity() {

    private lateinit var binding: com.uqac.motivz.databinding.ActivityGoalManagementBinding
    private lateinit var actionId: String

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