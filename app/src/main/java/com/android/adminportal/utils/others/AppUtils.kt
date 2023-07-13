package com.android.adminportal.utils.others

import android.os.StrictMode
import java.net.InetAddress

object AppUtils {
    fun isInternetAvailable(): Boolean {
        return try {
            val TEST_URL = "www.google.com"
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val ip: InetAddress = InetAddress.getByName(TEST_URL)
            !ip.equals("")
        } catch (e: Exception) {
            false
        }
    }
}