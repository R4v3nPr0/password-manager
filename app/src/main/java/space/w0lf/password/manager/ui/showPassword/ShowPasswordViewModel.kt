package space.w0lf.password.manager.ui.showPassword

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import space.w0lf.password.manager.base.android.BaseViewModel
import space.w0lf.password.manager.domain.model.Password

class ShowPasswordViewModel : BaseViewModel() {
    
    private val _password = MutableStateFlow<Password?>(null)
    val password = _password.asStateFlow()
    
    fun setPassword(password: Password) {
        _password.update { password  }
    }
    
}
