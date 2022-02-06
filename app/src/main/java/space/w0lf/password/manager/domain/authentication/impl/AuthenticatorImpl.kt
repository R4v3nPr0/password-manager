package space.w0lf.password.manager.domain.authentication.impl

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import space.w0lf.password.manager.R
import space.w0lf.password.manager.application.exception.AuthenticationException
import space.w0lf.password.manager.application.registry.ApplicationRegistry
import space.w0lf.password.manager.data.dao.android.content.SharedPreferencesHelper
import space.w0lf.password.manager.data.dao.android.room.crypto.PasswordCipher
import space.w0lf.password.manager.domain.authentication.Authenticator
import space.w0lf.password.manager.util.Result
import java.util.concurrent.CountDownLatch

class AuthenticatorImpl(private val fragmentActivity: FragmentActivity) : Authenticator {
    
    override fun authenticate(): Result<Boolean, AuthenticationException> {
        val biometricManager = BiometricManager.from(fragmentActivity)
    
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                return Result.failure(AuthenticationException("The user can't authenticate because the hardware is unavailable."))
            }
        
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                return Result.failure(AuthenticationException("The user can't authenticate because no biometric or device credential is enrolled."))
            }
        
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                return Result.failure(AuthenticationException("The user can't authenticate because there is no suitable hardware."))
            }
        
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                return Result.failure(AuthenticationException("The user can't authenticate because a security vulnerability has been discovered with one or more hardware sensors."))
            }
        
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                return Result.failure(AuthenticationException("The user can't authenticate because the specified options are incompatible with the current Android version."))
            }
        
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                return Result.failure(AuthenticationException("Unable to determine whether the user can authenticate."))
            }
        }
    
        var authenticationResult = Result.failure<Boolean, AuthenticationException>(
            AuthenticationException(fragmentActivity.getString(R.string.authentication_failed))
        )
        val countDownLatch = CountDownLatch(1)
    
        val authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                authenticationResult = when (errorCode) {
                    BiometricPrompt.ERROR_CANCELED -> {
                        Result.failure(AuthenticationException("The operation was canceled because the biometric sensor is unavailable."))
                    }
                    
                    BiometricPrompt.ERROR_HW_NOT_PRESENT -> {
                        Result.failure(AuthenticationException("The device does not have the required authentication hardware."))
                    }
                    
                    BiometricPrompt.ERROR_HW_UNAVAILABLE -> {
                        Result.failure(AuthenticationException("The hardware is unavailable."))
                    }
                    
                    BiometricPrompt.ERROR_LOCKOUT -> {
                        Result.failure(AuthenticationException("The operation was canceled because the API is locked out due to too many attempts."))
                    }
                    
                    BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> {
                        Result.failure(AuthenticationException("The operation was canceled because ERROR_LOCKOUT occurred too many times."))
                    }
                    
                    BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                        Result.failure(AuthenticationException("Authentication canceled."))
                    }
                    
                    BiometricPrompt.ERROR_NO_BIOMETRICS -> {
                        Result.failure(AuthenticationException("The user does not have any biometrics enrolled."))
                    }
                    
                    BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> {
                        Result.failure(AuthenticationException("The device does not have pin, pattern, or password set up."))
                    }
                    
                    BiometricPrompt.ERROR_NO_SPACE -> {
                        Result.failure(AuthenticationException("The operation can't be completed because there is not enough device storage remaining."))
                    }
                    
                    BiometricPrompt.ERROR_SECURITY_UPDATE_REQUIRED -> {
                        Result.failure(AuthenticationException("A security vulnerability has been discovered with one or more hardware sensors."))
                    }
                    
                    BiometricPrompt.ERROR_TIMEOUT -> {
                        Result.failure(AuthenticationException("The current operation has been running too long and has timed out."))
                    }
                    
                    BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> {
                        Result.failure(AuthenticationException("The sensor was unable to process the current image."))
                    }
                    
                    BiometricPrompt.ERROR_USER_CANCELED -> {
                        Result.failure(AuthenticationException("Authentication canceled."))
                    }
                    
                    BiometricPrompt.ERROR_VENDOR -> {
                        Result.failure(AuthenticationException("The operation failed due to a vendor-specific error."))
                    }
                    
                    else -> {
                        Result.failure(AuthenticationException("An error has occurred while authenticating."))
                    }
                }
                
                countDownLatch.countDown()
            }
        
            override fun onAuthenticationFailed() {
                return
            }
        
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                authenticationResult = Result.success(true)
                countDownLatch.countDown()
            }
        }
    
        val biometricPrompt = BiometricPrompt(fragmentActivity, authenticationCallback)
    
        fragmentActivity.runOnUiThread {
            biometricPrompt.authenticate(
                BiometricPrompt.PromptInfo.Builder()
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                    .setNegativeButtonText(fragmentActivity.getString(R.string.cancel))
                    .setTitle(fragmentActivity.getString(R.string.authentication_required))
                    .build()
            )
        }
    
        countDownLatch.await()
        
        if (authenticationResult.isSuccess && authenticationResult.value!!) {
            ApplicationRegistry.passwordCipher = PasswordCipher(fragmentActivity)
        }
    
        return authenticationResult
    }
    
    override fun saveCredentials(masterPassword: String) {
        SharedPreferencesHelper(fragmentActivity).putString(SharedPreferencesHelper.KEY_MASTER_PASSWORD, masterPassword)
        ApplicationRegistry.passwordCipher = PasswordCipher(fragmentActivity)
    }
    
    override fun validateUserCredentials(): Boolean {
        return SharedPreferencesHelper(fragmentActivity).contains(SharedPreferencesHelper.KEY_MASTER_PASSWORD)
    }
    
}
