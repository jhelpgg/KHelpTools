package khelp.security.exception

import java.lang.Exception

const val LOGIN_PASSWORD_INVALID_MESSAGE = "Wrong login or password."

object LoginPasswordInvalidException : Exception(LOGIN_PASSWORD_INVALID_MESSAGE)
