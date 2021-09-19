package khelp.engine3d.geometry

import khelp.engine3d.utils.BarycenterPoint3D
import khelp.engine3d.utils.ThreadOpenGL
import org.lwjgl.opengl.GL11

class Face internal constructor(internal val barycenter : BarycenterPoint3D, internal val virtualBox : VirtualBox)
{
    private val vertices = ArrayList<Vertex>()

    fun add(x : Float, y : Float, z : Float,
            uvU : Float, uvV : Float,
            normalX : Float, normalY : Float, normalZ : Float)
    {
        this.barycenter.add(x.toDouble(), y.toDouble(), z.toDouble())
        this.virtualBox.add(x, y, z)
        this.vertices.add(Vertex(Point3D(x, y, z),
                                 Point2D(uvU, uvV),
                                 Point3D(normalX, normalY, normalZ)))
    }

    fun add(position : Point3D, uv : Point2D, normal : Point3D)
    {
        this.barycenter.add(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())
        this.virtualBox.add(position.x, position.y, position.z)
        this.vertices.add(Vertex(position, uv, normal))
    }

    fun add(vertex : Vertex)
    {
        this.barycenter.add(vertex.position.x.toDouble(), vertex.position.y.toDouble(), vertex.position.z.toDouble())
        this.virtualBox.add(vertex.position.x, vertex.position.y, vertex.position.z)
        this.vertices.add(vertex)
    }

    @ThreadOpenGL
    internal fun render()
    {
        GL11.glBegin(GL11.GL_POLYGON)

        for (vertex in this.vertices)
        {
            vertex.render()
        }

        GL11.glEnd()
    }
}