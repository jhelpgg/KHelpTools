package khelp.engine3d.gui

import khelp.engine3d.gui.layout.GUILayout
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteLayout
import khelp.engine3d.render.Texture
import khelp.engine3d.render.prebuilt.Plane
import khelp.ui.events.MouseState
import khelp.ui.game.GameImage
import khelp.ui.utilities.TRANSPARENT

class GUI internal constructor(private val width : Int, private val height : Int)
{
    private val image = GameImage(this.width, this.height)
    private val texture = Texture(this.image)
    internal val plane = Plane("GUI")

    var layout : GUILayout<*> = GUIAbsoluteLayout()
        set(value)
        {
            field = value
            this.update()
        }

    init
    {
        this.plane.material.settingAsFor2D()
        this.plane.material.textureDiffuse = this.texture

        when
        {
            this.width == this.height -> Unit
            this.width > this.height  -> this.plane.scaleX = this.width.toFloat() / this.height.toFloat()
            else                      -> this.plane.scaleY = this.height.toFloat() / this.width.toFloat()
        }

        this.plane.z = - 1.2f
    }

    internal fun mouseState(mouseState : MouseState) : Boolean =
        this.layout.mouseState(mouseState)

    internal fun update()
    {
        this.layout.layout(this.width, this.height)
        this.image.clear(TRANSPARENT, false)
        this.image.draw { graphics2D ->
            for (component in layout.components())
            {
                if (component.visible)
                {
                    val clip = graphics2D.clip
                    val transform = graphics2D.transform
                    graphics2D.clipRect(component.x, component.y, component.width, component.height)
                    graphics2D.translate(component.x, component.y)
                    component.draw(graphics2D)
                    graphics2D.clip = clip
                    graphics2D.transform = transform
                }
            }
        }
    }
}
