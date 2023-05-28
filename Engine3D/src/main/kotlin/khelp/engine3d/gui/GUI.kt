package khelp.engine3d.gui

import khelp.engine3d.gui.component.GUIComponentPanel
import khelp.engine3d.gui.component.GUIDialog
import khelp.engine3d.gui.layout.GUIConstraints
import khelp.engine3d.gui.layout.GUILayout
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteLayout
import khelp.engine3d.render.Texture
import khelp.engine3d.render.prebuilt.Plane
import khelp.thread.Mutex
import khelp.ui.events.MouseState
import khelp.ui.game.GameImage
import khelp.ui.utilities.TRANSPARENT

class GUI internal constructor(private val width : Int, private val height : Int)
{
    private val image = GameImage(this.width, this.height)
    private val texture = Texture(this.image)
    private val mutexDialog = Mutex()
    private val dialogs = ArrayList<GUIDialog<*, *>>()
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

    fun <C : GUIConstraints, L : GUILayout<C>> createDialog(layout : L) : GUIDialog<C, L> =
        GUIDialog<C, L>(GUIComponentPanel<C, L>(layout), this)

    internal fun <C : GUIConstraints, L : GUILayout<C>> show(dialog : GUIDialog<C, L>)
    {
        this.mutexDialog { this.dialogs.add(dialog) }
    }

    internal fun <C : GUIConstraints, L : GUILayout<C>> hide(dialog : GUIDialog<C, L>)
    {
        this.mutexDialog { this.dialogs.remove(dialog) }
    }

    internal fun mouseState(mouseState : MouseState) : Boolean
    {
        var panelNullable : GUIComponentPanel<*, *>? = null

        this.mutexDialog {
            if (this.dialogs.isNotEmpty())
            {
                panelNullable = this.dialogs[this.dialogs.size - 1].panel
            }
        }

        if (panelNullable != null)
        {
            val panel = panelNullable !!
            val state = MouseState(mouseState.mouseStatus,
                                   mouseState.x - panel.x - panel.margin.left,
                                   mouseState.y - panel.y - panel.margin.top,
                                   mouseState.leftButtonDown, mouseState.middleButtonDown, mouseState.rightButtonDown,
                                   mouseState.shiftDown, mouseState.controlDown, mouseState.altDown,
                                   mouseState.clicked)
            return panel.mouseState(state)
        }

        return this.layout.mouseState(mouseState)

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

            this.mutexDialog {
                for (dialog in this.dialogs)
                {
                    val panel = dialog.panel
                    val size = panel.preferredSize()
                    panel.x = (this.width - size.width) / 2
                    panel.y = (this.height - size.height) / 2
                    panel.width = size.width
                    panel.height = size.height
                    val clip = graphics2D.clip
                    val transform = graphics2D.transform
                    graphics2D.clipRect(panel.x, panel.y, panel.width, panel.height)
                    graphics2D.translate(panel.x, panel.y)
                    panel.draw(graphics2D)
                    graphics2D.clip = clip
                    graphics2D.transform = transform
                }
            }
        }
    }
}
