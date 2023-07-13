package com.android.adminportal.data.remote.repository

import com.android.adminportal.data.local.room.AppDatabase
import com.android.adminportal.data.model.Device
import com.android.adminportal.utils.viewState.ResponseResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeviceRepository @Inject constructor(
    var store: FirebaseFirestore,
    private val database: AppDatabase
) {
    fun fetchAllDevices(callback: (ResponseResult<List<Device>>) -> Unit) {
        store.collectionGroup("devices").get().addOnSuccessListener { querySnapshot ->
            val devices = querySnapshot.toObjects(Device::class.java).filter { !it.isOfflineDeleted }
            callback(ResponseResult.Success(devices))
        }.addOnFailureListener { exception ->
            callback(ResponseResult.Error(exception.message.toString()))
        }
    }

    fun deleteDevice(device: Device?, callback: (ResponseResult<String>) -> Unit) {
        val query = store.collectionGroup("devices").whereEqualTo("deviceId", device?.deviceId)
        query.get().addOnSuccessListener {
            for (doc in it.documents) {
                doc.reference.delete().addOnSuccessListener {
                    callback(ResponseResult.Success(""))
                }.addOnFailureListener { exception ->
                    callback(ResponseResult.Error(exception.message.toString()))
                }
            }
        }.addOnFailureListener {

        }
    }

}