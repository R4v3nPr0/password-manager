package space.w0lf.password.manager.domain.useCase.impl

import space.w0lf.password.manager.data.dao.PasswordDao
import space.w0lf.password.manager.domain.useCase.GetPasswordsUseCase
import space.w0lf.password.manager.util.Result

class GetPasswordsUseCaseImpl(private val passwordDao: PasswordDao) : GetPasswordsUseCase() {
    
    override fun run(input: Input): Output =
        Output(Result.success(passwordDao.getPasswords()))
    
}
