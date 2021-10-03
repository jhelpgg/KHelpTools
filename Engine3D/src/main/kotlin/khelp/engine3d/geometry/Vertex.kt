package khelp.engine3d.geometry

import khelp.engine3d.utils.ThreadOpenGL
import khelp.utilities.serialization.ParsableSerializable
import khelp.utilities.serialization.Parser
import khelp.utilities.serialization.Serializer

class Vertex(position : Point3D, uv : Point2D, normal : Point3D) : ParsableSerializable
{
    companion object
    {
        fun provider() : Vertex = Vertex(Point3D(), Point2D(), Point3D())
    }

    var position = position
        private set
    var uv = uv
        private set
    var normal = normal
        private set

    @ThreadOpenGL
    internal fun render()
    {
        this.normal.glNormal3f()
        this.uv.glTexCoord2f()
        this.position.glVertex3f()
    }

    override fun serialize(serializer : Serializer)
    {
        serializer.setParsableSerializable("position", this.position)
        serializer.setParsableSerializable("uv", this.uv)
        serializer.setParsableSerializable("normal", this.normal)
    }

    override fun parse(parser : Parser)
    {
        this.position = parser.getParsableSerializable("position", Point3D::provider)
        this.uv = parser.getParsableSerializable("uv", Point2D::provider)
        this.normal = parser.getParsableSerializable("normal", Point3D::provider)
    }
}
