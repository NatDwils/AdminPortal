package com.android.adminportal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey var id: String = "",
    var admin: Boolean = true,
    var employeeId: String = "",
    var name: String = "",
    var email: String = "",
    var mobileNumber: String = "",
    var isOfflineDeleted: Boolean = false,
    var deviceCount: Int = 0,
    var devices: String = ""
) : java.io.Serializable