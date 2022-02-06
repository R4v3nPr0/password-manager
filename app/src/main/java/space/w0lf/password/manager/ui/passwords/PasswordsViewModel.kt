package space.w0lf.password.manager.ui.passwords

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import space.w0lf.password.manager.base.android.BaseViewModel
import space.w0lf.password.manager.domain.model.Password
import space.w0lf.password.manager.domain.useCase.DeletePasswordUseCase
import space.w0lf.password.manager.domain.useCase.GetPasswordsUseCase

class PasswordsViewModel(
    private val deletePasswordUseCase: DeletePasswordUseCase,
    private val getPasswordsUseCase: GetPasswordsUseCase
) : BaseViewModel() {
    
    class Factory(
        private val deletePasswordUseCase: DeletePasswordUseCase,
        private val getPasswordsUseCase: GetPasswordsUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            PasswordsViewModel(deletePasswordUseCase, getPasswordsUseCase) as T
    }
    
    private val _mutablePasswords = mutableListOf<Password>()
    private val _mutablePasswordsTemp = mutableListOf<Password>()
    
    private val _passwordDeleted = MutableSharedFlow<Int>()
    val passwordDeleted = _passwordDeleted.asSharedFlow()
    
    private val _passwords = MutableStateFlow<List<Password>>(_mutablePasswords)
    val passwords = _passwords.asStateFlow()
    
    private val _showActionsView = MutableSharedFlow<Int>()
    val showActionsView = _showActionsView.asSharedFlow()
    
    private val _showAddPasswordView = MutableSharedFlow<Any?>()
    val showAddPasswordView = _showAddPasswordView.asSharedFlow()
    
    private val _showConfirmDeletePasswordView = MutableSharedFlow<Int>()
    val showConfirmDeletePasswordView = _showConfirmDeletePasswordView.asSharedFlow()
    
    private val _showEditPasswordView = MutableSharedFlow<Int>()
    val showEditPasswordView = _showEditPasswordView.asSharedFlow()
    
    private val _showPassword = MutableSharedFlow<Int>()
    val showPassword = _showPassword.asSharedFlow()
    
    private val _showPasswords = MutableSharedFlow<Any?>()
    val showPasswords = _showPasswords.asSharedFlow()
    
    override suspend fun load() {
        super.load()
        
        _showLoadingView.emit(true)
        
        val getPasswordsOutput = getPasswordsUseCase.run(GetPasswordsUseCase.Input())
        
        delay(1000)
        
        _showLoadingView.emit(false)
        
        if (getPasswordsOutput.result.isSuccess) {
            _mutablePasswords.clear()
            _mutablePasswordsTemp.clear()
            
            _mutablePasswordsTemp.addAll(
                getPasswordsOutput.result.value!!.sortedBy { it.name }
            )
            _mutablePasswords.addAll(_mutablePasswordsTemp)
            
            _showPasswords.emit(null)
        }
    }
    
    suspend fun actionsClicked(position: Int) {
        _showActionsView.emit(position)
    }
    
    suspend fun addClicked() {
        _showAddPasswordView.emit(null)
    }
    
    suspend fun confirmDeleteClicked(passwordPosition: Int) {
        _showLoadingView.emit(true)
    
        deletePasswordUseCase.run(DeletePasswordUseCase.Input(passwords.value[passwordPosition]))
    
        _mutablePasswords.removeAt(passwordPosition)
        _mutablePasswordsTemp.removeAt(passwordPosition)
    
        delay(1000)
    
        _showLoadingView.emit(false)
        _passwordDeleted.emit(passwordPosition)
    }
    
    suspend fun deleteClicked(passwordPosition: Int) {
       _showConfirmDeletePasswordView.emit(passwordPosition)
    }
    
    suspend fun editClicked(position: Int) {
        _showEditPasswordView.emit(position)
    }
    
    suspend fun queryTextSubmitted(query: String) {
        _mutablePasswords.apply {
            clear()
            addAll(
                _mutablePasswordsTemp.filter {
                    it.name.contains(query, true)
                }
            )
        }
        _showPasswords.emit(null)
    }
    
    suspend fun searchViewClosed() {
        _mutablePasswords.apply {
            clear()
            addAll(_mutablePasswordsTemp)
        }
        _showPasswords.emit(null)
    }
    
    suspend fun showClicked(position: Int) {
        _showPassword.emit(position)
    }
    
}
