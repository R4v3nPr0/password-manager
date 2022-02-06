package space.w0lf.password.manager.base.android

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

open class BaseViewModel : ViewModel() {
    
    companion object {
        protected const val SHOW_LOADING_VIEW_INITIAL_STATE = false
    }
    
    protected val _showLoadingView = MutableStateFlow(SHOW_LOADING_VIEW_INITIAL_STATE)
    val showLoadingView = _showLoadingView.asStateFlow()
    
    open suspend fun load() {
    
    }
    
}
