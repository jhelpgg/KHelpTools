package khelp.engine3d.render

abstract class NodeWithMaterial(id:String) : Node(id)
{
    init
    {
        this.countInRender = true
    }

    var material : Material = Material()
}