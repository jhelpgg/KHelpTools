package khelp.engine3d.geometry

import khelp.engine3d.render.MeshDSL
import khelp.engine3d.utils.ThreadOpenGL

class Mesh
{
    private val faces = ArrayList<Face>()

    @MeshDSL
    fun face(faceCreator : Face.() -> Unit)
    {
        val face = Face()
        faceCreator(face)
        this.faces.add(face)
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
