package space.w0lf.password.manager.data.dao.android.content

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SharedPreferencesHelper(context: Context) {
    
    companion object {
        const val KEY_MASTER_PASSWORD = "master_password"
        
        private const val SHARED_PREFERENCES_FILE_NAME = "password_manager_shared_preferences"
    }
    
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        SHARED_PREFERENCES_FILE_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
    
    fun getString(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }
    
    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }
    
}
