package space.w0lf.password.manager.util

class Result<T, E : Exception> private constructor(
    val exception: E? = null,
    val isFailure: Boolean = false,
    val isSuccess: Boolean = false,
    val value: T? = null
)
{
    
    companion object {
        @JvmStatic
        fun <T, E : Exception> failure(exception: E): Result<T, E> =
            Result(isFailure = true, exception = exception)
        
        @JvmStatic
        fun <T, E : Exception> success(value: T): Result<T, E> =
            Result(isSuccess = true, value = value)
    }
    
}
