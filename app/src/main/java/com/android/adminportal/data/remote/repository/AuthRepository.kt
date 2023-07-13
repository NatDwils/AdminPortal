package com.android.adminportal.data.remote.repositoryImpl

import com.android.adminportal.data.model.User
import com.android.adminportal.utils.ktx.encode
import com.android.adminportal.utils.viewState.ResponseResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AuthRepository @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    var store: FirebaseFirestore
) {

    /*
    * Login by firebase auth (email & password auth)
    * */
    fun login(email: String, password: String, callback: (ResponseResult<String>) -> Unit) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = task.result.user?.uid ?: ""
                fetchUserDetails(userId,callback)
            } else {
                callback(ResponseResult.Error("Something went wrong, please try after some time."))
            }
        }
    }

    private fun fetchUserDetails(userId: String, callback: (ResponseResult<String>) -> Unit) {
        val root = "user"
        store.collection(root).document(userId).get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot != null && querySnapshot.exists()) {
                val user = querySnapshot.toObject(User::class.java)
                user?.admin?.let {
                    if (it) {
                        callback(
                            ResponseResult.Success(
                                user?.encode(User::class.java) ?: ""
                            )
                        )
                    }else {
                        callback(ResponseResult.Error("Invalid user, user is not admin."))
                    }
                }
            }
        }.addOnFailureListener { exception ->
            callback(ResponseResult.Error("Something went wrong, please try after some time."))
        }
    }

    /*
    * Register from firebase auth
    * */
    fun register(user: User, password: String, callback: (ResponseResult<User>) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.id = task.result?.user?.uid ?: ""
                    val userCollection = store.collection("user")
                    val userMapper = hashMapOf(
                        "id" to user.id,
                        "employeeId" to user.employeeId,
                        "name" to user.name,
                        "email" to user.email,
                        "mobileNumber" to user.mobileNumber,
                        "admin" to user.admin,
                    )
                    userCollection.document(user.id).set(userMapper)
                        .addOnSuccessListener {
                            callback(ResponseResult.Success(user))
                        }.addOnFailureListener { exception ->
                            callback(ResponseResult.Error(exception.message.toString()))
                        }
                } else {
                    callback(ResponseResult.Error("Something went wrong, please try after some time."))
                }
            }
    }

    fun reset(email: String, callback: (ResponseResult<String>) -> Unit) {

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                    callback.invoke(ResponseResult.Success("Email sent"))
            } else {
                callback(ResponseResult.Error("Something went wrong, please try after some time."))
            }
        }.addOnFailureListener {
            callback.invoke(ResponseResult.Error("No User exists with $email. Please check you email and try again"))

        }
    }

}