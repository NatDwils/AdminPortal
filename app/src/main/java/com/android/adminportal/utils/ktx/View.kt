package com.android.adminportal.utils.ktx

import android.view.View


/**
 * Method for view visible..
 * */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * Method for view invisible..
 * */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * Method for view gone..
 * */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Conditional check for Visible and Invisible
 * */
fun View.setVisibleInvisible(visibility: Boolean? = false) {
    this.visibility = if (visibility == true) View.VISIBLE else View.INVISIBLE
}

/**
 * Conditional check for Visible and Gone
 * */
fun View.setVisibleGone(visibility: Boolean? = false) {
    this.visibility = if (visibility == true) View.VISIBLE else View.GONE
}

