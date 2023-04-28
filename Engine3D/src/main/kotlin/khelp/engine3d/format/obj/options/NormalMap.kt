package khelp.engine3d.format.obj.options

import khelp.engine3d.extensions.blue
import khelp.engine3d.extensions.green
import khelp.engine3d.extensions.red
import khelp.engine3d.geometry.Point3D
import kotlin.math.floor

class NormalMap(val width : Int, val height : Int, val pixels : IntArray)
{
    operator fun get(u : Float, v : Float) : Point3D
    {
        val x = ((u - floor(u)) * this.width).toInt()
        val y = ((v - floor(v)) * this.height).toInt()
        val pixel = this.pixels[x + y * this.width]
        val red = pixel.red - 128
        val green = pixel.green - 128
        val blue = 128 - pixel.blue
        return Point3D(red.toFloat() / 127.0f, green.toFloat() / 127.0f, blue.toFloat() / 127.0f)
    }
}