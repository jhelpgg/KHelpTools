package khelp.algorithm.delaunay

import javax.swing.JFrame
import khelp.ui.utilities.takeAllScreen
import khelp.utilities.math.square
import kotlin.math.sqrt

fun main()
{
    val delaunay = Delaunay()
    val circles = ArrayList<Circle>()
    val ray = DelaunaySpyComponent.RAY * 2.1f

    for (count in 0 until 500)
    {
        val (x, y) = point(circles)
        delaunay.addPoint(x, y)
        circles.add(Circle(x, y, ray))
    }
    for (count in 0 until 50)
    {
        val (x, y) = point(circles)
        delaunay.addIgnorePoint(x, y)
        circles.add(Circle(x, y, ray))
    }

    val frame = JFrame("Delaunay")
    val component = DelaunaySpyComponent(delaunay, 100)
    frame.contentPane.add(component)
    frame.isUndecorated = true
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    takeAllScreen(frame)
    frame.isVisible = true
}

private class Circle(val centerX : Float, val centerY : Float, val radius : Float)
{
    fun contains(x : Float, y : Float) : Boolean =
        sqrt(square(this.centerX - x) + square(this.centerY - y)) <= this.radius
}

private fun inOneCircle(x : Float, y : Float, circles : List<Circle>) : Boolean
{
    for (circle in circles)
    {
        if (circle.contains(x, y))
        {
            return true
        }
    }

    return false
}

private fun point(circles : List<Circle>) : Pair<Float, Float>
{
    var x = 0f
    var y = 0f

    do
    {
        x = 100f + (Math.random() * 1900).toFloat()
        y = 100f + (Math.random() * 900).toFloat()
    }
    while (inOneCircle(x, y, circles))

    return Pair(x, y)
}