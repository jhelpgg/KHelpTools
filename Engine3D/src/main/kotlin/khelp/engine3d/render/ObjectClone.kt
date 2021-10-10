package khelp.engine3d.render

import khelp.engine3d.geometry.Point3D
import khelp.engine3d.geometry.VirtualBox
import khelp.engine3d.utils.ThreadOpenGL

class ObjectClone(id : String, internal val reference : Object3D) : NodeWithMaterial(id)
{
    override val center : Point3D get() = this.reference.center
    override val virtualBox : VirtualBox get() = this.reference.virtualBox

    init
    {
        this.twoSidedRule = this.reference.twoSidedRule
    }

    @ThreadOpenGL
    override fun renderSpecific()
    {
        this.material { material -> material.renderMaterial(this.reference) }
    }

    @ThreadOpenGL
    override fun renderSpecificPicking()
    {
        this.reference.drawObject()
    }
}
