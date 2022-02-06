package space.w0lf.password.manager.domain.useCase.impl

import space.w0lf.password.manager.data.dao.PasswordDao
import space.w0lf.password.manager.domain.useCase.DeletePasswordUseCase
import space.w0lf.password.manager.util.Result

class DeletePasswordUseCaseImpl(private val passwordDao: PasswordDao) : DeletePasswordUseCase() {
    
    override fun run(input: Input): Output {
        passwordDao.deletePassword(input.password)
        return Output(Result.success(true))
    }
    
}
