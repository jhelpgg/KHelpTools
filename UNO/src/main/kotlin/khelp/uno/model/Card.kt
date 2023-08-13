package khelp.uno.model

import khelp.engine3d.render.Node
import khelp.engine3d.render.ObjectClone
import khelp.engine3d.render.Texture
import khelp.engine3d.render.TwoSidedRule
import khelp.engine3d.render.prebuilt.Plane
import khelp.uno.ui.UnoTextures
import java.util.concurrent.atomic.AtomicInteger

sealed class Card(private val path : String, val points : Int)
{
    companion object
    {
        private const val WIDTH = 0.01f
        private val plane = Plane("CardFaceReference")
        private val NEXT_ID = AtomicInteger(0)
    }

    val id = "Card_${Card.NEXT_ID.getAndIncrement()}"
    val texture : Texture by lazy { UnoTextures[this.path] }

    val node : Node by lazy {
        val node = Node(this.id)

        val face = ObjectClone(this.id, Card.plane)
        face.twoSidedRule = TwoSidedRule.FORCE_ONE_SIDE
        face.z = Card.WIDTH
        face.scaleX = 1.5f
        face.scaleY = 2f
        face.scaleZ = 1f
        face.material.settingAsFor2D()
        face.material.twoSided = false
        face.material.textureDiffuse = this.texture
        face.materialForSelection = face.material
        face.canBePick = true
        node.addChild(face)

        val back = ObjectClone(this.id, Card.plane)
        back.twoSidedRule = TwoSidedRule.FORCE_ONE_SIDE
        back.z = - Card.WIDTH
        back.scaleX = 1.5f
        back.scaleY = 2f
        back.scaleZ = 1f
        back.angleY = 180f
        back.material.settingAsFor2D()
        back.material.twoSided = false
        back.material.textureDiffuse = CardBack.texture
        back.materialForSelection = back.material
        back.canBePick = true
        node.addChild(back)

        node
    }

    abstract fun name() : String

    override fun equals(other : Any?) : Boolean
    {
        if (this === other)
        {
            return true
        }

        if (null == other || other !is Card)
        {
            return false
        }

        return this.name() == other.name()
    }

    override fun hashCode() : Int =
        this.name()
            .hashCode()
}
