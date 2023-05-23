package khelp.engine3d.gui.dsl

import khelp.engine3d.gui.GUI
import khelp.engine3d.gui.component.GUIComponentButton
import khelp.engine3d.gui.component.GUIComponentPanel
import khelp.engine3d.gui.component.GUIComponentText
import khelp.engine3d.gui.layout.GUIConstraints
import khelp.engine3d.gui.layout.GUILayout
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteConstraint
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteLayout
import khelp.engine3d.gui.layout.proprtion.GUIProportionConstraint
import khelp.engine3d.gui.layout.proprtion.GUIProportionLayout
import khelp.resources.ResourcesText
import khelp.resources.defaultTexts
import khelp.ui.TextAlignment
import khelp.ui.VerticalAlignment
import khelp.ui.extensions.contrast
import khelp.ui.extensions.grey
import khelp.ui.extensions.invert
import khelp.ui.font.JHelpFont
import khelp.ui.style.ComponentHighLevel
import khelp.ui.style.background.StyleBackgroundColor
import khelp.ui.style.shape.StyleShape
import khelp.ui.style.shape.StyleShapeSausage
import khelp.ui.utilities.DEFAULT_FONT
import java.awt.Color

fun <C : GUIConstraints, L : GUILayout<C>> GUI.content(layout : L, content : GuiLayoutFiller<C, L>.() -> Unit)
{
    val filler = GuiLayoutFiller<C, L>(layout)
    content(filler)
    this.layout = layout
}

fun GUI.absoluteLayout(content : GuiLayoutFiller<GUIAbsoluteConstraint, GUIAbsoluteLayout>.() -> Unit)
{
    this.content(GUIAbsoluteLayout(), content)
}

fun GUI.proportionLayout(content : GuiLayoutFiller<GUIProportionConstraint, GUIProportionLayout>.() -> Unit)
{
    this.content(GUIProportionLayout(), content)
}

fun panelAbsolute(content : GuiLayoutFiller<GUIAbsoluteConstraint, GUIAbsoluteLayout>.() -> Unit) :
        GUIComponentPanel<GUIAbsoluteConstraint, GUIAbsoluteLayout>
{
    val layout = GUIAbsoluteLayout()
    val filler = GuiLayoutFiller<GUIAbsoluteConstraint, GUIAbsoluteLayout>(layout)
    content(filler)
    return GUIComponentPanel<GUIAbsoluteConstraint, GUIAbsoluteLayout>(layout)
}

fun panelProportion(content : GuiLayoutFiller<GUIProportionConstraint, GUIProportionLayout>.() -> Unit) :
        GUIComponentPanel<GUIProportionConstraint, GUIProportionLayout>
{
    val layout = GUIProportionLayout()
    val filler = GuiLayoutFiller<GUIProportionConstraint, GUIProportionLayout>(layout)
    content(filler)
    return GUIComponentPanel<GUIProportionConstraint, GUIProportionLayout>(layout)
}

fun buttonText(keyText : String,
               resourcesText : ResourcesText = defaultTexts,
               font : JHelpFont = DEFAULT_FONT,
               textColor : Color = Color.WHITE,
               textColorBorder : Color = textColor.invert,
               hasTextBorder : Boolean = true,
               shape : StyleShape = StyleShapeSausage,
               normalBackground : Color = Color.CYAN,
               overBackground : Color = normalBackground.contrast(2.0),
               downBackground : Color = normalBackground.contrast(0.5),
               disabledBackground : Color = normalBackground.grey,
               borderColor : Color = normalBackground.invert) : GUIComponentButton
{
    val normal = GUIComponentText()
    normal.keyText = keyText
    normal.resourcesText = resourcesText
    normal.font = font
    normal.textColorMain = textColor
    normal.textColorBorder = textColorBorder
    normal.borderText = hasTextBorder
    normal.background = StyleBackgroundColor(normalBackground)
    normal.textAlignment = TextAlignment.CENTER
    normal.verticalAlignment = VerticalAlignment.CENTER
    normal.borderColor = borderColor
    normal.shape = shape
    normal.componentHighLevel = ComponentHighLevel.HIGHEST

    val over = GUIComponentText()
    over.keyText = keyText
    over.resourcesText = resourcesText
    over.font = font
    over.textColorMain = textColor
    over.textColorBorder = textColorBorder
    over.borderText = hasTextBorder
    over.background = StyleBackgroundColor(overBackground)
    over.textAlignment = TextAlignment.CENTER
    over.verticalAlignment = VerticalAlignment.CENTER
    over.borderColor = borderColor
    over.shape = shape
    over.componentHighLevel = ComponentHighLevel.FLY

    val down = GUIComponentText()
    down.keyText = keyText
    down.resourcesText = resourcesText
    down.font = font
    down.textColorMain = textColor
    down.textColorBorder = textColorBorder
    down.borderText = hasTextBorder
    down.background = StyleBackgroundColor(downBackground)
    down.textAlignment = TextAlignment.CENTER
    down.verticalAlignment = VerticalAlignment.CENTER
    down.borderColor = borderColor
    down.shape = shape
    down.componentHighLevel = ComponentHighLevel.AT_GROUND

    val disabled = GUIComponentText()
    disabled.keyText = keyText
    disabled.resourcesText = resourcesText
    disabled.font = font
    disabled.textColorMain = textColor
    disabled.textColorBorder = textColorBorder
    disabled.borderText = hasTextBorder
    disabled.textAlignment = TextAlignment.CENTER
    disabled.verticalAlignment = VerticalAlignment.CENTER
    disabled.background = StyleBackgroundColor(disabledBackground)
    disabled.borderColor = borderColor
    disabled.shape = shape
    disabled.componentHighLevel = ComponentHighLevel.NEAR_GROUND

    return GUIComponentButton(normal, over, down, disabled)
}


