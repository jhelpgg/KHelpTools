package khelp.utilities.extensions

infix fun Byte.and(value : Int) : Int =
    (this.toInt() and 0xFF) and value

infix fun Byte.or(value : Int) : Int =
    (this.toInt() and 0xFF) or value

infix fun Byte.shl(shift : Int) : Int =
    (this.toInt() and 0xFF) shl shift

infix fun Byte.shr(shift : Int) : Int =
    (this.toInt() and 0xFF) shr shift

fun Byte.toUnsignedInt() : Int =
    this.toInt() and 0xFF
