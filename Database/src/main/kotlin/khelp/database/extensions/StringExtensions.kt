package khelp.database.extensions

import java.util.regex.Pattern

val NAME_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*")

fun String.validName() =
    NAME_PATTERN.matcher(this)
        .matches()