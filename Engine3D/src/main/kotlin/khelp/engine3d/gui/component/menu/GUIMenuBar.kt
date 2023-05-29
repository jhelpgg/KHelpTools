package khelp.engine3d.gui.component.menu

import khelp.engine3d.gui.component.GUIComponentPanel
import khelp.engine3d.gui.component.GUIComponentText
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteConstraint
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteLayout
import khelp.engine3d.gui.layout.horizontal.GUIHorizontalConstraint
import khelp.engine3d.gui.layout.horizontal.GUIHorizontalLayout
import khelp.resources.ResourcesText
import khelp.ui.TextAlignment
import khelp.ui.VerticalAlignment
import khelp.ui.extensions.color
import khelp.ui.extensions.semiVisible
import khelp.ui.font.JHelpFont
import khelp.ui.style.background.StyleBackgroundColor
import khelp.ui.style.shape.StyleShapeRectangle
import java.awt.Color
import kotlin.math.min

internal class GUIMenuBar(val resourcesText : ResourcesText,
                          val font : JHelpFont,
                          val textColor : Color,
                          val textBorderColor : Color)
{
    private val menus = ArrayList<GUIMenu>()
    private var previousPanel : GUIComponentPanel<*, *>? = null

    fun addMenu(keyText : String) : GUIMenu
    {
        val menu = GUIMenu(keyText, this.resourcesText)
        this.menus.add(menu)
        return menu
    }

    fun panel(width : Int) : Pair<GUIComponentPanel<*, *>, Int>
    {
        val main = GUIAbsoluteLayout()

        val headerLayout = GUIHorizontalLayout()
        val panels = ArrayList<Pair<GUIComponentText, GUIComponentPanel<*, *>>>()

        for (menu in this.menus)
        {
            val panel = menu.panel(this.font, this.textColor, this.textBorderColor)
            panel.visible = false

            val text = GUIComponentText()
            text.keyText = menu.keyText
            text.resourcesText = menu.resourcesText
            text.textColorMain = this.textColor
            text.textColorBorder = this.textBorderColor
            text.font = this.font
            text.textAlignment = TextAlignment.CENTER
            text.verticalAlignment = VerticalAlignment.CENTER
            text.downAction = { this.openClose(panel) }

            panels.add(Pair(text, panel))

            headerLayout.add(text, GUIHorizontalConstraint.CENTER)
        }

        val header = GUIComponentPanel<GUIHorizontalConstraint, GUIHorizontalLayout>(headerLayout)
        header.background = StyleBackgroundColor(khelp.ui.utilities.colors.Grey.GREY_0500.color.semiVisible)
        header.borderColor = Color.BLACK
        header.shape = StyleShapeRectangle
        val headerSize = header.preferredSize()
        headerLayout.layout(width, headerSize.height)
        val y = headerSize.height

        main.add(header, GUIAbsoluteConstraint(0, 0, width, headerSize.height))

        for ((text, panel) in panels)
        {
            var x = text.x
            val panelSize = panel.preferredSize()
            x = min(x, width - panelSize.width)
            main.add(panel, GUIAbsoluteConstraint(x, y, panelSize.width, panelSize.height))
        }

        return Pair(GUIComponentPanel<GUIAbsoluteConstraint, GUIAbsoluteLayout>(main), y)
    }

    private fun openClose(panel : GUIComponentPanel<*, *>)
    {
        if (panel.visible)
        {
            panel.visible = false
            this.previousPanel = null
            return
        }

        this.previousPanel?.visible = false
        panel.visible = true
        this.previousPanel = panel
    }
}
