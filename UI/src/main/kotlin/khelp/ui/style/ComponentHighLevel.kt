package khelp.ui.style

import kotlin.math.max


enum class ComponentHighLevel(val level : Int)
{
    AT_GROUND(0),
    NEAR_GROUND(4),
    FLY(8),
    HIGHEST(16)
}

val COMPONENT_HIGHEST_LEVEL : Int by lazy {
    var maximum = 0

    for (componentHighLevel in ComponentHighLevel.values())
    {
        maximum = max(maximum, componentHighLevel.level)
    }

    maximum
}
