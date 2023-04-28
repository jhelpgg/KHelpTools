package khelp.ui.utilities

import khelp.utilities.extensions.limit_0_255
import java.awt.Color

val TRANSPARENT = Color(0, true)

val SHADOW = Color(0x40808080, true)

/**
 * Compute red part of color from YUV
 *
 * @param y Y
 * @param u U
 * @param v V
 * @return Red part
 */
fun yuvToRed(y : Double, u : Double, v : Double) : Int =
    (y - 0.0009267 * (u - 128) + 1.4016868 * (v - 128)).toInt()
        .limit_0_255

/**
 * Compute green part of color from YUV
 *
 * @param y Y
 * @param u U
 * @param v V
 * @return Green part
 */
fun yuvToGreen(y : Double, u : Double, v : Double) : Int =
    (y - 0.3436954 * (u - 128) - 0.7141690 * (v - 128)).toInt()
        .limit_0_255

/**
 * Compute blue part of color from YUV
 *
 * @param y Y
 * @param u U
 * @param v V
 * @return Blue part
 */
fun yuvToBlue(y : Double, u : Double, v : Double) : Int =
    (y + 1.7721604 * (u - 128) + 0.0009902 * (v - 128)).toInt()
        .limit_0_255

fun yuv(y : Double, u : Double, v : Double) : Color =
    Color(yuvToRed(y, u, v), yuvToGreen(y, u, v), yuvToBlue(y, u, v))
