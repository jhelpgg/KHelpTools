package khelp.engine3d.geometry

import khelp.engine3d.utils.ThreadOpenGL

data class Vertex(val position : Point3D, val uv : Point2D, val normal : Point3D)
{
    @ThreadOpenGL
    internal fun render()
    {
        this.normal.glNormal3f()
        this.uv.glTexCoord2f()
        this.position.glVertex3f()
    }
}
