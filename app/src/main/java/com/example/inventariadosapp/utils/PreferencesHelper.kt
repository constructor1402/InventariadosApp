package com.example.inventariadosapp.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREF_NAME = "InventariadosPrefs"
    private const val KEY_USER_NAME = "nombreActivo"

    fun guardarNombreActivo(context: Context, nombre: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_NAME, nombre).apply()
    }

    fun obtenerNombreActivo(context: Context): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_NAME, null)
    }
}
