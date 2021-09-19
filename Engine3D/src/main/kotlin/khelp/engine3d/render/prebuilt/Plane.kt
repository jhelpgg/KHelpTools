package khelp.engine3d.render.prebuilt

import khelp.engine3d.render.Object3D
import khelp.engine3d.render.TwoSidedRule

class Plane(id : String, faceUV : FaceUV = FaceUV()) : Object3D(id)
{
    init
    {
        this.twoSidedRule = TwoSidedRule.FORCE_TWO_SIDE

        mesh {
            face {
                add(- 0.5f, 0.5f, 0f,
                    faceUV.minU, faceUV.minV,
                    0f, 0f, - 1f)
                add(0.5f, 0.5f, 0f,
                    faceUV.maxU, faceUV.minV,
                    0f, 0f, - 1f)
                add(0.5f, - 0.5f, 0f,
                    faceUV.maxU, faceUV.maxV,
                    0f, 0f, - 1f)
                add(- 0.5f, - 0.5f, 0f,
                    faceUV.minU, faceUV.maxV,
                    0f, 0f, - 1f)
            }
        }
    }
}
