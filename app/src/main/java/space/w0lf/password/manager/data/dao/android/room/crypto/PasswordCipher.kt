package space.w0lf.password.manager.data.dao.android.room.crypto

import android.content.Context
import android.util.Base64
import space.w0lf.password.manager.data.dao.android.content.SharedPreferencesHelper
import space.w0lf.password.manager.data.dao.android.room.entity.PasswordEntity
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec

class PasswordCipher(context: Context) {
    
    companion object {
        private const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
        private const val ITERATION_COUNT = 1_000_000
        private const val IV_LENGTH = 12
        private const val KEY_LENGTH = 256
        private const val SALT = "space.w0lf.password.manager"
        private const val SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2withHmacSHA512"
    }
    
    private val secretKey: SecretKey = SecretKeyFactory
        .getInstance(SECRET_KEY_FACTORY_ALGORITHM)
        .generateSecret(
            PBEKeySpec(
                SharedPreferencesHelper(context)
                    .getString(SharedPreferencesHelper.KEY_MASTER_PASSWORD)
                    .toCharArray(),
                SALT.toByteArray(),
                ITERATION_COUNT,
                KEY_LENGTH
            )
        )
    
    fun decrypt(passwordEntity: PasswordEntity): PasswordEntity {
        return PasswordEntity(
            passwordEntity.id,
            Base64.decode(passwordEntity.name, Base64.DEFAULT).run {
                val iv = sliceArray(0 until IV_LENGTH)
                val value = sliceArray(IV_LENGTH until size)
        
                String(decryptCipher(iv).doFinal(value))
            },
            Base64.decode(passwordEntity.notes, Base64.DEFAULT).run {
                val iv = sliceArray(0 until IV_LENGTH)
                val value = sliceArray(IV_LENGTH until size)
        
                String(decryptCipher(iv).doFinal(value))
            },
            Base64.decode(passwordEntity.password, Base64.DEFAULT).run {
                val iv = sliceArray(0 until IV_LENGTH)
                val value = sliceArray(IV_LENGTH until size)
        
                String(decryptCipher(iv).doFinal(value))
            },
            Base64.decode(passwordEntity.status, Base64.DEFAULT).run {
                val iv = sliceArray(0 until IV_LENGTH)
                val value = sliceArray(IV_LENGTH until size)
        
                String(decryptCipher(iv).doFinal(value))
            },
            Base64.decode(passwordEntity.url, Base64.DEFAULT).run {
                val iv = sliceArray(0 until IV_LENGTH)
                val value = sliceArray(IV_LENGTH until size)
        
                String(decryptCipher(iv).doFinal(value))
            },
            Base64.decode(passwordEntity.username, Base64.DEFAULT).run {
                val iv = sliceArray(0 until IV_LENGTH)
                val value = sliceArray(IV_LENGTH until size)
        
                String(decryptCipher(iv).doFinal(value))
            }
        )
    }
    
    fun encrypt(passwordEntity: PasswordEntity): PasswordEntity {
        return PasswordEntity(
            passwordEntity.id,
            Base64.encodeToString(
                encryptCipher().run {
                    iv + doFinal(passwordEntity.name.toByteArray())
                },
                Base64.DEFAULT
            ),
            Base64.encodeToString(
                encryptCipher().run {
                    iv + doFinal(passwordEntity.notes.toByteArray())
                },
                Base64.DEFAULT
            ),
            Base64.encodeToString(
                encryptCipher().run {
                    iv + doFinal(passwordEntity.password.toByteArray())
                },
                Base64.DEFAULT
            ),
            Base64.encodeToString(
                encryptCipher().run {
                    iv + doFinal(passwordEntity.status.toByteArray())
                },
                Base64.DEFAULT
            ),
            Base64.encodeToString(
                encryptCipher().run {
                    iv + doFinal(passwordEntity.url.toByteArray())
                },
                Base64.DEFAULT
            ),
            Base64.encodeToString(
                encryptCipher().run {
                    iv + doFinal(passwordEntity.username.toByteArray())
                },
                Base64.DEFAULT
            )
        )
    }
    
    private fun decryptCipher(iv: ByteArray): Cipher =
        Cipher.getInstance(CIPHER_TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        }
    
    private fun encryptCipher(): Cipher =
        Cipher.getInstance(CIPHER_TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, secretKey)
        }
    
}
