package space.w0lf.password.manager.domain.useCase

import space.w0lf.password.manager.base.domain.useCase.UseCase
import space.w0lf.password.manager.domain.model.Password
import space.w0lf.password.manager.util.Result

abstract class DeletePasswordUseCase : UseCase<DeletePasswordUseCase.Input, DeletePasswordUseCase.Output>() {
    
    class Input(val password: Password) : UseCase.Input()
    class Output(val result: Result<Boolean, Exception>) : UseCase.Output()
    
}
