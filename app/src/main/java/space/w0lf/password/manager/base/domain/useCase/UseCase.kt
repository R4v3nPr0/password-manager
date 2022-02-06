package space.w0lf.password.manager.base.domain.useCase

abstract class UseCase<I : UseCase.Input, O : UseCase.Output> {
    
    abstract class Input
    abstract class Output
    
    abstract fun run (input: I): O
    
}
