package khelp.engine3d.render.prebuilt

import khelp.engine3d.render.Object3D
import khelp.engine3d.render.TwoSidedRule
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

class Sphere(id : String, slice : Int = 33, stack : Int = 33, multiplierU : Float = 1f,
             multiplierV : Float = 1f) : Object3D(id)
{
    init
    {
        this.twoSidedRule = TwoSidedRule.FORCE_ONE_SIDE

        mesh {
            val slice = max(2, slice)
            val stack = max(2, stack)

            // Angles compute for slice and stack
            var sliceAngle : Double
            var stackAngle : Double
            var sliceAngleFuture : Double
            var stackAngleFuture : Double

            // Cosinus and sinus of angles
            var cosSliceAngle : Double
            var cosStackAngle : Double
            var cosSliceAngleFuture : Double
            var cosStackAngleFuture : Double
            var sinSliceAngle : Double
            var sinStackAngle : Double
            var sinSliceAngleFuture : Double
            var sinStackAngleFuture : Double

            // Computed UV
            var uA : Float
            var vA : Float
            var uF : Float
            var vF : Float

            // Computed normals
            var nxAA : Float
            var nyAA : Float
            var nzAA : Float
            var nxFA : Float
            var nyFA : Float
            var nzFA : Float
            var nxAF : Float
            var nyAF : Float
            var nzAF : Float
            var nxFF : Float
            var nyFF : Float
            var nzFF : Float

            // To walk throw slice and stack
            var sli : Int
            var sta : Int

            // For each slice
            sli = 0

            while (sli < slice)
            {
                // Compute slice angles, cosinus and sinus
                sliceAngle = 2.0 * Math.PI * sli.toDouble() / slice - Math.PI
                sliceAngleFuture = 2.0 * Math.PI * (sli + 1).toDouble() / slice - Math.PI
                //
                cosSliceAngle = Math.cos(sliceAngle)
                cosSliceAngleFuture = Math.cos(sliceAngleFuture)
                sinSliceAngle = Math.sin(sliceAngle)
                sinSliceAngleFuture = Math.sin(sliceAngleFuture)

                // Computes U (Slice walk throw U)
                uA = 1 - multiplierU + multiplierU * sli / slice
                uF = 1 - multiplierU + multiplierU * (sli + 1) / slice

                // For each stack
                sta = 0

                while (sta < stack)
                {
                    // Compute stack angles, cosinus and sinus
                    stackAngle = Math.PI * sta / stack - Math.PI / 2.0
                    stackAngleFuture = Math.PI * (sta + 1) / stack - Math.PI / 2.0
                    //
                    cosStackAngle = cos(stackAngle)
                    cosStackAngleFuture = cos(stackAngleFuture)
                    sinStackAngle = sin(stackAngle)
                    sinStackAngleFuture = sin(stackAngleFuture)

                    // Computes V (Stack walk throw V)
                    vA = 1 - multiplierV * sta / stack
                    vF = 1 - multiplierV * (sta + 1) / stack

                    // Computes normals
                    nxAA = (sinSliceAngle * cosStackAngle).toFloat()
                    nyAA = sinStackAngle.toFloat()
                    nzAA = (cosSliceAngle * cosStackAngle).toFloat()

                    nxFA = (sinSliceAngleFuture * cosStackAngle).toFloat()
                    nyFA = sinStackAngle.toFloat()
                    nzFA = (cosSliceAngleFuture * cosStackAngle).toFloat()

                    nxAF = (sinSliceAngle * cosStackAngleFuture).toFloat()
                    nyAF = sinStackAngleFuture.toFloat()
                    nzAF = (cosSliceAngle * cosStackAngleFuture).toFloat()

                    nxFF = (sinSliceAngleFuture * cosStackAngleFuture).toFloat()
                    nyFF = sinStackAngleFuture.toFloat()
                    nzFF = (cosSliceAngleFuture * cosStackAngleFuture).toFloat()

                    face {
                        add(nxAA, nyAA, nzAA,
                            uA, vA,
                            - nxAA, - nyAA, - nzAA)

                        add(nxAF, nyAF, nzAF,
                            uA, vF,
                            - nxAF, - nyAF, - nzAF)

                        add(nxFF, nyFF, nzFF,
                            uF, vF,
                            - nxFF, - nyFF, - nzFF)

                        add(nxFA, nyFA, nzFA,
                            uF, vA,
                            - nxFA, - nyFA, - nzFA)
                    }

                    sta ++
                }

                sli ++
            }
        }
    }
}
