package khelp.engine3d.extensions

import kotlin.math.PI

const val PI_FLOAT : Float = PI.toFloat()

val Float.degreeToRadian : Float get() = (this * PI_FLOAT) * 180f
val Float.radianToDegree : Float get() = (this * 180f) / PI_FLOAT
