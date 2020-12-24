package khelp.math.matrix


/**
 * Parameters for multiplication task
 * @param x Cell X
 * @param y Cell Y
 * @param firstWidth First matrix width
 * @param secondWidth Second matrix width
 * @param resultWidth Result matrix width
 * @param result Result matrix data
 * @param first First matrix data
 * @param second Second matrix data
 */
internal class TaskMultiplicationCell(val x: Int,
                                      val y: Int,
                                      val firstWidth: Int,
                                      val secondWidth: Int,
                                      val resultWidth: Int,
                                      val result: DoubleArray,
                                      val first: DoubleArray,
                                      val second: DoubleArray)
