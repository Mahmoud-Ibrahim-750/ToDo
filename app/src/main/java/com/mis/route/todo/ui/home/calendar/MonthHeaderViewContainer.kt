package com.mis.route.todo.ui.home.calendar

import android.view.View
import com.kizitonwose.calendar.view.ViewContainer
import com.mis.route.todo.databinding.CalendarMonthHeaderBinding

class MonthHeaderViewContainer(view: View) : ViewContainer(view) {
    val calendarMonthTitle = CalendarMonthHeaderBinding.bind(view).monthTitle
}