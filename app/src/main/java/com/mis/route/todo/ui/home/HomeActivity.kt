package com.mis.route.todo.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mis.route.todo.Constants
import com.mis.route.todo.R
import com.mis.route.todo.databinding.ActivityHomeBinding
import com.mis.route.todo.ui.home.fragments.settings.SettingsFragment
import com.mis.route.todo.ui.home.fragments.tasks.AddTaskBottomSheet
import com.mis.route.todo.ui.home.fragments.tasks.TasksListFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // by default
        showFragment(TasksListFragment())

        binding.bottomNavView.setOnItemSelectedListener {
            showFragment(
                when (it.itemId) {
                R.id.tasks_nav -> TasksListFragment()
                else -> SettingsFragment()
            })
            true
        }

        binding.addTaskFab.setOnClickListener { showAddTaskBottomSheet() }
    }

    private fun showAddTaskBottomSheet() {
        val addTaskBottomSheet = AddTaskBottomSheet()
        addTaskBottomSheet.onTaskAddedListener = AddTaskBottomSheet.OnTaskAddedListener {
            val tasksListFragment = supportFragmentManager
                .findFragmentByTag(Constants.TASK_LIST_FRAGMENT_TAG) as TasksListFragment
            tasksListFragment.loadTasks()
            tasksListFragment.notifyTaskInsertionAtRear()
        }
        addTaskBottomSheet.show(supportFragmentManager, "AddTask")
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, fragment, Constants.TASK_LIST_FRAGMENT_TAG)
            .commit()
    }
}