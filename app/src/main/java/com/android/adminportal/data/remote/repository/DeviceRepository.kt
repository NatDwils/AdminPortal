package com.android.adminportal.data.remote.repository

import com.android.adminportal.data.local.room.AppDatabase
import com.android.adminportal.data.model.Device
import com.android.adminportal.data.model.User
import com.android.adminportal.utils.viewState.ResponseResult
import com.google.firebase.firestore.FirebaseFirestore
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

    fun assignDevice(device: Device, user: User, callback: (ResponseResult<User>) -> Unit) {
        val userRef = store.collection("user").document(user.id)
        val deviceRef = userRef.collection("devices").document(device.deviceId)


        /*
        * Check if the device ID is already assigned to another user
        * */
        deviceRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    callback(ResponseResult.Error("Device ID is already assigned to another user."))
                } else {
                    deviceRef.set(device).addOnSuccessListener {
                        callback(ResponseResult.Success(user))
                    }.addOnFailureListener { exception ->
                        callback(ResponseResult.Error(exception.message.toString()))
                    }
                }
            } else {
                callback(ResponseResult.Error(task.exception?.message.toString()))
            }
        }
    }


}