package com.mis.route.todo.ui.home.calendar

import android.view.View
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.view.ViewContainer
import com.mis.route.todo.databinding.ItemCalendarDayBinding

class DayViewContainer(view: View) : ViewContainer(view) {
     val calendarDayNumber = ItemCalendarDayBinding.bind(view).calendarDayNumber
     val calendarDayName = ItemCalendarDayBinding.bind(view).calendarDayName

     // Will be set when this container is bound
     lateinit var day: WeekDay

     init {
          view.setOnClickListener {
               val date = "${day.date.dayOfMonth}-${day.date.monthValue}-${day.date.year}"
               onDayClickListener.onDayClicked(date)
          }
     }

     lateinit var onDayClickListener: OnDayClickListener
     fun interface OnDayClickListener {
          fun onDayClicked(date: String)
     }
}