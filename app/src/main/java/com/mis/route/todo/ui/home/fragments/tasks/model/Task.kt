package com.mis.route.todo.ui.home.fragments.tasks.model

import android.os.Parcelable
import com.mis.route.todo.database.model.TaskEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id: Int = -1,
    var title: String,
    var date: String,
    var status: String
) : Parcelable {
    companion object {
        fun Task.toTaskEntity(): TaskEntity {
            return TaskEntity(this.id, this.title, this.date, this.status)
        }

        fun MutableList<Task>.toTaskEntityList(): MutableList<TaskEntity> {
            val entities = mutableListOf<TaskEntity>()
            for (task in this) entities.add(task.toTaskEntity())
            return entities
        }
    }
}
