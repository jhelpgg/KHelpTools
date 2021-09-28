package khelp.engine3d.render

import khelp.engine3d.animation.AnimationGroup
import khelp.engine3d.animation.AnimationList
import khelp.engine3d.animation.AnimationManager
import khelp.engine3d.animation.AnimationNodePosition
import khelp.engine3d.comparator.NodeComparatorOrderZ
import khelp.engine3d.utils.ThreadOpenGL
import khelp.ui.game.GameImage
import khelp.ui.game.interpolation.GameImageInterpolationMelt
import khelp.ui.game.interpolation.GameImageInterpolationType
import org.lwjgl.opengl.GL11
import java.util.Stack

class Scene
{
    companion object
    {
        const val ROOT_ID = "-<([ROOT])>-"
    }

    var backgroundColor : Color4f = BLACK
    val root : Node = Node(Scene.ROOT_ID)

    init
    {
        this.root.z = - 5f
    }

    @NodeDSL
    fun root(creator : Node.() -> Unit)
    {
        creator(this.root)
    }

    @AnimationDSL
    fun animationNodePositionElement(animationName : String, nodeName : String,
                                     creator : AnimationNodePosition.() -> Unit)
    {
        val node = this.findById<Node>(nodeName) ?: return
        AnimationManager.animationNodePositionElement(animationName, node, creator)
    }

    @AnimationDSL
    fun animationGroup(animationName : String, creator : AnimationGroup.() -> Unit)
    {
        AnimationManager.animationGroup(animationName, creator)
    }

    @AnimationDSL
    fun animationList(name : String, creator : AnimationList.() -> Unit)
    {
        AnimationManager.animationList(name, creator)
    }

    fun animationTexture(name : String, start : GameImage, end : GameImage, transitionMillisecond : Long,
                         gameImageInterpolationType : GameImageInterpolationType = GameImageInterpolationMelt) : Texture =
        AnimationManager.animationTexture(name, start, end, transitionMillisecond, gameImageInterpolationType)

    fun playAnimation(animationName : String)
    {
        AnimationManager.play(animationName)
    }

    fun <N : Node> findById(id : String) : N? = this.root.findById(id)

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
                    node.zOrder = node.reverseProjectionWithRoot(node.center).z
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
