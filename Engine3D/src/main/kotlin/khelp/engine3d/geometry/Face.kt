package khelp.engine3d.geometry

import khelp.engine3d.utils.ThreadOpenGL
import org.lwjgl.opengl.GL11

class Face
{
    private val vertices = ArrayList<Vertex>()

    fun add(x : Float, y : Float, z : Float,
            uvU : Float, uvV : Float,
            normalX : Float, normalY : Float, normalZ : Float)
    {
        this.vertices.add(Vertex(Point3D(x, y, z),
                                 Point2D(uvU, uvV),
                                 Point3D(normalX, normalY, normalZ)))
    }

    fun add(position : Point3D, uv : Point2D, normal : Point3D)
    {
        this.vertices.add(Vertex(position, uv, normal))
    }

    fun add(vertex : Vertex)
    {
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