package com.android.adminportal.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.adminportal.data.model.Device
import com.android.adminportal.data.model.User

@Database(entities = [Device::class, User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun devicesDao(): DevicesDao
    abstract fun usersDao(): UsersDao
}
