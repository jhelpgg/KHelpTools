package khelp.ui.extensions

import java.awt.Shape
import java.awt.geom.Rectangle2D

fun Shape.innerBounds() : Rectangle2D
{
    val maxBounds = this.bounds2D

    if (this.contains(maxBounds))
    {
        return maxBounds
    }

    val bounds = Rectangle2D.Double(maxBounds.x, maxBounds.y, maxBounds.width, maxBounds.height)
    val minBounds = Rectangle2D.Double(maxBounds.centerX - 0.5, maxBounds.centerY - 0.5, 1.0, 1.0)

    while (! bounds.near(minBounds, 0.5))
    {
        val semiBounds = Rectangle2D.Double((bounds.x + minBounds.x) / 2.0,
                                            (bounds.y + minBounds.y) / 2.0,
                                            (bounds.width + minBounds.width) / 2.0,
                                            (bounds.height + minBounds.height) / 2.0)

        if (this.contains(semiBounds))
        {
            minBounds.setRect(semiBounds)
        }
        else
        {
            bounds.setRect(semiBounds)
        }
    }

    return bounds
}
