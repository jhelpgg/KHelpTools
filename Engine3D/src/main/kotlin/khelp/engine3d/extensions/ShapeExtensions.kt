package khelp.engine3d.extensions

import khelp.engine3d.geometry.path.Path
import khelp.ui.utilities.AFFINE_TRANSFORM
import khelp.utilities.log.debug
import java.awt.Shape
import java.awt.geom.PathIterator

val Shape.path : Path
    get()
    {
        val path = Path()
        val iterator = this.getPathIterator(AFFINE_TRANSFORM)
        val coordinates = FloatArray(6)
        debug(iterator.windingRule,
              " <=> WIND_EVEN_ODD=", PathIterator.WIND_EVEN_ODD,
              " | WIND_NON_ZERO=", PathIterator.WIND_NON_ZERO)

        while (! iterator.isDone)
        {
            val type = iterator.currentSegment(coordinates)

            when (type)
            {
                PathIterator.SEG_CLOSE   ->
                {
                    debug("CLOSE")
                    path.close()
                }
                PathIterator.SEG_MOVETO  ->
                {
                    debug("MOVE ", coordinates[0], ", ", coordinates[1])
                    path.move(coordinates[0], coordinates[1])
                }
                PathIterator.SEG_LINETO  ->
                {
                    debug("LINE ", coordinates[0], ", ", coordinates[1])
                    path.line(coordinates[0], coordinates[1])
                }
                PathIterator.SEG_QUADTO  ->
                {
                    debug("QUAD ", coordinates[0], ", ", coordinates[1],
                          " | ", coordinates[2], ", ", coordinates[3])
                    path.quadratic(coordinates[0], coordinates[1],
                                   coordinates[2], coordinates[3])
                }
                PathIterator.SEG_CUBICTO ->
                {
                    debug("CUBIC  ", coordinates[0], ", ", coordinates[1],
                          " | ", coordinates[2], ", ", coordinates[3],
                          " | ", coordinates[4], ", ", coordinates[5])
                    path.cubic(coordinates[0], coordinates[1],
                               coordinates[2], coordinates[3],
                               coordinates[4], coordinates[5])
                }

            }

            iterator.next()
        }

        return path
    }
