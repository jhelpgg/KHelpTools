package khelp.engine3d.render.font

import java.awt.geom.PathIterator

enum class WindingRule(val rule : Int)
{
    EVEN_ODD(PathIterator.WIND_EVEN_ODD),
    NON_ZERO(PathIterator.WIND_NON_ZERO)
}

fun windingRule(rule : Int) : WindingRule
{
    for (windingRule in WindingRule.values())
    {
        if (windingRule.rule == rule)
        {
            return windingRule
        }
    }

    return WindingRule.EVEN_ODD
}
