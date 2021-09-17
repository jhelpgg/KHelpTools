package khelp.engine3d.extensions

val Int.alpha : Int get() = (this shr 24) and 0xFF
val Int.red : Int get() = (this shr 16) and 0xFF
val Int.green : Int get() = (this shr 8) and 0xFF
val Int.blue : Int get() = this and 0xFF
