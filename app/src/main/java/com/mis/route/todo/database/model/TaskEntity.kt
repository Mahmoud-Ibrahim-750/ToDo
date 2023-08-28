package com.mis.route.todo.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mis.route.todo.ui.home.fragments.tasks.model.Task

@Entity(tableName = "Tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = -1,
    @ColumnInfo var title: String,
    @ColumnInfo var date: String,
    @ColumnInfo var status: String
) {
    companion object {
        fun TaskEntity.toTask(): Task {
            return Task(this.id, this.title, this.date, this.status)
        }

        fun MutableList<TaskEntity>.toTaskList(): MutableList<Task> {
            val tasks = mutableListOf<Task>()
            for (entity in this) tasks.add(entity.toTask())
            return tasks
        }
    }
}
