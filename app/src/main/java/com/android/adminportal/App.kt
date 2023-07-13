package com.android.adminportal

import android.app.Application
import androidx.lifecycle.*
import com.android.adminportal.data.local.MyPref.DataPreference
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    /*
    * Live data for sync activities...
    * */
    companion object {
        private val _syncData = MutableLiveData<Boolean>()
        val syncData: LiveData<Boolean>
            get() = _syncData
    }

    override fun onCreate() {
        super.onCreate()
        initLifecycleOwner()
        initDataPreference()
    }

    /*
    * Application lifecycle observer..
    * */
    private fun initLifecycleOwner() {
        val defaultLifecycleObserver = object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                _syncData.postValue(true)
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                _syncData.postValue(false)
            }
        }

        ProcessLifecycleOwner.get().lifecycle.addObserver(defaultLifecycleObserver)
    }

    /*
    * Init local data pref..
    * */
    private fun initDataPreference() {
        DataPreference.init(applicationContext)
    }
}