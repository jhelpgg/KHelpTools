package khelp.engine3d.format.obj

import khelp.engine3d.geometry.Point2D
import khelp.engine3d.geometry.Point3D

internal class ObjObject(var name : String)
{
    val points = ArrayList<Point3D>()
    val uvs = ArrayList<Point2D>()
    val normals = ArrayList<Point3D>()
    val faces = ArrayList<ObjFace>()

    fun subObject(name:String) : ObjObject {
        val subObject = ObjObject(name)
        subObject.points.addAll(this.points)
        subObject.uvs.addAll(this.uvs)
        subObject.normals.addAll(this.normals)
        return subObject
    }
}
