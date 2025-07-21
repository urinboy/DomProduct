package uz.urinboydev.domproduct.app.utils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.activities.LoginActivity

object AuthManager {

    private const val GUEST_MODE_KEY = "guest_mode"

    /**
     * Foydalanuvchi login qilganmi?
     */
    fun isLoggedIn(context: Context): Boolean {
        val preferenceManager = PreferenceManager(context)
        return preferenceManager.isLoggedIn()
    }

    /**
     * Foydalanuvchi mehmon sifatida kirishni tanlaganmi?
     */
    fun isGuest(context: Context): Boolean {
        val preferenceManager = PreferenceManager(context)
        return preferenceManager.getAppSettings(GUEST_MODE_KEY, false)
    }

    /**
     * Mehmon sifatida kirish
     */
    fun loginAsGuest(context: Context) {
        val preferenceManager = PreferenceManager(context)
        preferenceManager.saveAppSettings(GUEST_MODE_KEY, true)
    }

    /**
     * Authentication holati
     */
    fun getAuthState(context: Context): AuthState {
        return when {
            isLoggedIn(context) -> AuthState.LOGGED_IN
            isGuest(context) -> AuthState.GUEST
            else -> AuthState.NOT_AUTHENTICATED
        }
    }

    /**
     * Login talab qiladigan funksiya uchun check
     * @return true agar login kerak bo'lsa
     */
    fun requireLogin(context: Context): Boolean {
        return !isLoggedIn(context)
    }

    /**
     * Login dialog ko'rsatish
     */
    fun showLoginDialog(context: Context, message: String? = null): AlertDialog {
        val dialogMessage = message ?: context.getString(R.string.login_required_message)

        return AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.login_required))
            .setMessage(dialogMessage)
            .setPositiveButton(context.getString(R.string.login_button)) { _, _ ->
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
            .setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * Login prompt dialog (benefits bilan)
     */
    fun showLoginPrompt(context: Context, feature: String): AlertDialog {
        val message = context.getString(R.string.login_prompt_message, feature)

        return AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.unlock_features))
            .setMessage(message)
            .setPositiveButton(context.getString(R.string.login_button)) { _, _ ->
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
            .setNeutralButton(context.getString(R.string.register_button)) { _, _ ->
                // Navigate to RegisterActivity
                val intent = Intent(context, LoginActivity::class.java)
                intent.putExtra("show_register", true)
                context.startActivity(intent)
            }
            .setNegativeButton(context.getString(R.string.continue_as_guest)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    /**
     * Logout (guest mode ham, login ham)
     */
    fun logout(context: Context) {
        val preferenceManager = PreferenceManager(context)
        preferenceManager.logout()
        preferenceManager.saveAppSettings(GUEST_MODE_KEY, false)
    }

    /**
     * Guest user uchun permission check
     */
    fun hasPermission(context: Context, permission: Permission): Boolean {
        return when (permission) {
            Permission.VIEW_PRODUCTS -> true
            Permission.VIEW_CATEGORIES -> true
            Permission.SEARCH -> true
            Permission.LOCAL_CART -> true
            Permission.CHECKOUT -> isLoggedIn(context)
            Permission.PROFILE -> isLoggedIn(context)
            Permission.ORDERS_HISTORY -> isLoggedIn(context)
            Permission.WRITE_REVIEWS -> isLoggedIn(context)
            Permission.SAVE_FAVORITES -> isLoggedIn(context)
            Permission.SYNC_DATA -> isLoggedIn(context)
        }
    }

    /**
     * Feature access check with automatic dialog
     */
    fun checkFeatureAccess(context: Context, permission: Permission, feature: String): Boolean {
        if (hasPermission(context, permission)) {
            return true
        }

        showLoginPrompt(context, feature)
        return false
    }
}

/**
 * Authentication states
 */
enum class AuthState {
    LOGGED_IN,      // Token bilan login qilgan
    GUEST,          // Mehmon sifatida
    NOT_AUTHENTICATED  // Hech qanday state yo'q
}

/**
 * Permission types
 */
enum class Permission {
    VIEW_PRODUCTS,
    VIEW_CATEGORIES,
    SEARCH,
    LOCAL_CART,
    CHECKOUT,
    PROFILE,
    ORDERS_HISTORY,
    WRITE_REVIEWS,
    SAVE_FAVORITES,
    SYNC_DATA
}