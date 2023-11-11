package com.rival.tutorialloginregist

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val USERNAME = "username"
    }

    fun createLoginSession() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            editor.putBoolean(IS_LOGGED_IN, true)
            editor.putString(USERNAME, user.displayName)
            editor.apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(USERNAME, null)
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}
