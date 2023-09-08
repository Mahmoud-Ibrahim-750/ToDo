package com.mis.route.todo

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

object Constants {
    // UI
    const val TASKS_LIST_FRAGMENT_TAG = "TasksListFragment"
    const val ADD_TASK_BOTTOM_SHEET_FRAGMENT_TAG = "AddTaskBottomSheetFragment"
    const val EDIT_TASK_ACTIVITY_OBJECT_KEY = "EditTaskActivityObjectKey"

    // Languages Codes
    const val ENGLISH_CODE = "en"
    const val ARABIC_CODE = "ar"

    // Database
    const val DATABASE_NAME = "ToDoAppDatabase"

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

    fun LocalDate.formatLocalDate(): String = "${this.dayOfMonth}-${this.monthValue}-${this.year}"
}