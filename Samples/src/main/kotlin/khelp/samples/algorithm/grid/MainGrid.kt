package khelp.samples.algorithm.grid

import java.awt.geom.Ellipse2D
import khelp.algorithm.PointF
import khelp.algorithm.interpolation.ColorScale
import khelp.algorithm.interpolation.grid.InterpolationDistanceGrid
import khelp.image.Color
import khelp.image.ui.JHelpImageComponent
import khelp.ui.dsl.borderLayout
import khelp.ui.dsl.frame
import khelp.ui.utilities.COLOR_CYAN_0700
import khelp.ui.utilities.COLOR_GREEN_0700
import khelp.ui.utilities.COLOR_ORANGE_0700
import khelp.ui.utilities.COLOR_RED_0700
import khelp.ui.utilities.COLOR_YELLOW_0700
import khelp.utilities.math.random

fun main()
{
    val points = arrayOf(PointF(0f, 0f), PointF(100f, 100f), PointF(0f, 100f), PointF(100F, 0f), PointF(50f, 50f),
                         PointF(10f, 10f), PointF(42f, 73f))
    val colorScale =
        //     ColorScale(0f, Color(COLOR_BLACK), 10f, Color(COLOR_YELLOW_0700))
        ColorScale(0f, Color(COLOR_CYAN_0700), 10f, Color(COLOR_RED_0700))
    colorScale.add(2.5f, Color(COLOR_GREEN_0700))
    colorScale.add(5f, Color(COLOR_YELLOW_0700))
    colorScale.add(7.5f, Color(COLOR_ORANGE_0700))
    val grid = InterpolationDistanceGrid(512, 512, points, colorScale)//, shape = Ellipse2D.Float(-10f, -10f, 120f, 120f))
    val imageComponent = JHelpImageComponent(512, 512, grid.image)

    frame {
        borderLayout {
            center(imageComponent)
        }
    }

    val values = FloatArray(points.size)

    for (count in 0 until 10_000)
    {
        if (count % 17 == 0)
        {
            values[random(0, points.size - 1)] = 10f
        }

        grid.values(*values)
        Thread.sleep(16)

        for (index in 0 until values.size)
        {
            values[index] = if (values[index] > 0f) values[index] - 0.1f else 0f
        }
    }

    var loop : Boolean

    do
    {
        loop = false

        for (index in 0 until values.size)
        {
            if (values[index] > 0f)
            {
                values[index] = values[index] - 0.1f
                loop = true
            }
        }

        grid.values(*values)
        Thread.sleep(16)
    }
    while (loop)
}