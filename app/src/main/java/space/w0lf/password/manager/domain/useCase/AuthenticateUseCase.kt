package space.w0lf.password.manager.domain.useCase

import space.w0lf.password.manager.application.exception.AuthenticationException
import space.w0lf.password.manager.base.domain.useCase.UseCase
import space.w0lf.password.manager.util.Result

abstract class AuthenticateUseCase : UseCase<AuthenticateUseCase.Input, AuthenticateUseCase.Output>() {
    
    class Input : UseCase.Input()
    class Output(val result: Result<Boolean, AuthenticationException>) : UseCase.Output()
    
}
