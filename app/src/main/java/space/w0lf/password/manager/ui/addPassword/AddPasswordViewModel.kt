package space.w0lf.password.manager.ui.addPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import space.w0lf.password.manager.base.android.BaseViewModel
import space.w0lf.password.manager.domain.model.Password
import space.w0lf.password.manager.domain.useCase.SavePasswordUseCase

class AddPasswordViewModel(private val savePasswordUseCase: SavePasswordUseCase) : BaseViewModel() {
    
    class Factory(private val savePasswordUseCase: SavePasswordUseCase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddPasswordViewModel(savePasswordUseCase) as T
        }
    }
    
    companion object {
        private const val CONFIRMED_PASSWORD_INITIAL_STATE = ""
        private const val ID_INITIAL_STATE = 0
        private const val IS_PASSWORD_UPDATE_INITIAL_STATE = false
        private const val NAME_INITIAL_STATE = ""
        private const val NOTES_INITIAL_STATE = ""
        private const val PASSWORD_INITIAL_STATE = ""
        private const val SELECTED_STATUS = -1
        private const val URL_INITIAL_STATE = ""
        private const val USERNAME_INITIAL_STATE = ""
    }
    
    private val _confirmedPassword = MutableStateFlow(CONFIRMED_PASSWORD_INITIAL_STATE)
    val confirmedPassword = _confirmedPassword.asStateFlow()
    
    private val _id = MutableStateFlow(ID_INITIAL_STATE)
    val id = _id.asStateFlow()
    
    private val _isPasswordUpdate = MutableStateFlow(IS_PASSWORD_UPDATE_INITIAL_STATE)
    val isPasswordUpdate = _isPasswordUpdate.asStateFlow()
    
    private val _name = MutableStateFlow(NAME_INITIAL_STATE)
    val name = _name.asStateFlow()
    
    private val _notes = MutableStateFlow(NOTES_INITIAL_STATE)
    val notes = _notes.asStateFlow()
    
    private val _password = MutableStateFlow(PASSWORD_INITIAL_STATE)
    val password = _password.asStateFlow()
    
    private val _url = MutableStateFlow(URL_INITIAL_STATE)
    val url = _url.asStateFlow()
    
    private val _username = MutableStateFlow(USERNAME_INITIAL_STATE)
    val username = _username.asStateFlow()
    
    private val _selectedStatus = MutableStateFlow(SELECTED_STATUS)
    val selectedStatus = _selectedStatus.asStateFlow()
    
    private val _showInvalidNameError = MutableSharedFlow<Any?>()
    val showInvalidNameError = _showInvalidNameError.asSharedFlow()
    
    private val _showInvalidPasswordError = MutableSharedFlow<Any?>()
    val showInvalidPasswordError = _showInvalidPasswordError.asSharedFlow()
    
    private val _showInvalidStatusError = MutableSharedFlow<Any?>()
    val showInvalidStatusError = _showInvalidStatusError.asSharedFlow()
    
    private val _showPasswordSaveError = MutableSharedFlow<Any?>()
    val showPasswordSaveError = _showPasswordSaveError.asSharedFlow()
    
    private val _showPasswordSaveSuccess = MutableSharedFlow<Any?>()
    val showPasswordSaveSuccess = _showPasswordSaveSuccess.asSharedFlow()
    
    suspend fun saveClicked() {
        if (isDataValid()) {
            val status = if (selectedStatus.value == 0) {
                "ACTIVE"
            } else {
                "INACTIVE"
            }
            
            val savePasswordOutput = savePasswordUseCase.run(SavePasswordUseCase.Input(
                Password(
                    id.value,
                    name.value,
                    notes.value,
                    password.value,
                    status,
                    url.value,
                    username.value
                ),
                isPasswordUpdate.value
            ))
            
            if (savePasswordOutput.result.isSuccess && savePasswordOutput.result.value!!) {
                _showPasswordSaveSuccess.emit(null)
            } else {
                _showPasswordSaveError.emit(null)
            }
        }
    }
    
    fun setConfirmedPassword(confirmedPassword: String) {
        _confirmedPassword.update { confirmedPassword }
    }
    
    fun setId(id: Int) {
        _id.update { id }
    }
    
    fun setIsPasswordUpdate(isPasswordUpdate: Boolean) {
        _isPasswordUpdate.update { isPasswordUpdate }
    }
    
    fun setName(name: String) {
        _name.update { name }
    }
    
    fun setNotes(notes: String) {
        _notes.update { notes }
    }
    
    fun setPassword(password: String) {
        _password.update { password }
    }
    
    fun setUrl(url: String) {
        _url.update { url }
    }
    
    fun setUsername(username: String) {
        _username.update { username }
    }
    
    fun setSelectedStatus(selectedStatus: Int) {
        _selectedStatus.update { selectedStatus }
    }
    
    private suspend fun isDataValid(): Boolean {
        if (!isNameValid()) {
            _showInvalidNameError.emit(null)
            return false
        }
    
        if (!isPasswordValid()) {
            _showInvalidPasswordError.emit(null)
            return false
        }
        
        if (!isStatusValid()) {
            _showInvalidStatusError.emit(null)
            return false
        }
        
        return true
    }
    
    private fun isNameValid(): Boolean {
        return name.value.isNotEmpty()
    }
    
    private fun isPasswordValid(): Boolean {
        return confirmedPassword.value == password.value
    }
    
    private fun isStatusValid(): Boolean {
        return selectedStatus.value != -1
    }
    
}
