package khelp.ui.extensions

import khelp.ui.utilities.yuvToBlue
import khelp.ui.utilities.yuvToGreen
import khelp.ui.utilities.yuvToRed
import khelp.utilities.extensions.limit_0_255
import java.awt.Color

/**
 * Compute Y of a color
 *
 * Y = R * 0.299000 + G * 0.587000 + B * 0.114000
 *
 * @param red   Red part
 * @param green Green part
 * @param blue  Blue part
 * @return Y
 */
val Color.y : Double get() = this.red * 0.299 + this.green * 0.587 + this.blue * 0.114

/**
 * Compute U of a color
 *
 * U = R * -0.168736 + G * -0.331264 + B * 0.500000 + 128
 *
 * @param red   Red part
 * @param green Green part
 * @param blue  Blue part
 * @return U
 */
val Color.u : Double get() = - 0.169 * this.red - 0.331 * this.green + 0.500 * this.blue + 128.0

/**
 * Compute V of a color
 *
 * V = R * 0.500000 + G * -0.418688 + B * -0.081312 + 128
 *
 * @param red   Red part
 * @param green Green part
 * @param blue  Blue part
 * @return V
 */
val Color.v : Double get() = 0.500 * this.red - 0.419 * this.green - 0.081 * this.blue + 128.0

val Color.invert : Color get() = Color(255 - this.red, 255 - this.green, 255 - this.blue, this.alpha)

val Color.grey : Color
    get()
    {
        val y = this.y.toInt().limit_0_255
        return Color(y, y, y, this.alpha)
    }

fun Color.darker(darker : Int) : Color =
    Color((this.red - darker).limit_0_255, (this.green - darker).limit_0_255, (this.blue - darker).limit_0_255,
          this.alpha)

fun Color.lighter(lighter : Int) : Color =
    Color((this.red + lighter).limit_0_255, (this.green + lighter).limit_0_255, (this.blue + lighter).limit_0_255,
          this.alpha)

fun Color.contrast(contrast : Double) : Color
{
    val y = this.y * contrast
    val u = this.u
    val v = this.v
    val red = yuvToRed(y, u, v)
    val green = yuvToGreen(y, u, v)
    val blue = yuvToBlue(y, u, v)
    return Color(red, green, blue, this.alpha)
}

val Color.nearInvisible : Color get() = Color(this.red, this.green, this.blue, 64)

val Color.semiVisible : Color get() = Color(this.red, this.green, this.blue, 128)

val Color.almostVisible : Color get() = Color(this.red, this.green, this.blue, 192)

val Color.opaque : Color get() = Color(this.red, this.green, this.blue, 255)



