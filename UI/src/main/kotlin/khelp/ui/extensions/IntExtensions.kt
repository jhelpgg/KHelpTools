package khelp.ui.extensions

import khelp.utilities.extensions.blue
import khelp.utilities.extensions.green
import khelp.utilities.extensions.red

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
val Int.y : Double get() = this.red * 0.299 + this.green * 0.587 + this.blue * 0.114

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
val Int.u: Double get() = -0.169 * this.red - 0.331 * this.green + 0.500 * this.blue + 128.0

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
val Int.v: Double get() = 0.500 * this.red - 0.419 * this.green - 0.081 * this.blue + 128.0
