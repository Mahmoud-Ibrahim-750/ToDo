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
import com.mis.route.todo.ui.home.fragments.tasks.model.Task

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

    private var tasksList : MutableList<Task>? = null
    private val adapter = TasksAdapter(null)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadTasks()
        adapter.tasksList = tasksList
        adapter.taskClickListener = TasksAdapter.OnTaskClickListener { position ->
            // TODO: check this task (impl.)
            tasksList?.let {
                Toast.makeText(context, tasksList!![position].title, Toast.LENGTH_SHORT).show()
            }
        }
        binding.tasksRecycler.adapter = adapter
        tasksList?.size?.let { adapter.notifyItemRangeInserted(0, it) }
    }

    fun loadTasks() {
        tasksList = context?.let {
            AppDatabase.getInstance(it.applicationContext)
                .tasksDao()
                .getAll().toTaskList()
        }
        adapter.tasksList = tasksList
    }

    fun notifyTaskInsertionAtRear() {
        adapter.notifyItemInserted(tasksList!!.size - 1 )
    }
}
