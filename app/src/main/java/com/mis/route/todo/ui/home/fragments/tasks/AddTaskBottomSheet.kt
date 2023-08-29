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
import com.mis.route.todo.databinding.BottomSheetAddTaskBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
            addTask()
            onTaskAddedListener.onTaskAdded() // to reload tasks (to notify the adapter)
            dismiss()
        }
    }

    private fun addTask() {
        AppDatabase.getInstance(requireContext().applicationContext)
            .tasksDao().addTask(
                TaskEntity(
                    title = binding.inputTaskTitle.text.toString(),
                    date = binding.dateSelector.text.toString(),
                    status = Constants.INCOMPLETE)
            )
    }

    private fun displayCurrentDate() {
        val formatter = SimpleDateFormat("dd-M-yyyy", Locale.US)
        val currentDate = Calendar.getInstance(Locale.US).time
        binding.dateSelector.text = formatter.format(currentDate)
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
        fun onTaskAdded()
    }
}