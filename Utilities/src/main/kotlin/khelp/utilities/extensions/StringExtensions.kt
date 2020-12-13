package khelp.utilities.extensions

import java.util.Base64

val String.utf8 get() = this.toByteArray(Charsets.UTF_8)

val String.base64
    get() = Base64.getDecoder()
        .decode(this)
