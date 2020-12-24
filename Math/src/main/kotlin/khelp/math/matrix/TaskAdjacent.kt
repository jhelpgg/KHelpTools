package khelp.math.matrix

/**
 * Parameters for adjacent task
 * @param adjacent Adjacent matrix where write
 * @param sign Sign factor
 * @param index Adjacent computed cell index
 * @param x Adjacent cell X
 */
internal class TaskAdjacent(val adjacent: DoubleArray,
                            val sign: Double,
                            val index: Int,
                            val x: Int)
