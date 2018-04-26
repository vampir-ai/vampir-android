package org.vampirai.vampir.models

import java.util.*

data class User(val id: UUID, val username: String, val firstName: String, val lastName: String, val password: String, val email: String)