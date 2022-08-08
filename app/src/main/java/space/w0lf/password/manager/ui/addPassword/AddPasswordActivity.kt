package space.w0lf.password.manager.ui.addPassword

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import space.w0lf.password.manager.R
import space.w0lf.password.manager.base.android.BaseActivity
import space.w0lf.password.manager.databinding.ActivityAddPasswordBinding
import space.w0lf.password.manager.domain.model.Password

class AddPasswordActivity() : BaseActivity<ActivityAddPasswordBinding, AddPasswordViewModel>(
    R.layout.activity_add_password,
    AddPasswordViewModel::class.java
) {
    
    companion object {
        const val EXTRA_MODE = "EXTRA_MODE"
        const val EXTRA_PASSWORD = "EXTRA_PASSWORD"
        const val MODE_EDIT = 1
    }
    
    private val confirmedPasswordTextWatcher = object : TextWatcher {
    
        override fun afterTextChanged(s: Editable?) {
            return
        }
        
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }
        
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            lifecycleScope.launch(Dispatchers.IO) { viewModel.setConfirmedPassword(s.toString()) }
        }
        
    }
    
    private val nameTextWatcher = object : TextWatcher {
    
        override fun afterTextChanged(s: Editable?) {
            return
        }
        
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }
        
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            lifecycleScope.launch(Dispatchers.IO) { viewModel.setName(s.toString()) }
        }
        
    }
    
    private val notesTextWatcher = object : TextWatcher {
        
        override fun afterTextChanged(s: Editable?) {
            return
        }
        
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }
        
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            lifecycleScope.launch(Dispatchers.IO) { viewModel.setNotes(s.toString()) }
        }
        
    }
    
    private val passwordTextWatcher = object : TextWatcher {
    
        override fun afterTextChanged(s: Editable?) {
            return
        }
        
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }
        
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            lifecycleScope.launch(Dispatchers.IO) { viewModel.setPassword(s.toString()) }
        }
        
    }
    
    private val statusOnItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
        lifecycleScope.launch(Dispatchers.IO) { viewModel.setSelectedStatus(id.toInt()) }
    }
    
    private val urlTextWatcher = object : TextWatcher {
    
        override fun afterTextChanged(s: Editable?) {
            return
        }
        
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }
        
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            lifecycleScope.launch(Dispatchers.IO) { viewModel.setUrl(s.toString()) }
        }
        
    }
    
    private val usernameTextWatcher = object : TextWatcher {
    
        override fun afterTextChanged(s: Editable?) {
            return
        }
        
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            return
        }
        
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            lifecycleScope.launch(Dispatchers.IO) { viewModel.setUsername(s.toString()) }
        }
        
    }
    
    private val saveOnClickListener = View.OnClickListener {
        lifecycleScope.launch(Dispatchers.IO) { viewModel.saveClicked() }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelFactory = AddPasswordViewModel.Factory(factory.createSavePasswordUseCase(factory.createPasswordDao()))
        
        super.onCreate(savedInstanceState)
        
        intent.extras?.let {
            if (
                intent.extras?.containsKey(EXTRA_MODE)!! &&
                intent.extras?.containsKey(EXTRA_PASSWORD)!!
            ) {
                setTitle(R.string.edit_password)
        
                val password = intent.extras?.getSerializable(EXTRA_PASSWORD)!! as Password
                
                runBlocking {
                    viewModel.setConfirmedPassword(password.password)
                    viewModel.setId(password.id)
                    viewModel.setIsPasswordUpdate(true)
                    viewModel.setName(password.name)
                    viewModel.setNotes(password.notes)
                    viewModel.setPassword(password.password)
                    viewModel.setSelectedStatus(if (password.status == "ACTIVE") 0 else 1)
                    viewModel.setUrl(password.url)
                    viewModel.setUsername(password.username)
                }
            }
        }
        
        initView()
        initListeners()
        initCollectors()
    }
    
    private fun initCollectors() {
        lifecycleScope.launch {
            viewModel.showInvalidNameError.collect { onShowInvalidNameErrorCollected() }
        }
        lifecycleScope.launch {
            viewModel.showInvalidPasswordError.collect { onShowInvalidPasswordErrorCollected() }
        }
        lifecycleScope.launch {
            viewModel.showInvalidStatusError.collect { onShowInvalidStatusErrorCollected() }
        }
        lifecycleScope.launch {
            viewModel.showPasswordSaveError.collect { onShowPasswordSaveErrorCollected() }
        }
        lifecycleScope.launch {
            viewModel.showPasswordSaveSuccess.collect { onShowPasswordSaveSuccessCollected() }
        }
    }
    
    private fun initListeners() {
        viewDataBinding.run {
            confirmedPasswordTextInputLayout.editText
                ?.addTextChangedListener(confirmedPasswordTextWatcher)
            nameTextInputLayout.editText?.addTextChangedListener(nameTextWatcher)
            notesTextInputLayout.editText?.addTextChangedListener(notesTextWatcher)
            passwordTextInputLayout.editText?.addTextChangedListener(passwordTextWatcher)
            urlTextInputLayout.editText?.addTextChangedListener(urlTextWatcher)
            usernameTextInputLayout.editText?.addTextChangedListener(usernameTextWatcher)
            saveMaterialButton.setOnClickListener(saveOnClickListener)
    
            (statusTextInputLayout.editText as? AppCompatAutoCompleteTextView)
                ?.onItemClickListener = statusOnItemClickListener
        }
    }
    
    private fun initView() {
        viewDataBinding.run {
            confirmedPasswordTextInputLayout.editText
                ?.setText(viewModel.confirmedPassword.value)
            nameTextInputLayout.editText?.setText(viewModel.name.value)
            notesTextInputLayout.editText?.setText(viewModel.notes.value)
            passwordTextInputLayout.editText?.setText(viewModel.password.value)
            urlTextInputLayout.editText?.setText(viewModel.url.value)
            usernameTextInputLayout.editText?.setText(viewModel.username.value)
            
            (statusTextInputLayout.editText as? AppCompatAutoCompleteTextView)?.setAdapter(
                ArrayAdapter(
                    this@AddPasswordActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    listOf(getString(R.string.active), getString(R.string.inactive))
                )
            )
            
            if (viewModel.selectedStatus.value != -1) {
                val text = if (viewModel.selectedStatus.value == 0)
                    getString(R.string.active)
                else
                    getString(R.string.inactive)
                
                (statusTextInputLayout.editText as? AppCompatAutoCompleteTextView)?.apply {
                    setText(text, false)
                }
            }
        }
    }
    
    private fun onShowInvalidNameErrorCollected() {
        AlertDialog.Builder(this)
            .setMessage(R.string.error_empty_name)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setTitle(R.string.error)
            .show()
    }
    
    private fun onShowInvalidPasswordErrorCollected() {
        AlertDialog.Builder(this)
            .setMessage(R.string.error_different_passwords)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setTitle(R.string.error)
            .show()
    }
    
    private fun onShowInvalidStatusErrorCollected() {
        AlertDialog.Builder(this)
            .setMessage(R.string.error_status_not_selected)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setTitle(R.string.error)
            .show()
    }
    
    private fun onShowPasswordSaveErrorCollected() {
        AlertDialog.Builder(this)
            .setMessage(R.string.error_password_not_saved)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .setTitle(R.string.error)
            .show()
    }
    
    private fun onShowPasswordSaveSuccessCollected() {
        AlertDialog.Builder(this)
            .setMessage(R.string.message_password_saved)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
                setResult(RESULT_OK)
                finish()
            }
            .setTitle(R.string.information)
            .show()
    }
    
}
