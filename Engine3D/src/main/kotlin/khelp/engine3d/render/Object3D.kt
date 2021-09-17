package khelp.engine3d.render

import khelp.engine3d.geometry.Mesh
import khelp.engine3d.utils.ThreadOpenGL

open class Object3D(id : String) : NodeWithMaterial(id)
{
    private val mesh : Mesh = Mesh()
    var showWire : Boolean = false
    var wireColor : Color4f = DEFAULT_WIRE_FRAME_COLOR

    @MeshDSL
    fun mesh(creator : Mesh.() -> Unit)
    {
        creator(this.mesh)
    }

    @ThreadOpenGL
    override fun renderSpecific()
    {
        this.material.renderMaterial(this)
    }

    @ThreadOpenGL
    internal fun drawObject()
    {
        this.mesh.render()
    }
}
