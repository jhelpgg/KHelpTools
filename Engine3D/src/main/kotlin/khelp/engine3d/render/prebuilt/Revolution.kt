package khelp.engine3d.render.prebuilt

import khelp.engine3d.extensions.degreeToRadian
import khelp.engine3d.geometry.path.Path
import khelp.engine3d.render.MeshDSL
import khelp.engine3d.render.Object3D
import khelp.utilities.extensions.bounds
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Revolution(id : String) : Object3D(id)
{
    @MeshDSL
    fun path(precision : Int = 5,
             angle : Float = 360f, rotationPrecision : Int = 12,
             start : Float = 0f, end : Float = 1f,
             multiplierU : Float = 1f,
             pathCreator : Path.() -> Unit)
    {
        val path = Path()
        pathCreator(path)
        this.path(path, precision.bounds(2, 12),
                  start, end,
                  angle.bounds(1f, 360f), rotationPrecision.bounds(3, 32),
                  multiplierU)
    }

    private fun path(path : Path, precision : Int,
                     start : Float, end : Float,
                     revolutionAngle : Float, rotationPrecision : Int,
                     multiplierU : Float)
    {
        this.mesh {
            clear()
            val lines = path.path(precision, start, end)
            val radian = revolutionAngle.degreeToRadian

            var angle : Float
            var angleFuture : Float
            var cos : Float
            var cosFuture : Float
            var sin : Float
            var sinFuture : Float
            var x0 : Float
            var y0 : Float
            var x1 : Float
            var y1 : Float
            var vx : Float
            var vy : Float
            var u0 : Float
            var u1 : Float
            var v0 : Float
            var v1 : Float
            var length : Float

            var xAA : Float
            var yAA : Float
            var zAA : Float
            var uAA : Float
            var vAA : Float
            var nxAA : Float
            var nyAA : Float
            var nzAA : Float
            var xAF : Float
            var yAF : Float
            var zAF : Float
            var uAF : Float
            var vAF : Float
            var nxAF : Float
            var nyAF : Float
            var nzAF : Float
            var xFA : Float
            var yFA : Float
            var zFA : Float
            var uFA : Float
            var vFA : Float
            var nxFA : Float
            var nyFA : Float
            var nzFA : Float
            var xFF : Float
            var yFF : Float
            var zFF : Float
            var uFF : Float
            var vFF : Float
            var nxFF : Float
            var nyFF : Float
            var nzFF : Float

            var an : Int

            // For each line of the path
            for (pathLine in lines)
            {
                // Get start and end point
                x0 = pathLine.x1
                y0 = pathLine.y1
                v0 = pathLine.information1

                x1 = pathLine.x2
                y1 = pathLine.y2
                v1 = pathLine.information2

                // Compute the vector start to end and normalize it
                vx = x1 - x0
                vy = y1 - y0

                length = sqrt(vx * vx + vy * vy)
                if (abs(length) >= 1e-5f)
                {
                    vx /= length
                    vy /= length
                }

                // For each rotation step
                an = 0
                while (an < rotationPrecision)
                {
                    // Compute U
                    u0 = an * multiplierU / rotationPrecision
                    u1 = (an + 1f) * multiplierU / rotationPrecision

                    // Compute angles, cosinus and sinus
                    angle = radian * an / rotationPrecision
                    angleFuture = radian * (an + 1) / rotationPrecision

                    cos = cos(angle)
                    sin = sin(angle)
                    cosFuture = cos(angleFuture)
                    sinFuture = sin(angleFuture)

                    // Compute each vertex
                    xAA = (cos * x0)
                    yAA = y0
                    zAA = (- sin * x0)
                    uAA = u0
                    vAA = v0
                    nxAA = (cos * vy)
                    nyAA = vx
                    nzAA = (- sin * vy)

                    xAF = (cos * x1)
                    yAF = y1
                    zAF = (- sin * x1)
                    uAF = u0
                    vAF = v1
                    nxAF = (cos * vy)
                    nyAF = vx
                    nzAF = (- sin * vy)

                    xFA = (cosFuture * x0)
                    yFA = y0
                    zFA = (- sinFuture * x0)
                    uFA = u1
                    vFA = v0
                    nxFA = (cosFuture * vy)
                    nyFA = vx
                    nzFA = (- sinFuture * vy)

                    xFF = (cosFuture * x1)
                    yFF = y1
                    zFF = (- sinFuture * x1)
                    uFF = u1
                    vFF = v1
                    nxFF = (cosFuture * vy)
                    nyFF = vx
                    nzFF = (- sinFuture * vy)

                    // Draw the face
                    face {
                        add(xAA, yAA, zAA,
                            uAA, vAA,
                            nxAA, nyAA, nzAA)
                        add(xFA, yFA, zFA,
                            uFA, vFA,
                            nxFA, nyFA, nzFA)
                        add(xFF, yFF, zFF,
                            uFF, vFF,
                            nxFF, nyFF, nzFF)
                        add(xAF, yAF, zAF,
                            uAF, vAF,
                            nxAF, nyAF, nzAF)
                    }

                    an ++
                }
            }
        }
    }
}
