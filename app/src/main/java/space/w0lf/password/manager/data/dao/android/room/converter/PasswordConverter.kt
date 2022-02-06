package space.w0lf.password.manager.data.dao.android.room.converter

import space.w0lf.password.manager.data.dao.android.room.entity.PasswordEntity
import space.w0lf.password.manager.domain.model.Password

class PasswordConverter {
    
    fun convert(password: Password): PasswordEntity =
        PasswordEntity(
            password.id,
            password.name,
            password.notes,
            password.password,
            password.status,
            password.url,
            password.username
        )
    
    fun convert(passwordEntity: PasswordEntity): Password =
        Password(
            passwordEntity.id,
            passwordEntity.name,
            passwordEntity.notes,
            passwordEntity.password,
            passwordEntity.status,
            passwordEntity.url,
            passwordEntity.username
        )
    
}
