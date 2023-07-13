package com.android.adminportal.data.local.MyPref

import android.content.Context
import android.content.SharedPreferences
import java.util.*


/**
 * The type App preference.
 */
object DataPreference {

    private const val PREFS_FILE_NAME = "local_preference"
    private var settings: SharedPreferences? = null

    /*init preference*/
    fun init(context: Context) {
        settings = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Add value.
     *
     * @param preferencesKey the preferences key
     * @param text the text
     */
    fun addValue(key: DataPreferenceKeys, text: String?) {
        val editor: SharedPreferences.Editor = settings!!.edit()
        editor.putString(key.name, text)
        editor.apply()
    }

    /**
     * Add value.
     *
     * @param preferencesKey the preferences key
     * @param value the list
     */
    fun addListValue(key: DataPreferenceKeys, value: MutableSet<String>?) {
        val editor: SharedPreferences.Editor = settings!!.edit()
        editor.putStringSet(key.name, value)
        editor.apply()
    }

    /**
     * Add boolean.
     *
     * @param id the preferences key
     * @param value the value
     */
    fun addBoolean(key: DataPreferenceKeys, value: Boolean?) {
        try {
            val editor: SharedPreferences.Editor = settings!!.edit()
            editor.putBoolean(key.name, value!!)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Add int.
     *
     * @param preferencesKey the preferences key
     * @param value the value
     */
    fun addInt(key: DataPreferenceKeys, value: Int?) {
        try {
            val editor: SharedPreferences.Editor = settings!!.edit()
            editor.putInt(key.name, value!!)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Gets value.
     *
     * @param preferencesKey the preferences key
     * @return the value
     */
    fun getValue(key: DataPreferenceKeys, java: Class<String>): String? {
        return settings!!.getString(key.name, "")
    }


    /**
     * Gets value.
     *
     * @param preferencesKey the preferences key
     * @return the value
     */
    fun getListValue(key: DataPreferenceKeys): MutableSet<String>? {
        return settings!!.getStringSet(key.name, setOf())
    }

    /**
     * Gets int.
     *
     * @param preferencesKey the preferences key
     * @return the int
     */
    fun getInt(key: DataPreferenceKeys): Int {
        return settings!!.getInt(key.name, 0)
    }

    /**
     * Add list.
     *
     * @param preferencesKey the preferences key
     * @param storedList     the stored list
     */
    fun addList(key: DataPreferenceKeys, storedList: ArrayList<String>?) {
        //Set the values
        val set: MutableSet<String> = HashSet()
        set.addAll(storedList!!)
        val editor: SharedPreferences.Editor = settings!!.edit()
        editor.putStringSet(key.name, set)
        editor.apply()
    }

    /**
     * Gets boolean.
     *
     * @param preferencesKey the preferences key
     * @return the boolean
     */
    fun getBoolean(key: DataPreferenceKeys): Boolean {
        return settings!!.getBoolean(key.name, false)
    }

    /**
     * Gets list.
     *
     * @param preferencesKey the preferences key
     * @return the list
     */
    fun getList(key: DataPreferenceKeys): Set<String>? {
        var set: Set<String>? = null
        try {
            //Retrieve the values
            set = settings!!.getStringSet(key.name, null)
            return set
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return set
    }

    /**
     * Clear shared preference.
     */
    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = settings!!.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * Remove value.
     *
     * @param preferencesKey the preferences key
     */
    fun removeValue(key: DataPreferenceKeys) {
        val editor: SharedPreferences.Editor = settings!!.edit()
        editor.putString(key.name, "")
        editor.apply()
    }

    /**
     * Remove value of list.
     *
     * @param preferencesKey the preferences key
     */
    fun removeValueOfList(key: DataPreferenceKeys) {
        val set: Set<String> = HashSet()
        val editor: SharedPreferences.Editor = settings!!.edit()
        editor.putStringSet(key.name, set)
        editor.apply()
    }
}