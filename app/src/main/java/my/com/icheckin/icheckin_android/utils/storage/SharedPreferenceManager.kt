package my.com.icheckin.icheckin_android.utils.storage

import android.content.Context

/**
 * Created by gaara on 2/2/18.
 */
object SharedPreferenceManager {

    private val PREF_NAME = "icheckin"

    fun putString(context: Context, key: String, value: String) {
        val pref = context.getSharedPreferences(PREF_NAME, 0)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(context: Context, key: String): String {
        val pref = context.getSharedPreferences(PREF_NAME, 0)
        return pref.getString(key, "")
    }
}