package com.mis.route.todo.ui.edit

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mis.route.todo.Constants
import com.mis.route.todo.R
import com.mis.route.todo.database.AppDatabase
import com.mis.route.todo.database.model.TaskEntity
import com.mis.route.todo.databinding.ActivityEditTaskBinding
import com.mis.route.todo.ui.home.fragments.tasks.model.Task

class EditTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTaskBinding
    private var task: Task? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupStatusExposedDropdown()

        task = retrieveTask()
        displayTaskDetails(task)

        binding.dateSelector.setOnClickListener { showDatePickerDialog() }

        binding.saveTaskChangesButton.setOnClickListener {
            if (validateChanges(task)) {
                updateTask(task)
                Snackbar.make(this, binding.root, "Task Updated Successfully!",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupStatusExposedDropdown() {
        val items = listOf(Constants.COMPLETE, Constants.INCOMPLETE)
        val adapter = ArrayAdapter(this, R.layout.item_task_status_dropdown_menu, items)
        binding.taskStatusInput.setAdapter(adapter)
    }

    private fun updateTask(task: Task?) {
        task?.let {
            AppDatabase.getInstance(this).tasksDao()
                .updateTask(TaskEntity(
                    id = task.id,
                    title = binding.taskTitleInput.text.toString(),
                    date = binding.dateSelector.text.toString(),
                    status = binding.taskStatusInput.text.toString()
                ))
        }
    }

    private fun retrieveTask(): Task? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.EDIT_TASK_ACTIVITY_OBJECT_KEY, Task::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.EDIT_TASK_ACTIVITY_OBJECT_KEY)
        }
    }

    private fun displayTaskDetails(task: Task?) {
        task?.let {
            binding.taskTitleInput.setText(task.title)
            // false: no filtration for the options (the autocomplete component filters by default)
            binding.taskStatusInput.setText(task.status, false)
            binding.dateSelector.text = task.date
        }
    }

    private fun validateChanges(task: Task?): Boolean {
        // TODO: to be fixed later
//        var isChangedCount = 3
//        task?.let {
//            if (binding.taskTitleInput.text!!.toString() == task.title) {
//                binding.taskTitleInput.error = "No change to save"
//                isChangedCount--
//            }
//            if (binding.taskStatusInput.text.toString() == task.status) {
//                binding.taskStatusInput.error = "No change to save"
//                isChangedCount--
//            } else if (binding.taskStatusInput.error != null) binding.taskStatusInput.error = null
//            if (binding.dateSelector.text.toString() == task.date) {
//                binding.dateSelector.error = "No change to save"
//                isChangedCount--
//            }
//        }
//        return isChangedCount != 0
        return true
    }

    // TODO: this fun is duplicated (add task bottom sheet), is there something to be done about that?
    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            val dateFormat = "$dayOfMonth-${month + 1}-$year"
            if (binding.dateSelector.text != dateFormat) binding.dateSelector.error = null
            binding.dateSelector.text = dateFormat
        }
        datePickerDialog.show()
    }
}