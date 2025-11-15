package khelp.security.exception

import java.lang.Exception

const val LOGIN_PASSWORD_INVALID_MESSAGE = "Wrong login or password."

class LoginPasswordInvalidException : Exception(LOGIN_PASSWORD_INVALID_MESSAGE)
