package space.w0lf.password.manager.domain.useCase

import space.w0lf.password.manager.base.domain.useCase.UseCase
import space.w0lf.password.manager.domain.model.Password
import space.w0lf.password.manager.util.Result
import java.lang.Exception

abstract class GetPasswordsUseCase : UseCase<GetPasswordsUseCase.Input, GetPasswordsUseCase.Output>() {
    
    class Input : UseCase.Input()
    class Output(val result:Result<List<Password>, Exception> ) : UseCase.Output()
    
}
