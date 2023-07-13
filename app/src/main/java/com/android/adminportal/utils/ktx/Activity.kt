package com.android.adminportal.utils.ktx

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.android.adminportal.R

fun AppCompatActivity.launchActivityRTL(cls: Class<*>?) {
    startActivity(Intent(this, cls))
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

fun AppCompatActivity.launchActivityForResultRTL(
    resultActivity: ActivityResultLauncher<Intent>, cls: Class<*>?
) {
    resultActivity.launch(Intent(this, cls))
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

fun AppCompatActivity.launchActivityWithClearTaskRTL(cls: Class<*>?) {
    val mainIntent = Intent(this, cls)
    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    startActivity(mainIntent)
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

fun AppCompatActivity.launchActivityWithDataRTL(cls: Class<*>?, bundle: Bundle?) {
    val mainIntent = Intent(this, cls)
    mainIntent.putExtras(bundle!!)
    startActivity(mainIntent)
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

fun AppCompatActivity.launchActivityForResultWithDataRTL(
    resultActivity: ActivityResultLauncher<Intent>, cls: Class<*>?, bundle: Bundle?
) {
    val mainIntent = Intent(this, cls)
    mainIntent.putExtras(bundle!!)
    resultActivity.launch(mainIntent)
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}

fun AppCompatActivity.launchActivityTTB(cls: Class<*>?) {
    startActivity(Intent(this, cls))
    overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out)
}

fun AppCompatActivity.launchActivityWithClearTaskTTB(cls: Class<*>?) {
    val mainIntent = Intent(this, cls)
    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    startActivity(mainIntent)
    overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out)
}


fun AppCompatActivity.launchActivityForResultTTB(
    resultActivity: ActivityResultLauncher<Intent>, cls: Class<*>?
) {
    resultActivity.launch(Intent(this, cls))
    overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out)
}


fun AppCompatActivity.launchActivityWithDataTTB(cls: Class<*>?, bundle: Bundle?) {
    val mainIntent = Intent(this, cls)
    mainIntent.putExtras(bundle!!)
    startActivity(mainIntent)
    overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out)
}

fun AppCompatActivity.launchActivityForResultWithDataTTB(
    resultActivity: ActivityResultLauncher<Intent>, cls: Class<*>?, bundle: Bundle?
) {
    val mainIntent = Intent(this, cls)
    mainIntent.putExtras(bundle!!)
    resultActivity.launch(mainIntent)
    overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out)
}

fun AppCompatActivity.launchActivityWithSharedTransitionAndData(
    cls: Class<*>?, view: View, transitionName: String, bundle: Bundle?
) {
    val mainIntent = Intent(this, cls)
    mainIntent.putExtras(bundle!!)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
        this, view, transitionName
    )
    startActivity(mainIntent, options.toBundle())
}

fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}