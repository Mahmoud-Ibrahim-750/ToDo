package com.mis.route.todo

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Constants {
    // UI
    const val TASKS_LIST_FRAGMENT_TAG = "TasksListFragment"
    const val ADD_TASK_BOTTOM_SHEET_FRAGMENT_TAG = "AddTaskBottomSheetFragment"

    // Task Status
//    const val UNREGISTERED = "UnRegistered"
    const val INCOMPLETE = "Incomplete"
    const val COMPLETE = "Complete"
//    const val INAPPLICABLE = "Incomplete"

    // Date
    const val DATE_FORMAT = "d-M-yyyy"
    fun getTodayDate(): String {
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.US)
        val formattedDate = Calendar.getInstance(Locale.US).time
        return formatter.format(formattedDate)
    }
}