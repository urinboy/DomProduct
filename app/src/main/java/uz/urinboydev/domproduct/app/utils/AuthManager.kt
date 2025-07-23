package uz.urinboydev.domproduct.app.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.activities.LoginActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(private val preferenceManager: PreferenceManager) {

    private val GUEST_MODE_KEY = "guest_mode"

    /**
     * Foydalanuvchi login qilganmi?
     */
    fun isLoggedIn(): Boolean {
        return preferenceManager.isLoggedIn()
    }

    /**
     * Foydalanuvchi mehmon sifatida kirishni tanlaganmi?
     */
    fun isGuest(): Boolean {
        return preferenceManager.getAppSettings(GUEST_MODE_KEY, false)
    }

    /**
     * Mehmon sifatida kirish
     */
    fun loginAsGuest(context: Context) {
        preferenceManager.saveAppSettings(GUEST_MODE_KEY, true)
        preferenceManager.clearToken() // Mehmon bo'lganda token bo'lmasligi kerak
        preferenceManager.clearUser() // Mehmon bo'lganda user ma'lumotlari bo'lmasligi kerak
    }

    /**
     * Authentication holati
     */
    fun getAuthState(): AuthState {
        return when {
            isLoggedIn() -> AuthState.LOGGED_IN
            isGuest() -> AuthState.GUEST
            else -> AuthState.NOT_AUTHENTICATED
        }
    }

    /**
     * Login talab qiladigan funksiya uchun check
     * @return true agar login kerak bo'lsa
     */
    fun requireLogin(): Boolean {
        return !isLoggedIn()
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
    @SuppressLint("StringFormatInvalid")
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
                // RegisterActivity ga yo'naltirish
                val intent = Intent(context, LoginActivity::class.java)
                intent.putExtra("show_register", true) // RegisterActivity ga o'tish uchun flag
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
    fun logout(localCartManager: LocalCartManager) {
        preferenceManager.logout() // Token va user ma'lumotlarini tozalash
        preferenceManager.saveAppSettings(GUEST_MODE_KEY, false) // Mehmon rejimini o'chirish
        // Savatni ham tozalash (agar server savati bo'lsa, avval sinxronizatsiya qilish kerak)
        localCartManager.clearCart()
    }

    /**
     * Guest user uchun permission check
     */
    fun hasPermission(permission: Permission): Boolean {
        return when (permission) {
            Permission.VIEW_PRODUCTS -> true
            Permission.VIEW_CATEGORIES -> true
            Permission.SEARCH -> true
            Permission.LOCAL_CART -> true
            Permission.CHECKOUT -> isLoggedIn()
            Permission.PROFILE -> isLoggedIn()
            Permission.ORDERS_HISTORY -> isLoggedIn()
            Permission.WRITE_REVIEWS -> isLoggedIn()
            Permission.SAVE_FAVORITES -> isLoggedIn()
            Permission.SYNC_DATA -> isLoggedIn()
        }
    }

    /**
     * Feature access check with automatic dialog
     */
    fun checkFeatureAccess(context: Context, permission: Permission, feature: String): Boolean {
        if (hasPermission(permission)) {
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