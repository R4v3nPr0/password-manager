package space.w0lf.password.manager.ui.authenticate

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.w0lf.password.manager.R
import space.w0lf.password.manager.base.android.BaseActivity
import space.w0lf.password.manager.databinding.ActivityAuthenticateBinding
import space.w0lf.password.manager.ui.passwords.PasswordsActivity

class AuthenticateActivity : BaseActivity<ActivityAuthenticateBinding, AuthenticateViewModel>(
    R.layout.activity_authenticate,
    AuthenticateViewModel::class.java
) {
    
    private val authenticateOnClickListener = View.OnClickListener {
        lifecycleScope.launch(Dispatchers.IO) { viewModel.authenticateClicked() }
    }
    
    private val confirmedMasterPasswordTextWatcher = object : TextWatcher {
        
        override fun afterTextChanged(s: Editable?) {
            return
        }
    
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }
    
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            lifecycleScope.launch(Dispatchers.IO) { viewModel.setConfirmedMasterPassword(s.toString()) }
        }
        
    }
    
    private val masterPasswordTextWatcher = object : TextWatcher {
        
        override fun afterTextChanged(s: Editable?) {
            return
        }
        
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }
        
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            lifecycleScope.launch(Dispatchers.IO) { viewModel.setMasterPassword(s.toString()) }
        }
        
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val authenticator = factory.createAuthenticator(this)
        
        viewModelFactory = AuthenticateViewModel.Factory(
            factory.createAuthenticateUseCase(authenticator),
            factory.createStoreCredentialsUseCase(authenticator),
            factory.createValidateUserCredentialsUseCase(authenticator)
        )
        
        super.onCreate(savedInstanceState)
        
        initView()
        initListeners()
        initCollectors()
    }
    
    private fun initCollectors() {
        lifecycleScope.launch {
            viewModel.showAuthenticationError.collect { onShowAuthenticationErrorCollected(it) }
        }
        lifecycleScope.launch {
            viewModel.showEmptyMasterPasswordError.collect { onShowEmptyMasterPasswordErrorCollected() }
        }
        lifecycleScope.launch {
            viewModel.showInvalidMasterPasswordError.collect { onShowInvalidMasterPasswordErrorCollected() }
        }
        lifecycleScope.launch {
            viewModel.showPasswordsView.collect { onShowPasswordsViewCollected() }
        }
    }
    
    private fun initListeners() {
        viewDataBinding.run {
            confirmedMasterPasswordTextInputLayout.editText?.addTextChangedListener(confirmedMasterPasswordTextWatcher)
            masterPasswordTextInputLayout.editText?.addTextChangedListener(masterPasswordTextWatcher)
            authenticateMaterialButton.setOnClickListener(authenticateOnClickListener)
        }
    }
    
    private fun initView() {
        viewDataBinding.run {
            confirmedMasterPasswordTextInputLayout.editText?.setText(viewModel.confirmedMasterPassword.value)
            masterPasswordTextInputLayout.editText?.setText(viewModel.masterPassword.value)
        }
    }
    
    private fun onShowAuthenticationErrorCollected(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setTitle(R.string.error)
            .show()
    }
    
    private fun onShowEmptyMasterPasswordErrorCollected() {
        AlertDialog.Builder(this)
            .setMessage(R.string.error_empty_master_password)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setTitle(R.string.error)
            .show()
    }
    
    private fun onShowInvalidMasterPasswordErrorCollected() {
        AlertDialog.Builder(this)
            .setMessage(R.string.error_different_master_passwords)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setTitle(R.string.error)
            .show()
    }
    
    private fun onShowPasswordsViewCollected() {
        startActivity(Intent(this, PasswordsActivity::class.java))
        finish()
    }
    
}
