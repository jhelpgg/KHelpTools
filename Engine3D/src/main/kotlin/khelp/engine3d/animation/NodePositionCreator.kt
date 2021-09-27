package khelp.engine3d.animation

import khelp.engine3d.extensions.position
import khelp.engine3d.render.Node

class NodePositionCreator(var x : Float = 0f, var y : Float = 0f, var z : Float = 0f,
                          var angleX : Float = 0f, var angleY : Float = 0f, var angleZ : Float = 0f,
                          var scaleX : Float = 1f, var scaleY : Float = 1f, var scaleZ : Float = 1f)
{
    constructor(nodePosition : NodePosition) : this(nodePosition.x, nodePosition.y, nodePosition.z,
                                                    nodePosition.angleX, nodePosition.angleY, nodePosition.angleZ,
                                                    nodePosition.scaleX, nodePosition.scaleY, nodePosition.scaleZ)

    constructor(node : Node) : this(node.position)

    val position : NodePosition
        get() = NodePosition(this.x, this.y, this.z,
                             this.angleX, this.angleY, this.angleZ,
                             this.scaleX, this.scaleY, this.scaleZ)
}
