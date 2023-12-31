package com.mis.route.todo.database

import android.content.Context
import com.mis.route.todo.Constants
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mis.route.todo.database.dao.TasksDao
import com.mis.route.todo.database.model.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tasksDao() : TasksDao

    // TODO: apply singleton pattern here, explain?
    // TODO: SEARCH for semafore
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        Constants.DATABASE_NAME
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}