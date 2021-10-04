package khelp.engine3d.geometry

import khelp.engine3d.animation.SinusInterpolation
import khelp.engine3d.render.MeshDSL
import khelp.engine3d.utils.BarycenterPoint3D
import khelp.engine3d.utils.ThreadOpenGL
import khelp.utilities.serialization.ParsableSerializable
import khelp.utilities.serialization.Parser
import khelp.utilities.serialization.Serializer

class Mesh : ParsableSerializable
{
    companion object
    {
        fun provider() : Mesh = Mesh()
    }

    private val faces = ArrayList<Face>()
    private val barycenter = BarycenterPoint3D()
    val center : Point3D get() = this.barycenter.barycenter()
    val virtualBox : VirtualBox = VirtualBox()

    @MeshDSL
    fun face(faceCreator : Face.() -> Unit)
    {
        val face = Face()
        face.barycenter = this.barycenter
        face.virtualBox = this.virtualBox
        faceCreator(face)
        this.faces.add(face)
    }

    fun movePoint(point : Point3D, vector : Point3D, solidity : Float, near : Int)
    {
        this.barycenter.reset()
        this.virtualBox.reset()
        val factor = SinusInterpolation(solidity.toDouble()).toFloat()

        for (face in this.faces)
        {
            face.movePoint(point, vector, solidity, factor, near)
            face.refillBarycenterAndVirtualBox()
        }
    }

    fun clear()
    {
        this.virtualBox.reset()
        this.barycenter.reset()
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

    override fun serialize(serializer : Serializer)
    {
        serializer.setParsableSerializableList("faces", this.faces)
    }

    override fun parse(parser : Parser)
    {
        this.faces.clear()
        parser.appendParsableSerializableList("faces", this.faces, Face::provider)

        this.barycenter.reset()
        this.virtualBox.reset()

        for (face in this.faces)
        {
            face.barycenter = this.barycenter
            face.virtualBox = this.virtualBox
            face.refillBarycenterAndVirtualBox()
        }
    }
}
