package com.mis.route.todo.ui.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.mis.route.todo.Constants
import com.mis.route.todo.R
import com.mis.route.todo.database.AppDatabase
import com.mis.route.todo.database.model.TaskEntity
import com.mis.route.todo.database.model.TaskEntity.Companion.toTask
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
            hideKeyboard()
            if (validateChanges(task)) {
                task = updateTask(task)?.toTask() // just in case user tries updating again
                Snackbar.make(
                    this, binding.root, "Task Updated Successfully!",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(this, binding.root, "No changes to save", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // compare the syntax, no nesting level
//    private fun hideKeyboard() {
//        val view = this.currentFocus
//        view?.let {
//            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }

    private fun hideKeyboard() {
        val view = this.currentFocus ?: return // if view is null, return
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupStatusExposedDropdown() {
        val items = listOf(Constants.COMPLETE, Constants.INCOMPLETE)
        val adapter = ArrayAdapter(this, R.layout.item_task_status_dropdown_menu, items)
        binding.taskStatusInput.setAdapter(adapter)
    }

    private fun updateTask(task: Task?): TaskEntity? {
        task?.let {
            val entity = TaskEntity(
                id = task.id,
                title = binding.taskTitleInput.text.toString(),
                date = binding.dateSelector.text.toString(),
                status = binding.taskStatusInput.text.toString()
            )
            AppDatabase.getInstance(this).tasksDao()
                .updateTask(entity)
            return entity
        }
        return null
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
        var isChanged = false
        task?.let {
            isChanged = (binding.taskTitleInput.text!!.toString() != task.title) ||
                        (binding.taskStatusInput.text.toString() != task.status) ||
                        (binding.dateSelector.text.toString() != task.date)
        }
        return isChanged
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