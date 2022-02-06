package space.w0lf.password.manager.domain.useCase

import space.w0lf.password.manager.base.domain.useCase.UseCase

abstract class ValidateUserCredentialsUseCase : UseCase<ValidateUserCredentialsUseCase.Input, ValidateUserCredentialsUseCase.Output>() {
    
    class Input : UseCase.Input()
    class Output(val isUserCredentialsSaved: Boolean) : UseCase.Output()
    
}
