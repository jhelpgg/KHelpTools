package khelp.engine3d.gui

import khelp.engine3d.gui.component.GUIComponentPanel
import khelp.engine3d.gui.component.GUIDialog
import khelp.engine3d.gui.component.dialogs.DialogColorChooser
import khelp.engine3d.gui.component.dialogs.DialogMessage
import khelp.engine3d.gui.component.menu.GUIMenuBar
import khelp.engine3d.gui.dsl.GUIMenuBarCreator
import khelp.engine3d.gui.layout.GUIConstraints
import khelp.engine3d.gui.layout.GUILayout
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteConstraint
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteLayout
import khelp.engine3d.render.Texture
import khelp.engine3d.render.prebuilt.Plane
import khelp.resources.ResourcesText
import khelp.resources.defaultTexts
import khelp.thread.Mutex
import khelp.ui.events.MouseState
import khelp.ui.extensions.color
import khelp.ui.extensions.invert
import khelp.ui.extensions.semiVisible
import khelp.ui.font.JHelpFont
import khelp.ui.game.GameImage
import khelp.ui.utilities.DEFAULT_FONT
import khelp.ui.utilities.TRANSPARENT
import khelp.ui.utilities.colors.Green
import java.awt.Color

class GUI internal constructor(private val width : Int, private val height : Int)
{
    private val image = GameImage(this.width, this.height)
    private val texture = Texture(this.image)
    private val mutexDialog = Mutex()
    private val dialogs = ArrayList<GUIDialog<*, *>>()
    private var menuBarPanel : GUIComponentPanel<*, *>? = null
    private var menuBarY = 0
    private var completeLayout : GUILayout<*> = GUIAbsoluteLayout()
    internal val plane = Plane("GUI")

    var layout : GUILayout<*> = GUIAbsoluteLayout()
        set(value)
        {
            field = value
            this.completeLayout()
        }

    val dialogMessage = DialogMessage(this)

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

    fun dialogColorChooser() : DialogColorChooser = DialogColorChooser(this)

    fun menuBar(resourcesText : ResourcesText = defaultTexts,
                textColor : Color = Color.WHITE,
                textBackColor : Color = textColor.invert,
                font : JHelpFont = DEFAULT_FONT,
                backgroundColor : Color = Green.GREEN_0050.color.semiVisible,
                creator : GUIMenuBarCreator.() -> Unit)
    {
        val menuBar = GUIMenuBar(resourcesText, font, textColor, textBackColor, backgroundColor)
        val menuCreator = GUIMenuBarCreator(menuBar)
        creator(menuCreator)
        val (panel, height) = menuBar.panel(this.width)
        this.menuBarPanel = panel
        this.menuBarY = height
        this.completeLayout()
    }

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

        return this.completeLayout.mouseState(mouseState)

    }

    internal fun update()
    {
        this.completeLayout.layout(this.width, this.height)
        this.image.clear(TRANSPARENT, false)
        this.image.draw { graphics2D ->
            for (component in completeLayout.components())
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


    private fun completeLayout()
    {
        val menuBar = this.menuBarPanel

        if (menuBar == null)
        {
            this.completeLayout = this.layout
        }
        else
        {
            val completeLayout = GUIAbsoluteLayout()
            completeLayout.add(GUIComponentPanel(this.layout),
                               GUIAbsoluteConstraint(0, this.menuBarY, this.width, this.height - this.menuBarY))
            completeLayout.add(menuBar,
                               GUIAbsoluteConstraint(0, 0, this.width, this.height))
            this.completeLayout = completeLayout
        }

        this.update()
    }
}
