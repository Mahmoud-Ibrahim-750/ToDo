package com.mis.route.todo.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder
import com.mis.route.todo.Constants
import com.mis.route.todo.Constants.formatLocalDate
import com.mis.route.todo.R
import com.mis.route.todo.databinding.ActivityHomeBinding
import com.mis.route.todo.ui.home.calendar.DayViewContainer
import com.mis.route.todo.ui.home.calendar.MonthHeaderViewContainer
import com.mis.route.todo.ui.home.fragments.settings.SettingsFragment
import com.mis.route.todo.ui.home.fragments.tasks.AddTaskBottomSheet
import com.mis.route.todo.ui.home.fragments.tasks.TasksListFragment
import com.mis.route.todo.ui.home.fragments.tasks.model.Task
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    var selectedDate: LocalDate = LocalDate.now() // today
    var monthName: String? = Calendar.getInstance()
        .getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) // this month
    var year = Calendar.getInstance().get(Calendar.YEAR).toString() // this year
    var calendarHeaderTitle = "$monthName - $year" // default header

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // by default
        showFragment(TasksListFragment())

        binding.bottomNavView.setOnItemSelectedListener {
            showFragment(
                when (it.itemId) {
                    R.id.tasks_nav -> TasksListFragment()
                    else -> SettingsFragment()
                }
            )
            initViews(it.itemId)
            true
        }

        binding.addTaskFab.setOnClickListener { showAddTaskBottomSheet() }

        // calendar day binder
        binding.calendarView.dayBinder = object : WeekDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: WeekDay) {
                // Initialize the calendar day for this container.
                container.day = data

                container.onDayClickListener = DayViewContainer.OnDayClickListener { date ->
                    selectDayAndLoadTasks(container, date)
                }

                // Show the month dates. Remember that views are reused!
                val colorResId: Int =
                    if (container.day.date == selectedDate) R.color.blue else R.color.black

                container.calendarDayNumber.setTextColor(ContextCompat.getColor(this@HomeActivity, colorResId))
                container.calendarDayName.setTextColor(ContextCompat.getColor(this@HomeActivity, colorResId))

                monthName = data.date.month.toString() // for use outside (in header)
                year = data.date.year.toString()
                calendarHeaderTitle = "$monthName - $year"
                container.calendarDayNumber.text = data.date.dayOfMonth.toString()
                container.calendarDayName.text = data.date.dayOfWeek.toString().substring(0..2)
            }
        }

        // calendar month header binder
        binding.calendarView.weekHeaderBinder =
            object : WeekHeaderFooterBinder<MonthHeaderViewContainer> {
                override fun create(view: View) = MonthHeaderViewContainer(view)

                override fun bind(container: MonthHeaderViewContainer, data: Week) {
                    container.calendarMonthTitle.text = calendarHeaderTitle
                }
            }

        // calendar setup
        val currentDate = LocalDate.now()
        val currentMonth = YearMonth.now()
        val startDate = currentMonth.minusMonths(10).atStartOfMonth()
        val endDate = currentMonth.plusMonths(10).atEndOfMonth()
        val firstDayOfWeek = firstDayOfWeekFromLocale()
        binding.calendarView.setup(startDate, endDate, firstDayOfWeek)
        binding.calendarView.scrollToWeek(currentDate)
    }

    private fun selectDayAndLoadTasks(container: DayViewContainer, date: String) {
        val tasksListFragment = supportFragmentManager
            .findFragmentByTag(Constants.TASKS_LIST_FRAGMENT_TAG) as TasksListFragment
        tasksListFragment.loadTasksByDateAndNotifyAdapter(date)

        // update selected day date and notify selection change to rebind the day layout
        // and the selection effect take place
        val currentSelection = selectedDate
        if (currentSelection == container.day.date) {
            // clicked the same date, do nothing
        } else {
            selectedDate = container.day.date
            binding.calendarView.notifyDateChanged(container.day.date)
            // notify previously selected day deselection to remove selection effect
            binding.calendarView.notifyDateChanged(currentSelection)
        }
    }

    private fun showAddTaskBottomSheet() {
        val addTaskBottomSheet = AddTaskBottomSheet()
        addTaskBottomSheet.onTaskAddedListener = AddTaskBottomSheet.OnTaskAddedListener {
            notifyTaskAdded(it)
        }
        addTaskBottomSheet.show(
            supportFragmentManager,
            Constants.ADD_TASK_BOTTOM_SHEET_FRAGMENT_TAG
        )
    }

    private fun notifyTaskAdded(addedTask: Task) {
        // get today
        val todayDate = Constants.getTodayDate()
        // check if today is displayed (if not, no refreshing is required)
        if (todayDate == addedTask.date) {
            val tasksListFragment = supportFragmentManager
                .findFragmentByTag(Constants.TASKS_LIST_FRAGMENT_TAG) as TasksListFragment
            tasksListFragment.loadTasksByDateAndNotifyAdapter(addedTask.date)
            tasksListFragment.notifyTaskInsertionAtRear()
        }
    }

    private fun showFragment(fragment: Fragment, tag: String = Constants.TASKS_LIST_FRAGMENT_TAG) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, fragment, tag)
            .commit()
    }

    private fun initViews(fragmentId: Int) {
        setActivityTitle(fragmentId)
        toggleCalendarVisibility(fragmentId)
        toggleSeparatorVisibility(fragmentId)
    }

    private fun toggleSeparatorVisibility(fragmentId: Int) {
        binding.actionBarSeparator.visibility = when (fragmentId) {
            R.id.tasks_nav -> View.GONE
            else -> View.VISIBLE
        }
    }

    private fun toggleCalendarVisibility(fragmentId: Int) {
        binding.calendarView.visibility = when (fragmentId) {
            R.id.tasks_nav -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun setActivityTitle(fragmentId: Int) {
        binding.toolbar.title = resources.getString(
            when (fragmentId) {
                R.id.tasks_nav -> R.string.tasks_list_title
                else -> R.string.settings_title
            }
        )
    }

    override fun onResume() {
        super.onResume()
        // reload tasks to reflect changes that may have occurred
        val tasksListFragment = supportFragmentManager
            .findFragmentByTag(Constants.TASKS_LIST_FRAGMENT_TAG) as TasksListFragment
        tasksListFragment.loadTasksByDateAndNotifyAdapter(selectedDate.formatLocalDate())
    }
}