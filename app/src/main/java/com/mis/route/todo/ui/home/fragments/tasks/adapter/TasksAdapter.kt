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
        }

        fun setViewsColor(color: Int) {
            binding.title.setTextColor(color)
            binding.time.setTextColor(color)
            binding.divider.setBackgroundColor(color)
            binding.checkButton.setBackgroundResource(R.drawable.shape_checked_button)
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

            if (task.status == Constants.COMPLETE) {
                val theme = holder.binding.root.context.theme
                val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.binding.root.context.resources.getColor(R.color.green, theme)
                } else {
                    holder.binding.root.context.resources.getColor(R.color.green)
                }
                holder.setViewsColor(color)
            }

            holder.binding.root.setOnClickListener { onTaskItemClickListener.onClick(position) }
            holder.binding.checkButton.setOnClickListener { onTaskCheckedClickListener.onClick(position) }
            holder.binding.deleteView.setOnClickListener { onTaskDeleteClickListener.onClick(position) }
        }
    }

    override fun getItemCount() = tasksList?.size ?: 0

    lateinit var onTaskItemClickListener: OnTaskClickListener
    lateinit var onTaskDeleteClickListener: OnTaskClickListener
    lateinit var onTaskCheckedClickListener: OnTaskClickListener

    fun interface OnTaskClickListener {
        fun onClick(position: Int)
    }
}