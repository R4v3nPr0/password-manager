package space.w0lf.password.manager.application.registry

import space.w0lf.password.manager.data.dao.android.room.crypto.PasswordCipher
import space.w0lf.password.manager.data.dao.android.room.database.PasswordManagerDatabase

object ApplicationRegistry {
    
    lateinit var passwordCipher: PasswordCipher
    lateinit var passwordManagerDatabase: PasswordManagerDatabase
    
}
