package space.w0lf.password.manager.domain.model

import java.io.Serializable

class Password(
    val id: Int,
    val name: String,
    val notes: String,
    val password: String,
    val status: String,
    val url: String,
    val username: String
) : Serializable
