package khelp.engine3d.gui

import khelp.engine3d.gui.component.GUIComponent
import khelp.engine3d.gui.layout.GUILayout
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteLayout
import khelp.engine3d.render.Texture
import khelp.engine3d.render.prebuilt.Plane
import khelp.ui.events.MouseState
import khelp.ui.events.MouseStatus
import khelp.ui.game.GameImage
import khelp.ui.utilities.TRANSPARENT

class GUI internal constructor(private val width : Int, private val height : Int)
{
    private val image = GameImage(this.width, this.height)
    private val texture = Texture(this.image)
    internal val plane = Plane("GUI")
    private var previousComponent : GUIComponent? = null

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

    internal fun mouseState(mouseState : MouseState) : Boolean
    {
        val x = mouseState.x
        val y = mouseState.y

        for (component in this.layout.components().reversed())
        {
            if (component.visible && component.contains(x, y))
            {
                if (this.previousComponent != component)
                {
                    this.previousComponent?.let { previous ->
                        previous.mouseState(MouseState(MouseStatus.EXIT,
                                                       mouseState.x - previous.x, mouseState.y - previous.y,
                                                       leftButtonDown = false, middleButtonDown = false,
                                                       rightButtonDown = false,
                                                       shiftDown = false, controlDown = false, altDown = false,
                                                       clicked = false))
                    }

                    component.mouseState(MouseState(MouseStatus.ENTER,
                                                    mouseState.x - component.x, mouseState.y - component.y,
                                                    leftButtonDown = false, middleButtonDown = false,
                                                    rightButtonDown = false,
                                                    shiftDown = false, controlDown = false, altDown = false,
                                                    clicked = false))
                }

                this.previousComponent = component
                val state = MouseState(mouseState.mouseStatus,
                                       mouseState.x - component.x, mouseState.y - component.y,
                                       mouseState.leftButtonDown, mouseState.middleButtonDown,
                                       mouseState.rightButtonDown,
                                       mouseState.shiftDown, mouseState.controlDown, mouseState.altDown,
                                       mouseState.clicked)
                return component.mouseState(state)
            }
        }

        this.previousComponent?.let { previous ->
            previous.mouseState(MouseState(MouseStatus.EXIT,
                                           mouseState.x - previous.x, mouseState.y - previous.y,
                                           leftButtonDown = false, middleButtonDown = false, rightButtonDown = false,
                                           shiftDown = false, controlDown = false, altDown = false,
                                           clicked = false))
        }

        this.previousComponent = null
        return false
    }

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
