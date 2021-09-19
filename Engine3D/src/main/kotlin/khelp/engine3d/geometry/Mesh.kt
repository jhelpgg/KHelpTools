package khelp.engine3d.geometry

import khelp.engine3d.render.MeshDSL
import khelp.engine3d.utils.BarycenterPoint3D
import khelp.engine3d.utils.ThreadOpenGL

class Mesh
{
    private val faces = ArrayList<Face>()
    private val barycenter = BarycenterPoint3D()
    val center : Point3D = this.barycenter.barycenter()
    val virtualBox: VirtualBox = VirtualBox()

    @MeshDSL
    fun face(faceCreator : Face.() -> Unit)
    {
        val face = Face(this.barycenter, this.virtualBox)
        faceCreator(face)
        this.faces.add(face)
    }

    fun clear()
    {
        this.faces.clear()
    }

    @ThreadOpenGL
    internal fun render()
    {
        for (face in this.faces)
        {
            face.render()
        }
    }
}
