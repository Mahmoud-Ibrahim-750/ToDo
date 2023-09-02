package com.mis.route.todo.ui.home.fragments.tasks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mis.route.todo.Constants
import com.mis.route.todo.database.AppDatabase
import com.mis.route.todo.database.model.TaskEntity.Companion.toTaskList
import com.mis.route.todo.databinding.FragmentTasksListBinding
import com.mis.route.todo.ui.edit.EditTaskActivity
import com.mis.route.todo.ui.home.fragments.tasks.adapter.TasksAdapter
import com.mis.route.todo.ui.home.fragments.tasks.model.Task
import com.mis.route.todo.ui.home.fragments.tasks.model.Task.Companion.toTaskEntity

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

    private var tasksList: MutableList<Task>? = null
    private val adapter = TasksAdapter(null)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.onTaskItemClickListener = TasksAdapter.OnTaskClickListener { position ->
            tasksList?.let {
                val intent = Intent(context, EditTaskActivity::class.java)
                intent.putExtra(Constants.EDIT_TASK_ACTIVITY_OBJECT_KEY, tasksList!![position])
                startActivity(intent)
            }
        }

        adapter.onTaskDeleteButtonClickListener = TasksAdapter.OnTaskClickListener { position ->
            tasksList?.let {
                deleteTask(tasksList!![position])
                adapter.notifyItemRemoved(position)
            }
        }

        adapter.onTaskCheckButtonClickListener = TasksAdapter.OnTaskClickListener { position ->
            tasksList?.let {
                if (position < tasksList!!.size) {
                    completeTaskAt(position)
                    adapter.notifyItemChanged(position)
                }
            }
        }
        binding.tasksRecycler.adapter = adapter

        val todayDate = Constants.getTodayDate()
        loadTasksByDateAndNotifyAdapter(todayDate)
    }

    private fun completeTaskAt(position: Int) {
        val task = tasksList!![position]
        task.status = Constants.COMPLETE
        AppDatabase.getInstance(requireContext().applicationContext)
            .tasksDao().updateTask(task.toTaskEntity())
        tasksList!![position] = task
        adapter.tasksList!![position] = task
    }

    private fun deleteTask(task: Task) {
        AppDatabase.getInstance(requireContext().applicationContext)
            .tasksDao()
            .deleteTask(task.toTaskEntity())
        tasksList?.remove(task)
        adapter.tasksList?.remove(task)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadTasksByDateAndNotifyAdapter(date: String) {
        tasksList = context?.let {
            AppDatabase.getInstance(it.applicationContext)
                .tasksDao()
                .getTasksByDate(date).toTaskList()
        }
        adapter.tasksList = tasksList
        adapter.notifyDataSetChanged()
    }

    fun notifyTaskInsertionAtRear() {
        adapter.notifyItemInserted(tasksList!!.size - 1)
    }

    override fun onResume() {
        super.onResume()
        // reload task again
        val todayDate = Constants.getTodayDate()
        loadTasksByDateAndNotifyAdapter(todayDate)
    }
}
