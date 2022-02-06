package space.w0lf.password.manager.base.android

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.w0lf.password.manager.R
import space.w0lf.password.manager.application.factory.ApplicationFactory

open class BaseActivity<VDB : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes private val layoutResId: Int,
    private val viewModelClass: Class<VM>,
) : AppCompatActivity()
{
    companion object {
        @JvmStatic
        protected val factory = ApplicationFactory()
    }
    
    protected lateinit var viewDataBinding: VDB
    protected lateinit var viewModel: VM
    
    protected var viewModelFactory: ViewModelProvider.Factory? = null
    
    private lateinit var loadingView: AlertDialog
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResId)
    
        viewModel = if (viewModelFactory != null) {
            ViewModelProvider(this, viewModelFactory!!).get(viewModelClass)
        } else {
            ViewModelProvider(this)[viewModelClass]
        }
        
        initView()
        initCollectors()
    
        lifecycleScope.launch(Dispatchers.IO) { viewModel.load() }
    }
    
    private fun initCollectors() {
        lifecycleScope.launch { viewModel.showLoadingView.collect {  onShowLoadingViewCollected(it) } }
    }
    
    private fun initView() {
        loadingView = AlertDialog.Builder(this, R.style.DialogWithCustomView)
            .setView(R.layout.view_loading)
            .create()
    }
    
    private fun onShowLoadingViewCollected(showLoadingView: Boolean) {
        if (showLoadingView) {
            if (!loadingView.isShowing) {
                loadingView.show()
            }
        } else {
            loadingView.dismiss()
        }
    }
    
}
