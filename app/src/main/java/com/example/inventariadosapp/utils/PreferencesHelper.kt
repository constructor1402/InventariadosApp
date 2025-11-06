package com.example.inventariadosapp.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {
    private const val PREF_NAME = "InventariadosPrefs"
    private const val KEY_USER_NAME = "nombreActivo"

    // 🔹 Guarda el nombre activo del usuario
    fun guardarNombreActivo(context: Context, nombre: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_USER_NAME, nombre.trim()).apply()
    }

    // 🔹 Obtiene el nombre activo del usuario (o null si no existe)
    fun obtenerNombreActivo(context: Context): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_NAME, null)
    }

    // 🔹 Obtiene el nombre activo de forma segura (si no hay, devuelve vacío)
    fun obtenerNombreActivoSeguro(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_USER_NAME, "") ?: ""
    }

    // 🔹 Limpia el usuario activo (por ejemplo, al cerrar sesión)
    fun limpiarUsuarioActivo(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_USER_NAME).apply()
    }
}
