package space.w0lf.password.manager.domain.authentication

import space.w0lf.password.manager.application.exception.AuthenticationException
import space.w0lf.password.manager.util.Result

interface Authenticator {
    
    fun authenticate(): Result<Boolean, AuthenticationException>
    fun saveCredentials(masterPassword: String)
    fun validateUserCredentials(): Boolean
    
}
