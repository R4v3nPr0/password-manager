package space.w0lf.password.manager.ui.authenticate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import space.w0lf.password.manager.base.android.BaseViewModel
import space.w0lf.password.manager.domain.useCase.AuthenticateUseCase
import space.w0lf.password.manager.domain.useCase.StoreCredentialsUseCase
import space.w0lf.password.manager.domain.useCase.ValidateUserCredentialsUseCase

class AuthenticateViewModel(
    private val authenticateUseCase: AuthenticateUseCase,
    private val storeCredentialsUseCase: StoreCredentialsUseCase,
    private val validateUserCredentialsUseCase: ValidateUserCredentialsUseCase
) : BaseViewModel() {
    
    class Factory(
        private val authenticateUseCase: AuthenticateUseCase,
        private val storeCredentialsUseCase: StoreCredentialsUseCase,
        private val validateUserCredentialsUseCase: ValidateUserCredentialsUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthenticateViewModel(
                authenticateUseCase,
                storeCredentialsUseCase,
                validateUserCredentialsUseCase
            ) as T
        }
    }
    
    companion object {
        private const val CONFIRMED_MASTER_PASSWORD_INITIAL_STATE = ""
        private const val MASTER_PASSWORD_INITIAL_STATE = ""
    }
    
    private val _confirmedMasterPassword = MutableStateFlow(CONFIRMED_MASTER_PASSWORD_INITIAL_STATE)
    val confirmedMasterPassword = _confirmedMasterPassword.asStateFlow()
    
    private val _masterPassword = MutableStateFlow(MASTER_PASSWORD_INITIAL_STATE)
    val masterPassword = _masterPassword.asStateFlow()
    
    private val _showAuthenticationError = MutableSharedFlow<String>()
    val showAuthenticationError = _showAuthenticationError.asSharedFlow()
    
    private val _showEmptyMasterPasswordError = MutableSharedFlow<Any?>()
    val showEmptyMasterPasswordError = _showEmptyMasterPasswordError.asSharedFlow()
    
    private val _showInvalidMasterPasswordError = MutableSharedFlow<Any?>()
    val showInvalidMasterPasswordError = _showInvalidMasterPasswordError.asSharedFlow()
    
    private val _showPasswordsView = MutableSharedFlow<Any?>()
    val showPasswordsView = _showPasswordsView.asSharedFlow()
    
    override suspend fun load() {
        super.load()
        
        _showLoadingView.emit(true)
        
        if (
            validateUserCredentialsUseCase
                .run(ValidateUserCredentialsUseCase.Input())
                .isUserCredentialsSaved
        ) {
            val authenticationOutput = authenticateUseCase.run(AuthenticateUseCase.Input())
            
            _showLoadingView.emit(false)
            
            if (authenticationOutput.result.isSuccess && authenticationOutput.result.value!!) {
                _showPasswordsView.emit(null)
            } else {
                _showAuthenticationError.emit(authenticationOutput.result.exception!!.message!!)
            }
        } else {
            _showLoadingView.emit(false)
        }
    }
    
    suspend fun authenticateClicked() {
        if (isMasterPasswordNotEmpty()) {
            if (isMasterPasswordValid()) {
                _showLoadingView.emit(true)
                storeCredentialsUseCase.run(StoreCredentialsUseCase.Input(masterPassword.value))
                _showLoadingView.emit(false)
                _showPasswordsView.emit(null)
            } else {
                _showInvalidMasterPasswordError.emit(null)
            }
        } else {
            _showEmptyMasterPasswordError.emit(null)
        }
    }
    
    fun setConfirmedMasterPassword(confirmedMasterPassword: String) {
        _confirmedMasterPassword.update { confirmedMasterPassword }
    }
    
    fun setMasterPassword(masterPassword: String) {
        _masterPassword.update { masterPassword }
    }
    
    private fun isMasterPasswordNotEmpty(): Boolean =
        confirmedMasterPassword.value.isNotEmpty() && masterPassword.value.isNotEmpty()
    
    private fun isMasterPasswordValid(): Boolean =
        confirmedMasterPassword.value == masterPassword.value
    
}
