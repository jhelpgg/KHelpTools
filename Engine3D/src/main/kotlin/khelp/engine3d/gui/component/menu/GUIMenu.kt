package khelp.engine3d.gui.component.menu

import khelp.engine3d.gui.component.GUIComponentPanel
import khelp.engine3d.gui.component.GUIComponentText
import khelp.engine3d.gui.component.GUIComponentTextImage
import khelp.engine3d.gui.dsl.panelVertical
import khelp.resources.ResourcesText
import khelp.ui.GenericAction
import khelp.ui.TextAlignment
import khelp.ui.VerticalAlignment
import khelp.ui.font.JHelpFont
import khelp.ui.style.ImageTextRelativePosition
import khelp.ui.style.background.StyleBackgroundColor
import khelp.ui.style.shape.StyleShapeRectangle
import java.awt.Color
import java.awt.event.ActionEvent

internal class GUIMenu(val keyText : String, val resourcesText : ResourcesText, val backgroundColor : Color)
{
    companion object
    {
        private val ACTION_EVENT = ActionEvent(Unit, 0, "")
    }

    private val actions = ArrayList<GenericAction>()
    private var panel : GUIComponentPanel<*, *>? = null

    fun add(action : GenericAction)
    {
        this.actions.add(action)
    }

    fun panel(font : JHelpFont,
              textColor : Color, textBorderColor : Color) : GUIComponentPanel<*, *>
    {
        val panel = panelVertical {
            for (action in this@GUIMenu.actions)
            {
                val image = action.largeImage() ?: action.smallImage()

                val component =
                    if (image == null)
                    {
                        val text = GUIComponentText()
                        text.keyText = action.keyName()
                        text.resourcesText = this@GUIMenu.resourcesText
                        text.font = font
                        text.textColorMain = textColor
                        text.textColorBorder = textBorderColor
                        text.textAlignment = TextAlignment.LEFT
                        text.verticalAlignment = VerticalAlignment.CENTER
                        text.marginLeft = 16
                        text
                    }
                    else
                    {
                        val textImage = GUIComponentTextImage()
                        textImage.keyText = action.keyName()
                        textImage.resourcesText = this@GUIMenu.resourcesText
                        textImage.font = font
                        textImage.textColorMain = textColor
                        textImage.textColorBorder = textBorderColor
                        textImage.textAlignment = TextAlignment.LEFT
                        textImage.image = image
                        textImage.imageSize = 32
                        textImage.imageTextRelativePosition = ImageTextRelativePosition.IMAGE_LEFT_OF_TEXT
                        textImage.marginLeft = 16
                        textImage
                    }

                component.downAction = {
                    this@GUIMenu.panel?.visible = false
                    action.actionPerformed(GUIMenu.ACTION_EVENT)
                }

                component.left
            }
        }

        panel.background = StyleBackgroundColor(this.backgroundColor)
        panel.borderColor = Color.BLACK
        panel.shape = StyleShapeRectangle
        panel.relayoutWithPreferred()
        this.panel = panel
        return panel
    }
}

