package space.w0lf.password.manager.data.dao

import space.w0lf.password.manager.domain.model.Password

interface PasswordDao {
    
    fun deletePassword(password: Password)
    fun getPasswords(): List<Password>
    fun savePassword(password: Password, isPasswordUpdate: Boolean)
    
}
