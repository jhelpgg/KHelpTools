package khelp.engine3d.render.prebuilt

import khelp.engine3d.geometry.Point2D
import khelp.engine3d.geometry.Point3D
import khelp.engine3d.geometry.Rotf
import khelp.engine3d.geometry.Vec3f
import khelp.engine3d.geometry.Vertex
import khelp.engine3d.geometry.path.Path
import khelp.engine3d.render.Object3D
import khelp.engine3d.render.TwoSidedRule
import khelp.math.formal.Function
import khelp.math.formal.Variable
import khelp.math.formal.toFunction
import khelp.utilities.math.isNul
import khelp.utilities.math.sign

/**
 * Variable **t** that:
 *
 * * `X(t) : functionX`
 * * `Y(t) : functionY`
 * * `Z(t) : functionZ`
 *
 * must only depends
 */
val VARIABLE_T = Variable("t")

/**
 * Check if given function depends only on [T] variable
 * @param function Checked function
 * @throws IllegalArgumentException If given function not depends only on [T]
 */
@Throws(IllegalArgumentException::class)
private fun checkOnlyUseT(function : Function)
{
    val variables = function.variableList()

    if (variables.size > 1)
    {
        throw IllegalArgumentException("$function not depends only on t")
    }

    if (variables.size == 1 && variables[0] != VARIABLE_T)
    {
        throw IllegalArgumentException("$function not depends on t")
    }
}

/**
 * Create object 3D along a 3D parametric equation.
 *
 * It is a path repeat along the equation.
 *
 * Each equation must depends only on **t** ([VARIABLE_T])
 * @param border Path repeat along the equation
 * @param borderPrecision Precision used for draw the path
 * @param tStart **t** start value
 * @param tEnd **t** end value
 * @param tStep **t** step size for go from [tStart] tho [tEnd]
 * @param functionX Equation on **t** for coordinates X: X(t)
 * @param functionY Equation on **t** for coordinates Y: Y(t)
 * @param functionZ Equation on **t** for coordinates Z: Z(t)
 * @param equation3DListener Call back to alert when object is complete
 * @throws IllegalArgumentException If one equation not depends only of **t** or [tStart], [tEnd] and [tStep] aren't coherent
 */
class Equation3D(id : String,
                 val border : Path, val borderPrecision : Int = 12,
                 val tStart : Float, val tEnd : Float, val tStep : Float,
                 functionX : Function, functionY : Function, functionZ : Function) : Object3D(id)
{
    constructor(id : String,
                border : Path, borderPrecision : Int = 12,
                tStart : Float, tEnd : Float, tStep : Float,
                functionX : String, functionY : String, functionZ : String)
            : this(id,
                   border, borderPrecision,
                   tStart, tEnd, tStep,
                   functionX.toFunction(), functionY.toFunction(), functionZ.toFunction())

    val functionX = functionX()
    val functionY = functionY()
    val functionZ = functionZ()

    init
    {
        checkOnlyUseT(this.functionX)
        checkOnlyUseT(this.functionY)
        checkOnlyUseT(this.functionZ)

        if (isNul(this.tStep) || sign(this.tEnd - this.tStart) != sign(this.tStep))
        {
            throw IllegalArgumentException(
                "tStart=${this.tStart}, tEnd=${this.tEnd} and tStep=${this.tStep} aren't coherent. Those values don't permit to goes from tStart to tEnd")
        }

        this.twoSidedRule = TwoSidedRule.FORCE_TWO_SIDE

        val deriveX = this.functionX.derive(VARIABLE_T)()
        val deriveY = this.functionY.derive(VARIABLE_T)()
        val deriveZ = this.functionZ.derive(VARIABLE_T)()
        val lines = this.border.path(this.borderPrecision)
        val size = lines.size
        val axisZ = Vec3f(0f, 0f, 1f)
        val limit = this.border.border()
        val minU = limit.x.toFloat()
        val minV = limit.y.toFloat()
        val multU = 1f / limit.width.toFloat()
        val multV = 1f / limit.height.toFloat()
        var t = this.tStart

        this.mesh {
            while (t < tEnd)
            {
                var dx = deriveX.replace(VARIABLE_T, t.toDouble())().obtainRealValueNumber()
                var dy = deriveY.replace(VARIABLE_T, t.toDouble())().obtainRealValueNumber()
                var dz = deriveZ.replace(VARIABLE_T, t.toDouble())().obtainRealValueNumber()
                var derivateVectorLength = Math.sqrt(dx * dx + dy * dy + dz * dz)

                if (! isNul(derivateVectorLength))
                {
                    dx /= derivateVectorLength
                    dy /= derivateVectorLength
                    dz /= derivateVectorLength
                }

                var normal = Vec3f(dx.toFloat(), dy.toFloat(), dz.toFloat())
                val rotationEdge = Rotf(axisZ, normal)

                val x = functionX.replace(VARIABLE_T, t.toDouble())().obtainRealValueNumber()
                    .toFloat()
                val y = functionY.replace(VARIABLE_T, t.toDouble())().obtainRealValueNumber()
                    .toFloat()
                val z = functionZ.replace(VARIABLE_T, t.toDouble())().obtainRealValueNumber()
                    .toFloat()

                //

                var dx2 = deriveX.replace(VARIABLE_T, t.toDouble() + tStep)().obtainRealValueNumber()
                var dy2 = deriveY.replace(VARIABLE_T, t.toDouble() + tStep)().obtainRealValueNumber()
                var dz2 = deriveZ.replace(VARIABLE_T, t.toDouble() + tStep)().obtainRealValueNumber()
                derivateVectorLength = Math.sqrt(dx2 * dx2 + dy2 * dy2 + dz2 * dz2)

                if (! isNul(derivateVectorLength))
                {
                    dx2 /= derivateVectorLength
                    dy2 /= derivateVectorLength
                    dz2 /= derivateVectorLength
                }

                normal = Vec3f(dx2.toFloat(), dy2.toFloat(), dz2.toFloat())
                val rotationEdge2 = Rotf(axisZ, normal)

                val x2 = functionX.replace(VARIABLE_T, t.toDouble() + tStep)().obtainRealValueNumber()
                    .toFloat()
                val y2 = functionY.replace(VARIABLE_T, t.toDouble() + tStep)().obtainRealValueNumber()
                    .toFloat()
                val z2 = functionZ.replace(VARIABLE_T, t.toDouble() + tStep)().obtainRealValueNumber()
                    .toFloat()

                //

                for (lig in 0 until size)
                {
                    val line = lines[lig]

                    //Start
                    val xStart = line.x1
                    val yStart = line.y1
                    var point = Vec3f(xStart, yStart, 0f)
                    point = rotationEdge.rotateVector(point)

                    val vertex00 = Vertex(Point3D(point.x + x, point.y + y, point.z + z),
                                          Point2D((xStart - minU) * multU, (yStart - minV) * multV),
                                          Point3D(- point.x, - point.y, - point.z))

                    //End
                    val xEnd = line.x2
                    val yEnd = line.y2
                    point = Vec3f(xEnd, yEnd, 0f)
                    point = rotationEdge.rotateVector(point)

                    val vertex01 = Vertex(Point3D(point.x + x, point.y + y, point.z + z),
                                          Point2D((xEnd - minU) * multU, (yEnd - minV) * multV),
                                          Point3D(- point.x, - point.y, - point.z))

                    // ---*---

                    // Start
                    point = Vec3f(xStart, yStart, 0f)
                    point = rotationEdge2.rotateVector(point)

                    val vertex10 = Vertex(Point3D(point.x + x2, point.y + y2, point.z + z2),
                                          Point2D((xStart - minU) * multU, (yStart - minV) * multV),
                                          Point3D(- point.x, - point.y, - point.z))

                    //End
                    point = Vec3f(xEnd, yEnd, 0f)
                    point = rotationEdge2.rotateVector(point)

                    val vertex11 = Vertex(Point3D(point.x + x2, point.y + y2, point.z + z2),
                                          Point2D((xEnd - minU) * multU, (yEnd - minV) * multV),
                                          Point3D(- point.x, - point.y, - point.z))

                    // ---*---

                    face {
                        add(vertex10)
                        add(vertex11)
                        add(vertex01)
                        add(vertex00)
                    }
                }

                t += tStep
            }
        }

        //this.computeUVspherical(1f, 1f)
    }
}