package space.w0lf.password.manager.domain.useCase.impl

import space.w0lf.password.manager.domain.authentication.Authenticator
import space.w0lf.password.manager.domain.useCase.StoreCredentialsUseCase

class StoreCredentialsUseCaseImpl(private val authenticator: Authenticator) : StoreCredentialsUseCase() {
    
    override fun run(input: Input): Output {
        authenticator.saveCredentials(input.masterPassword)
        return Output()
    }
    
}
