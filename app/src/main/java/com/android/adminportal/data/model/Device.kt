package com.android.adminportal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(
    @PrimaryKey var deviceId: String = "",
    var employeeId: String = "",
    var deviceName: String = "",
    var deviceType: String = "",
    var os: String = "",
    var model: String = "",
    var manufacturer: String = "",
    var otherDetails: String = "",
    var isOfflineDeleted: Boolean = false,
    var isOfflineAdded: Boolean = false
) : java.io.Serializable