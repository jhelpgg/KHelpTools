package khelp.engine3d.geometry

import khelp.engine3d.utils.BarycenterPoint3D
import khelp.engine3d.utils.ThreadOpenGL
import khelp.utilities.extensions.transform
import org.lwjgl.opengl.GL11

class Face internal constructor(private val barycenter : BarycenterPoint3D, private val virtualBox : VirtualBox)
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

    internal fun movePoint(point : Point3D, vector : Point3D, solidity : Float, solidityFactor : Float, near : Int)
    {
        val indexStart = this.vertices.indexOfFirst { vertex -> vertex.position == point }

        if (indexStart < 0)
        {
            return
        }

        var vertex = this.vertices[indexStart]
        this.vertices[indexStart] = Vertex(vertex.position + vector, vertex.uv, vertex.normal)
        var newVector = vector
        var newSolidity = solidity
        var indexForward = indexStart
        var indexBackward = indexStart
        var notChanged : Boolean
        val size = this.vertices.size

        for (time in 0 until near)
        {
            notChanged = true
            newVector *= newSolidity
            newSolidity *= solidityFactor

            indexForward = (indexForward + 1) % size

            if (indexForward != indexStart)
            {
                notChanged = false
                vertex = this.vertices[indexForward]
                this.vertices[indexForward] = Vertex(vertex.position + newVector, vertex.uv, vertex.normal)
            }

            indexBackward = (indexBackward + size - 1) % size


            if (indexBackward != indexStart)
            {
                notChanged = false
                vertex = this.vertices[indexBackward]
                this.vertices[indexBackward] = Vertex(vertex.position + newVector, vertex.uv, vertex.normal)
            }

            if (notChanged)
            {
                break
            }
        }
    }

    internal fun refillBarycenterAndVirtualBox()
    {
        for (point in this.vertices.transform { vertex -> vertex.position })
        {
            this.barycenter.add(point)
            this.virtualBox.add(point)
        }
    }
}