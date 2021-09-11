package khelp.ui.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

class ShapeExtensionsTests
{
    @Test
    fun ellipse()
    {
        val inner =
            Ellipse2D
                .Double(0.0, 0.0, 250.0, 120.0)
                .innerBounds()
        Assertions.assertEquals(36.5, inner.x, 0.1)
        Assertions.assertEquals(17.4, inner.y, 0.1)
        Assertions.assertEquals(177.0, inner.width, 0.1)
        Assertions.assertEquals(85.1, inner.height, 0.1)
    }

    @Test
    fun rectangle()
    {
        val inner =
            Rectangle2D
                .Double(0.0, 0.0, 250.0, 120.0)
                .innerBounds()
        Assertions.assertEquals(0.0, inner.x, 0.1)
        Assertions.assertEquals(0.0, inner.y, 0.1)
        Assertions.assertEquals(250.0, inner.width, 0.1)
        Assertions.assertEquals(120.0, inner.height, 0.1)
    }
}
