package com.android.adminportal.data.remote.repository

import com.android.adminportal.data.model.User
import com.android.adminportal.utils.viewState.ResponseResult
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val store: FirebaseFirestore
) {
    fun fetchUserDetails(user: User?, callback: (User?) -> Unit) {
        user?.let {
            callback(it)
        }
    }

    fun updateUser(user: User, callback: (ResponseResult<User>) -> Unit) {
        try {
            val userCollection = store.collection("user")

            userCollection.document(user.id).update(
                mapOf(
                    "name" to user.name,
                    "mobileNumber" to user.mobileNumber
                )
            ).addOnSuccessListener {
                callback(ResponseResult.Success(user))
            }.addOnFailureListener { exception ->
                callback(ResponseResult.Error(exception.message.toString()))
            }
        } catch (e: Exception) {
            callback(ResponseResult.Error(e.message.toString()))
        }
    }
}
