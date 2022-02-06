package space.w0lf.password.manager.data.dao.android.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import space.w0lf.password.manager.data.dao.android.room.dao.PasswordDao
import space.w0lf.password.manager.data.dao.android.room.entity.PasswordEntity

@Database(entities = [PasswordEntity::class], version = 1)
abstract class PasswordManagerDatabase : RoomDatabase() {
    
    abstract fun passwordDao(): PasswordDao
    
}
