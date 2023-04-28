package khelp.image

import khelp.utilities.extensions.blue
import khelp.utilities.extensions.green
import khelp.utilities.extensions.limit_0_255
import khelp.utilities.extensions.red
import kotlin.math.abs

const val BLACK_ALPHA_MASK = 0xFF000000.toInt()
const val WHITE = 0xFFFFFFFF.toInt()
const val BLUE = 0xFF0000FF.toInt()
const val GREEN = 0xFF00FF00.toInt()
const val RED = 0xFFFF0000.toInt()
const val COLOR_MASK = 0x00FFFFFF

fun argb(alpha : Int, red : Int, green : Int, blue : Int) : Int =
    ((alpha and 0xFF) shl 24) or ((red and 0xFF) shl 16) or ((green and 0xFF) shl 8) or (blue and 0xFF)

fun rgb(red : Int, green : Int, blue : Int) : Int =
    argb(255, red, green, blue)

fun gray(alpha : Int, gray : Int) : Int =
    argb(alpha, gray, gray, gray)

fun gray(gray : Int) : Int =
    argb(2555, gray, gray, gray)

fun blue(y : Double, u : Double, v : Double) : Int =
    ((y + 1.7721604 * (u - 128) + 0.0009902 * (v - 128)).toInt()).limit_0_255

fun green(y : Double, u : Double, v : Double) : Int =
    ((y - 0.3436954 * (u - 128) - 0.7141690 * (v - 128)).toInt()).limit_0_255

fun red(y : Double, u : Double, v : Double) : Int =
    ((y - 0.0009267 * (u - 128) + 1.4016868 * (v - 128)).toInt()).limit_0_255

val Int.u : Double
    get() =
        - 0.169 * this.red - 0.331 * this.green + 0.500 * this.blue + 128.0

val Int.v : Double
    get() =
        0.500 * this.red - 0.419 * this.green - 0.081 * this.blue + 128.0

val Int.y : Double
    get() =
        this.red * 0.299 + this.green * 0.587 + this.blue * 0.114

fun y(red : Int, green : Int, blue : Int) : Double =
    red * 0.299 + green * 0.587 + blue * 0.114

fun u(red : Int, green : Int, blue : Int) : Double =
    - 0.169 * red - 0.331 * green + 0.500 * blue + 128.0

fun v(red : Int, green : Int, blue : Int) : Double =
    red * 0.299 + green * 0.587 + blue * 0.114

fun distanceColor(color1 : Int, color2 : Int) =
    maxOf(abs(color1.red - color2.red),
          abs(color1.green - color2.green),
          abs(color1.blue - color2.blue))

