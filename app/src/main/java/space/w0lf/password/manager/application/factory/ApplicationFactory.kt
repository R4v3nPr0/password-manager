package space.w0lf.password.manager.application.factory

import androidx.fragment.app.FragmentActivity
import space.w0lf.password.manager.data.dao.PasswordDao
import space.w0lf.password.manager.data.dao.impl.PasswordDaoImpl
import space.w0lf.password.manager.domain.authentication.Authenticator
import space.w0lf.password.manager.domain.authentication.impl.AuthenticatorImpl
import space.w0lf.password.manager.domain.useCase.SavePasswordUseCase
import space.w0lf.password.manager.domain.useCase.AuthenticateUseCase
import space.w0lf.password.manager.domain.useCase.DeletePasswordUseCase
import space.w0lf.password.manager.domain.useCase.GetPasswordsUseCase
import space.w0lf.password.manager.domain.useCase.StoreCredentialsUseCase
import space.w0lf.password.manager.domain.useCase.ValidateUserCredentialsUseCase
import space.w0lf.password.manager.domain.useCase.impl.SavePasswordUseCaseImpl
import space.w0lf.password.manager.domain.useCase.impl.AuthenticateUseCaseImpl
import space.w0lf.password.manager.domain.useCase.impl.DeletePasswordUseCaseImpl
import space.w0lf.password.manager.domain.useCase.impl.GetPasswordsUseCaseImpl
import space.w0lf.password.manager.domain.useCase.impl.StoreCredentialsUseCaseImpl
import space.w0lf.password.manager.domain.useCase.impl.ValidateUserCredentialsUseCaseImpl

class ApplicationFactory {
    
    fun createAuthenticateUseCase(authenticator: Authenticator) : AuthenticateUseCase =
        AuthenticateUseCaseImpl(authenticator)
    
    fun createAuthenticator(fragmentActivity: FragmentActivity): Authenticator =
        AuthenticatorImpl(fragmentActivity)
    
    fun createDeletePasswordUseCase(passwordDao: PasswordDao): DeletePasswordUseCase =
        DeletePasswordUseCaseImpl(passwordDao)
    
    fun createGetPasswordsUseCase(passwordDao: PasswordDao): GetPasswordsUseCase =
        GetPasswordsUseCaseImpl(passwordDao)
    
    fun createPasswordDao(): PasswordDao =
        PasswordDaoImpl()
    
    fun createSavePasswordUseCase(passwordDao: PasswordDao): SavePasswordUseCase =
        SavePasswordUseCaseImpl(passwordDao)
    
    fun createStoreCredentialsUseCase(authenticator: Authenticator): StoreCredentialsUseCase =
        StoreCredentialsUseCaseImpl(authenticator)
    
    fun createValidateUserCredentialsUseCase(authenticator: Authenticator): ValidateUserCredentialsUseCase =
        ValidateUserCredentialsUseCaseImpl(authenticator)
    
}
