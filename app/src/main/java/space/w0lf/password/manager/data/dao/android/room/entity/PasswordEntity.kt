package space.w0lf.password.manager.data.dao.android.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Passwords")
class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val notes: String,
    val password: String,
    val status: String,
    val url: String,
    val username: String
)
