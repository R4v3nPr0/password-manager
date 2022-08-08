package space.w0lf.password.manager.ui.showPassword

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import space.w0lf.password.manager.R
import space.w0lf.password.manager.base.android.BaseActivity
import space.w0lf.password.manager.databinding.ActivityShowPasswordBinding
import space.w0lf.password.manager.domain.model.Password

class ShowPasswordActivity : BaseActivity<ActivityShowPasswordBinding, ShowPasswordViewModel>(
    R.layout.activity_show_password,
    ShowPasswordViewModel::class.java
)
{
    
    companion object {
        const val EXTRA_PASSWORD = "EXTRA_PASSWORD"
    }
    
    private val copyUsernameImageButtonOnClickListener = View.OnClickListener {
        (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).run {
            setPrimaryClip(
                ClipData.newPlainText(
                    "",
                    viewModel.password.value!!.username
                )
            )
        }
    
        Snackbar
            .make(
                viewDataBinding.root,
                R.string.message_username_copied,
                Snackbar.LENGTH_LONG
            )
            .show()
    }
    
    private val copyPasswordImageButtonOnClickListener = View.OnClickListener {
        (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).run {
            setPrimaryClip(
                ClipData.newPlainText(
                    "",
                    viewModel.password.value!!.password
                )
            )
        }
    
        Snackbar
            .make(
                viewDataBinding.root,
                R.string.message_password_copied,
                Snackbar.LENGTH_LONG
            )
            .show()
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        viewModel.setPassword(intent.extras!!.getSerializable(EXTRA_PASSWORD) as Password)
        
        initView()
        initListeners()
    }
    
    private fun initListeners() {
        viewDataBinding.run {
            copyUsernameImageButton.setOnClickListener(copyUsernameImageButtonOnClickListener)
            copyPasswordImageButton.setOnClickListener(copyPasswordImageButtonOnClickListener)
        }
    }
    
    private fun initView() {
        viewDataBinding.apply {
            name = viewModel.password.value!!.name
            notes = viewModel.password.value!!.notes
            password = viewModel.password.value!!.password
            status = viewModel.password.value!!.status
            url = viewModel.password.value!!.url
            username = viewModel.password.value!!.username
        }
    }
    
}
