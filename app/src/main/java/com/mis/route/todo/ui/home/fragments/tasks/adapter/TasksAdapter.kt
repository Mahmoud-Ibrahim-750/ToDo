package com.mis.route.todo.ui.home.fragments.tasks.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mis.route.todo.Constants
import com.mis.route.todo.R
import com.mis.route.todo.databinding.ItemTaskBinding
import com.mis.route.todo.ui.home.fragments.tasks.model.Task

class TasksAdapter(var tasksList: MutableList<Task>?) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    private lateinit var binding: ItemTaskBinding

    class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindViews(task: Task) {
            binding.title.text = task.title
            binding.time.text = task.date
            setViewsColor(task)
        }

        private fun getColorFromResource(colorId: Int): Int {
            val theme = binding.root.context.theme
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.root.context.resources.getColor(colorId, theme)
            } else {
                @Suppress("DEPRECATION")
                binding.root.context.resources.getColor(colorId)
            }
        }

        private fun setViewsColor(task: Task) {
            val color : Int
            val buttonShape : Int
            if (task.status == Constants.COMPLETE) {
                color = getColorFromResource(R.color.green)
                buttonShape = R.drawable.shape_checked_button
            } else {
                color = getColorFromResource(R.color.blue)
                buttonShape = R.drawable.shape_unchecked_button
            }
            binding.title.setTextColor(color)
            binding.time.setTextColor(color)
            binding.divider.setBackgroundColor(color)
            binding.checkButton.setBackgroundResource(buttonShape)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        tasksList?.let {
            val task = tasksList!![position]
            holder.bindViews(task)

            holder.binding.root.setOnClickListener { onTaskItemClickListener.onClick(position) }
            holder.binding.checkButton.setOnClickListener { onTaskCheckButtonClickListener.onClick(position) }
            holder.binding.deleteView.setOnClickListener { onTaskDeleteButtonClickListener.onClick(position) }
        }
    }

    override fun getItemCount() = tasksList?.size ?: 0

    lateinit var onTaskItemClickListener: OnTaskClickListener
    lateinit var onTaskDeleteButtonClickListener: OnTaskClickListener
    lateinit var onTaskCheckButtonClickListener: OnTaskClickListener

    fun interface OnTaskClickListener {
        fun onClick(position: Int)
    }
}