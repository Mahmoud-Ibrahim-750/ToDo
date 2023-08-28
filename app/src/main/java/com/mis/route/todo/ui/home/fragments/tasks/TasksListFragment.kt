package com.mis.route.todo.ui.home.fragments.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mis.route.todo.database.AppDatabase
import com.mis.route.todo.database.model.TaskEntity.Companion.toTaskList
import com.mis.route.todo.databinding.FragmentTasksListBinding
import com.mis.route.todo.ui.home.fragments.tasks.adapter.TasksAdapter

class TasksListFragment : Fragment() {
    private lateinit var binding: FragmentTasksListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tasksList = AppDatabase.getInstance(requireContext().applicationContext)
            .tasksDao().getAll().toTaskList()

        val adapter = TasksAdapter(tasksList)
        adapter.taskClickListener = TasksAdapter.OnTaskClickListener { position ->
            // TODO: check this task (impl.)
            Toast.makeText(context, tasksList[position].title, Toast.LENGTH_SHORT).show()
        }
        binding.tasksRecycler.adapter = adapter
    }
}