package space.w0lf.password.manager.ui.passwords

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import space.w0lf.password.manager.databinding.ItemPasswordBinding
import space.w0lf.password.manager.domain.model.Password

class PasswordsAdapter(
    context: Context,
    private val passwords: List<Password>,
    private val adapterCallbacks: AdapterCallbacks
) : RecyclerView.Adapter<PasswordsAdapter.PasswordViewHolder>() {
    
    interface AdapterCallbacks {
        fun onActionsClicked(view: View, position: Int)
    }
    
    private val layoutInflater = LayoutInflater.from(context)
    
    class PasswordViewHolder(val binding: ItemPasswordBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(password: Password) {
            binding.nameTextView.text = password.name
            binding.urlTextView.text = password.url
        }
        
    }
    
    override fun getItemCount(): Int = passwords.size
    
    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        holder.bind(passwords[position])
        holder.binding.actionsImageButton.setOnClickListener {
            adapterCallbacks.onActionsClicked(holder.binding.actionsImageButton, holder.adapterPosition)
        }
        
        if (position == passwords.lastIndex) {
            holder.binding.root.apply {
                layoutParams = (layoutParams as RecyclerView.LayoutParams).apply {
                    setMargins(marginLeft, marginTop, marginRight, marginTop)
                }
            }
        } else {
            holder.binding.root.apply {
                layoutParams = (layoutParams as RecyclerView.LayoutParams).apply {
                    setMargins(marginLeft, marginTop, marginRight, 0)
                }
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder =
        PasswordViewHolder(ItemPasswordBinding.inflate(layoutInflater, parent, false))
    
}