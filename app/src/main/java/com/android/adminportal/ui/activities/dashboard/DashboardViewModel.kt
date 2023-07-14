package com.android.adminportal.ui.activities.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.adminportal.App
import com.android.adminportal.data.local.room.AppDatabase
import com.android.adminportal.utils.others.AppUtils
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var store: FirebaseFirestore

    fun init() {
        observeSync(App.syncData)
    }

    /*
    * Sync delete devices when online..
    * */
    private fun observeSync(syncData: LiveData<Boolean>) {
        syncData.observeForever {
            viewModelScope.launch {
                if (it && AppUtils.isInternetAvailable()) {
                    database.devicesDao().getAllDevices().onEach { list ->
                        list.filter { item -> item.isOfflineDeleted }.forEach { device ->
                            val query = store.collectionGroup("devices")
                                .whereEqualTo("deviceId", device.deviceId)
                            query.get().addOnSuccessListener { snap ->
                                for (doc in snap) {
                                    doc.reference.delete()
                                }
                            }
                        }
                    }.collect()
                }
            }
        }
    }
}