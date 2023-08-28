package com.mis.route.todo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mis.route.todo.database.model.TaskEntity

@Dao
interface TasksDao {
    @Insert
    fun addTask(task: TaskEntity)

    @Update
    fun updateTask(task: TaskEntity)

    @Delete
    fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM Tasks")
    fun getAll(): MutableList<TaskEntity>

    @Query("SELECT * FROM Tasks WHERE id LIKE :taskId")
    fun getTaskById(taskId: Int): TaskEntity
}