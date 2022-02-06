package space.w0lf.password.manager.data.dao.android.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import space.w0lf.password.manager.data.dao.android.room.entity.PasswordEntity

@Dao
interface PasswordDao {
    
    @Delete
    fun delete(password: PasswordEntity)
    
    @Query("SELECT * FROM Passwords")
    fun getAll(): List<PasswordEntity>

    @Insert
    fun insert(password: PasswordEntity)
    
    @Update
    fun update(password: PasswordEntity)

}
