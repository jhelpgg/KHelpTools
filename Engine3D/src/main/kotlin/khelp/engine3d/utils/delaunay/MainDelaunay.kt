package khelp.engine3d.utils.delaunay

import javax.swing.JFrame

fun main()
{
    val delaunay = Delaunay()
    delaunay.addPoint(164f,164f)
    delaunay.addPoint(173f,420f)
    delaunay.addPoint(120f,185f)
    delaunay.addPoint(850f,512f)
    delaunay.addPoint(400f,173f)
    val frame = JFrame("Delaunay")
    val component = DelaunaySpyComponent(delaunay)
    frame.contentPane.add(component)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.pack()
    frame.isVisible = true
}