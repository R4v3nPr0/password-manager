package space.w0lf.password.manager.domain.useCase

import space.w0lf.password.manager.base.domain.useCase.UseCase

abstract class StoreCredentialsUseCase : UseCase<StoreCredentialsUseCase.Input, StoreCredentialsUseCase.Output>() {
    
    class Input(val masterPassword: String) : UseCase.Input()
    class Output : UseCase.Output()
    
}
