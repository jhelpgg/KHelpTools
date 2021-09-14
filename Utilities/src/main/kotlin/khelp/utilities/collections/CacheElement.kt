package khelp.utilities.collections

internal data class CacheElement<out V>(var lastTime: Long, val value: V)
