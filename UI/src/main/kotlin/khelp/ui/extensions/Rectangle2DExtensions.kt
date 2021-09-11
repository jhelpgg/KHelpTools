package khelp.ui.extensions

import java.awt.geom.Rectangle2D

val Rectangle2D.area : Double get() = this.width * this.height

fun Rectangle2D.near(rectangle2D : Rectangle2D, precision : Double = 1.0) : Boolean =
    (this.x - rectangle2D.x) < precision && (this.y - rectangle2D.y) < precision &&
    (this.maxX - rectangle2D.maxX) < precision && (this.maxY - rectangle2D.maxY) < precision