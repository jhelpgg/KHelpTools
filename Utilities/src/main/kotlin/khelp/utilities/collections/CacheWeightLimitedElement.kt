package khelp.utilities.collections

import khelp.utilities.math.sign

internal data class CacheWeightLimitedElement<V>(var lastTime : Long, val weight : Long, val element : V)



