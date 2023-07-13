package com.android.adminportal.utils.ktx

import android.text.TextUtils
import android.util.Patterns
import com.google.gson.Gson
import java.util.*

/**
 * To validate phone number.
 * */
fun String.isValidPhoneNumber(): Boolean {
    val phoneNumber = this.filter { it.isDigit() } // Remove any non-digit characters

    return if (phoneNumber.length == 10 && phoneNumber.all { it != '0' }) {
        Patterns.PHONE.matcher(phoneNumber).matches()
    } else false
}

/**
 * To validate email id.
 * */
fun String.isValidEmailAddress(): Boolean {
    val emailPattern = "^(?!.*?\\.\\.)[a-zA-Z0-9._-]+@(?!.*?\\.\\.)[a-zA-Z0-9-]+\\.[a-zA-Z]+$"
    return !TextUtils.isEmpty(this) && this.trim().matches(Regex(emailPattern))
}

/**
 * To decode string.
 * */
fun <T> String.decode(mapper: Class<T>): T {
    return Gson().fromJson(this, mapper)
}

/**
 * set to title case
 */
fun String.titleCase(): String {
    return this.lowercase().split(" ").joinToString(" ") { word ->
        word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }
}