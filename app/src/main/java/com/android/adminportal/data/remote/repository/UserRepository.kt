package com.android.adminportal.data.remote.repository

import com.android.adminportal.data.model.Device
import com.android.adminportal.data.model.User
import com.android.adminportal.utils.ktx.encode
import com.android.adminportal.utils.viewState.ResponseResult
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UserRepository @Inject constructor(var store: FirebaseFirestore) {

    fun fetchAllUsers(callback: (ResponseResult<List<User>>) -> Unit) {
        store.collectionGroup("user").get().addOnSuccessListener { querySnapshot ->
            callback(ResponseResult.Success(querySnapshot.toObjects(User::class.java)))

        }.addOnFailureListener { exception ->
            callback(ResponseResult.Error(exception.message.toString()))
        }
    }

    fun getUserDevices(user: User, callback: (ResponseResult<User>) -> Unit) {
        try {
            store.collection("user/${user.id}/devices").get()
                .addOnSuccessListener { querySnapshot ->
                    val devices: MutableList<Device> =
                        querySnapshot.toObjects(Device::class.java)
                    user.devices = devices.encode(MutableList::class.java)
                    user.deviceCount = devices.size
                    callback(ResponseResult.Success(user))
                }.addOnFailureListener { exception ->
                    callback(ResponseResult.Error(exception.message.toString()))
                }
        } catch (exception: Exception) {
            callback(ResponseResult.Error(exception.message.toString()))
        }
    }
}