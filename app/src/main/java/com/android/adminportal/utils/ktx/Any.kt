package com.android.adminportal.utils.ktx

import com.google.gson.Gson

fun Any.encode(mapper: Class<*>): String {
    return Gson().toJson(this, mapper)
}