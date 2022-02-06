package space.w0lf.password.manager.domain.useCase.impl

import space.w0lf.password.manager.domain.authentication.Authenticator
import space.w0lf.password.manager.domain.useCase.AuthenticateUseCase
import space.w0lf.password.manager.util.Result

class AuthenticateUseCaseImpl(private val authenticator: Authenticator) : AuthenticateUseCase() {
    
    override fun run(input: Input): Output {
        val authenticationResult = authenticator.authenticate()
    
        return if (authenticationResult.isSuccess && authenticationResult.value!!) {
            Output(Result.success(true))
        } else {
            Output(authenticationResult)
        }
    }
    
}
