package uz.urinboydev.domproduct.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import uz.urinboydev.domproduct.app.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(private val context: Context) {

    private val PREF_NAME = "DomProductPrefs"
    private val PRIVATE_MODE = Context.MODE_PRIVATE

    private val KEY_TOKEN = "token"
    private val KEY_USER = "user"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor: SharedPreferences.Editor = prefs.edit()
    private val gson = Gson()

    fun saveToken(token: String?) {
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUser(user: User?) {
        if (user != null) {
            editor.putString(KEY_USER, gson.toJson(user))
        } else {
            editor.remove(KEY_USER)
        }
        editor.apply()
    }

    fun getUser(): User? {
        val userJson = prefs.getString(KEY_USER, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null && getUser() != null
    }

    fun saveAppSettings(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getAppSettings(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun logout() {
        editor.remove(KEY_TOKEN)
        editor.remove(KEY_USER)
        editor.apply()
    }
}