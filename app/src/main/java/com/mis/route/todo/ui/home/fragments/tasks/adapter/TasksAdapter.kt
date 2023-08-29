package com.mis.route.todo.ui.home.fragments.tasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mis.route.todo.databinding.ItemTaskBinding
import com.mis.route.todo.ui.home.fragments.tasks.model.Task

class TasksAdapter(var tasksList: MutableList<Task>?) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    private lateinit var binding: ItemTaskBinding

    class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        tasksList?.let {
            val task = tasksList!![position]
            holder.binding.title.text = task.title
            holder.binding.time.text = task.date
            holder.binding.checkButton.setOnClickListener {
                onTaskItemClickListener.onClick(position)
            }
            holder.binding.deleteView.setOnClickListener {
                onTaskDeleteClickListener.onClick(position)
            }
        }
    }

    override fun getItemCount() = tasksList?.size ?: 0

    lateinit var onTaskItemClickListener: OnTaskClickListener
    lateinit var onTaskDeleteClickListener: OnTaskClickListener

    fun interface OnTaskClickListener {
        fun onClick(position: Int)
    }
}