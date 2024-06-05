package space.w0lf.password.manager.ui.passwords

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.w0lf.password.manager.R
import space.w0lf.password.manager.application.registry.ApplicationRegistry
import space.w0lf.password.manager.base.android.BaseActivity
import space.w0lf.password.manager.data.dao.android.room.database.PasswordManagerDatabase
import space.w0lf.password.manager.databinding.ActivityPasswordsBinding
import space.w0lf.password.manager.databinding.ActivityShowPasswordBinding
import space.w0lf.password.manager.ui.addPassword.AddPasswordActivity
import space.w0lf.password.manager.ui.showPassword.ShowPasswordActivity

class PasswordsActivity : BaseActivity<ActivityPasswordsBinding, PasswordsViewModel>(
    R.layout.activity_passwords,
    PasswordsViewModel::class.java
) {
    
    private lateinit var actionsAnchorView: View
    private lateinit var addMenuItem: MenuItem
    
    private val passwordsAdapterCallbacks = object : PasswordsAdapter.AdapterCallbacks {
        override fun onActionsClicked(view: View, position: Int) {
            actionsAnchorView = view
            lifecycleScope.launch(Dispatchers.IO) { viewModel.actionsClicked(position) }
        }
    }
    
    private val searchOnActionExpandListener = object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.searchViewClosed()
            }
            
            addMenuItem.isVisible = true
            
            return true
        }
        
        override fun onMenuItemActionExpand(item: MenuItem): Boolean {
            addMenuItem.isVisible = false
            return true
        }
    }
    
    private val searchOnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(newText: String?): Boolean {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.queryTextSubmitted(newText!!)
            }
            
            return true
        }
        
        override fun onQueryTextSubmit(query: String?): Boolean {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.queryTextSubmitted(query!!)
            }
            
            return true
        }
    }
    
    private val showAddPasswordViewLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            lifecycleScope.launch(Dispatchers.IO) { viewModel.load() }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        initDatabase()
        
        val passwordDao = factory.createPasswordDao()
        
        viewModelFactory = PasswordsViewModel.Factory(
            factory.createDeletePasswordUseCase(passwordDao),
            factory.createGetPasswordsUseCase(passwordDao)
        )
    
        super.onCreate(savedInstanceState)
        
        initView()
        initCollectors()
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_passwords, menu)
        
        menu?.findItem(R.id.add)?.let {
            addMenuItem = it
        }
        
        menu?.findItem(R.id.search)?.let {
            it.setOnActionExpandListener(searchOnActionExpandListener)
            
            (it.actionView as? SearchView)?.setOnQueryTextListener(searchOnQueryTextListener)
        }
        
        return true
    }
    
    override fun onDestroy() {
        ApplicationRegistry.passwordManagerDatabase.close()
        
        super.onDestroy()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                lifecycleScope.launch(Dispatchers.IO) { viewModel.addClicked() }
            }
        }
        
        return super.onOptionsItemSelected(item)
    }
    
    private fun initCollectors() {
        lifecycleScope.launch {
            viewModel.passwordDeleted.collect { onPasswordDeletedCollected(it) }
        }
        lifecycleScope.launch {
            viewModel.showActionsView.collect { onShowActionsViewCollected(it) }
        }
        lifecycleScope.launch {
            viewModel.showAddPasswordView.collect { onShowAddPasswordViewCollected() }
        }
        lifecycleScope.launch {
            viewModel.showEditPasswordView.collect { onShowEditPasswordViewCollected(it) }
        }
        lifecycleScope.launch {
            viewModel.showPassword.collect{ onShowPasswordCollected(it) }
        }
        lifecycleScope.launch {
            viewModel.showPasswords.collect { onShowPasswordsCollected() }
        }
        lifecycleScope.launch {
            viewModel.showConfirmDeletePasswordView.collect { onShowConfirmPasswordViewCollected(it) }
        }
    }
    
    private fun initDatabase() {
        ApplicationRegistry.passwordManagerDatabase = Room.databaseBuilder(
            this,
            PasswordManagerDatabase::class.java,
            "password_manager.db"
        ).build()
    }
    
    private fun initView() {
        viewDataBinding.passwordsRecyclerView.layoutManager =
            LinearLayoutManager(
                this@PasswordsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
    }
    
    private fun onPasswordDeletedCollected(passwordPosition: Int) {
        viewDataBinding.passwordsRecyclerView.adapter?.notifyItemRemoved(passwordPosition)
    }
    
    private fun onShowAddPasswordViewCollected() {
        showAddPasswordViewLauncher.launch(Intent(
            this,
            AddPasswordActivity::class.java
        ))
    }
    
    private fun onShowActionsViewCollected(position: Int) {
        PopupMenu(this@PasswordsActivity, actionsAnchorView).run {
            inflate(R.menu.menu_passwords_password_actions)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.copy -> {
                        (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).run {
                            setPrimaryClip(
                                ClipData.newPlainText(
                                    "",
                                    viewModel.passwords.value[position].password
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
                
                    R.id.delete -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.deleteClicked(position)
                        }
                    }
                
                    R.id.edit -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.editClicked(position)
                        }
                    }
                
                    R.id.show -> {
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.showClicked(position)
                        }
                    }
                }
            
                true
            }
            show()
        }
    }
    
    private fun onShowConfirmPasswordViewCollected(passwordPosition: Int) {
        AlertDialog.Builder(this)
            .setMessage(R.string.message_confirm_delete_password)
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.yes) { dialog, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    dialog.dismiss()
                    viewModel.confirmDeleteClicked(passwordPosition)
                }
            }
            .setTitle(R.string.warning)
            .show()
    }
    
    private fun onShowEditPasswordViewCollected(position: Int) {
        showAddPasswordViewLauncher.launch(Intent(this@PasswordsActivity, AddPasswordActivity::class.java).apply {
            putExtra(AddPasswordActivity.EXTRA_MODE, AddPasswordActivity.MODE_EDIT)
            putExtra(AddPasswordActivity.EXTRA_PASSWORD, viewModel.passwords.value[position])
        })
    }
    
    private fun onShowPasswordCollected(position: Int) {
        startActivity(Intent(this, ShowPasswordActivity::class.java).apply {
            putExtra(ShowPasswordActivity.EXTRA_PASSWORD, viewModel.passwords.value[position])
        })
    }
    
    private fun onShowPasswordsCollected() {
        viewDataBinding.passwordsRecyclerView.apply {
            adapter = PasswordsAdapter(
                this@PasswordsActivity,
                viewModel.passwords.value,
                passwordsAdapterCallbacks
            )
        }
    }
    
}
