package com.mis.route.todo.ui.home.fragments.tasks

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mis.route.todo.Constants
import com.mis.route.todo.database.AppDatabase
import com.mis.route.todo.database.model.TaskEntity
import com.mis.route.todo.database.model.TaskEntity.Companion.toTask
import com.mis.route.todo.databinding.BottomSheetAddTaskBinding
import com.mis.route.todo.ui.home.fragments.tasks.model.Task

class AddTaskBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetAddTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun invalidateInput(): Boolean {
        return if (binding.inputTaskTitle.text.isNullOrBlank()) {
            binding.inputTaskTitle.error = "Title can't be empty!"
            false
        } else true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayCurrentDate()
        binding.dateSelector.setOnClickListener {
            showDatePickerDialog()
        }

        binding.addTaskButton.setOnClickListener {
            if (!invalidateInput()) return@setOnClickListener
            val addedTask = createAndAddTask()
            onTaskAddedListener.onTaskAdded(addedTask) // to reload tasks (to notify the adapter)
            dismiss()
        }
    }

    private fun createAndAddTask(): Task {
        val newTaskEntity = TaskEntity(
            title = binding.inputTaskTitle.text.toString(),
            date = binding.dateSelector.text.toString(),
            status = Constants.INCOMPLETE)
        AppDatabase.getInstance(requireContext().applicationContext)
            .tasksDao().addTask(newTaskEntity)
        return newTaskEntity.toTask()
    }

    private fun displayCurrentDate() {
        val todayDate = Constants.getTodayDate()
        binding.dateSelector.text = todayDate
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            val dateFormat = "$dayOfMonth-${month + 1}-$year"
            binding.dateSelector.text = dateFormat
        }
        datePickerDialog.show()
    }

    lateinit var onTaskAddedListener: OnTaskAddedListener

    fun interface OnTaskAddedListener {
        fun onTaskAdded(task: Task)
    }
}