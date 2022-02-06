package space.w0lf.password.manager.data.dao.impl

import space.w0lf.password.manager.application.registry.ApplicationRegistry
import space.w0lf.password.manager.data.dao.PasswordDao
import space.w0lf.password.manager.data.dao.android.room.converter.PasswordConverter
import space.w0lf.password.manager.data.dao.android.room.crypto.PasswordCipher
import space.w0lf.password.manager.data.dao.android.room.database.PasswordManagerDatabase
import space.w0lf.password.manager.domain.model.Password

class PasswordDaoImpl() : PasswordDao {
    
    private val cipher: PasswordCipher = ApplicationRegistry.passwordCipher
    private val converter: PasswordConverter = PasswordConverter()
    private val db: PasswordManagerDatabase = ApplicationRegistry.passwordManagerDatabase
    
    override fun deletePassword(password: Password) {
        db.passwordDao().delete(converter.convert(password))
    }
    
    override fun getPasswords(): List<Password> =
        db.passwordDao().getAll().map {
            converter.convert(cipher.decrypt(it))
        }
    
    override fun savePassword(password: Password, isPasswordUpdate: Boolean) {
        val encryptedPassword = cipher.encrypt(converter.convert(password))
        
        if (isPasswordUpdate) {
            db.passwordDao().update(encryptedPassword)
        } else {
            db.passwordDao().insert(encryptedPassword)
        }
    }
    
}
