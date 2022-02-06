package space.w0lf.password.manager.domain.useCase.impl

import space.w0lf.password.manager.domain.authentication.Authenticator
import space.w0lf.password.manager.domain.useCase.ValidateUserCredentialsUseCase

class ValidateUserCredentialsUseCaseImpl(private val authenticator: Authenticator) : ValidateUserCredentialsUseCase() {
    
    override fun run(input: Input): Output =
        Output(authenticator.validateUserCredentials())
    
}
