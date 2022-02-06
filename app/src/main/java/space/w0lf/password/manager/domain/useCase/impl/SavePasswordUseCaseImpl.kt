package space.w0lf.password.manager.domain.useCase.impl

import space.w0lf.password.manager.data.dao.PasswordDao
import space.w0lf.password.manager.domain.useCase.SavePasswordUseCase
import space.w0lf.password.manager.util.Result

class SavePasswordUseCaseImpl(private val passwordDao: PasswordDao) : SavePasswordUseCase() {
    
    override fun run(input: Input): Output {
        passwordDao.savePassword(input.password, input.isPasswordUpdate)
        return Output(Result.success(true))
    }
    
}
