package khelp.engine3d.render

import khelp.engine3d.comparator.NodeComparatorOrderZ
import khelp.engine3d.utils.ThreadOpenGL
import org.lwjgl.opengl.GL11
import java.util.Stack

class Scene
{
    companion object
    {
        const val ROOT_ID = "-<([ROOT])>-"
    }

    var backgroundColor : Color4f = BLACK
    private val root : Node = Node(Scene.ROOT_ID)

    init
    {
        this.root.z = - 5f
    }

    @NodeDSL
    fun root(creator : Node.() -> Unit)
    {
        creator(this.root)
    }

    fun <N:Node> findById(id:String): N? = this.root.findById(id)

    @ThreadOpenGL
    internal fun drawBackground()
    {
        this.backgroundColor.glColor4fBackground()
    }

    @ThreadOpenGL
    internal fun render()
    {
        val stack = Stack<Node>()
        stack.push(this.root)
        val nodes = ArrayList<Node>()

        while (! stack.isEmpty())
        {
            val node = stack.pop()

            if (node.visible)
            {
                if (node.countInRender)
                {
                    node.zOrder = node.reverseProjection(node.center).z
                    nodes.add(node)
                }

                for (child in node)
                {
                    stack.push(child)
                }
            }
        }

        nodes.sortWith(NodeComparatorOrderZ)

        for (node in nodes)
        {
            GL11.glPushMatrix()
            node.matrixRootToMe()
            node.renderSpecific()
            GL11.glPopMatrix()
        }
    }
}
