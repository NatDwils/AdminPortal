package com.android.adminportal.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.android.adminportal.data.model.Device
import kotlinx.coroutines.flow.Flow


@Dao
interface DevicesDao {

    @Query("SELECT * FROM devices")
    fun getAllDevices(): Flow<List<Device>>

    @Query("SELECT * FROM devices WHERE deviceId = :deviceId")
    fun getDeviceById(deviceId: String): Flow<Device?>

    @Insert
    fun insertDevice(device: Device)

    @Insert
    suspend fun insertDevices(devices: List<Device>)

    @Update
    suspend fun updateDevice(device: Device?)

    @Query("DELETE FROM devices")
    suspend fun deleteAll()

}
