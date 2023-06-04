package khelp.engine3d.gui.dsl

import khelp.engine3d.gui.GUI
import khelp.engine3d.gui.GUIMargin
import khelp.engine3d.gui.component.GUIComponentButton
import khelp.engine3d.gui.component.GUIComponentEmpty
import khelp.engine3d.gui.component.GUIComponentImage
import khelp.engine3d.gui.component.GUIComponentPanel
import khelp.engine3d.gui.component.GUIComponentScroll
import khelp.engine3d.gui.component.GUIComponentText
import khelp.engine3d.gui.component.GUIDialog
import khelp.engine3d.gui.layout.GUIConstraints
import khelp.engine3d.gui.layout.GUILayout
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteConstraint
import khelp.engine3d.gui.layout.absolute.GUIAbsoluteLayout
import khelp.engine3d.gui.layout.constraint.GUIConstraintConstraint
import khelp.engine3d.gui.layout.constraint.GUIConstraintLayout
import khelp.engine3d.gui.layout.grid.GUIGridConstraint
import khelp.engine3d.gui.layout.grid.GUIGridLayout
import khelp.engine3d.gui.layout.horizontal.GUIHorizontalConstraint
import khelp.engine3d.gui.layout.horizontal.GUIHorizontalLayout
import khelp.engine3d.gui.layout.proprtion.GUIProportionConstraint
import khelp.engine3d.gui.layout.proprtion.GUIProportionLayout
import khelp.engine3d.gui.layout.vertical.GUIVerticalConstraint
import khelp.engine3d.gui.layout.vertical.GUIVerticalLayout
import khelp.resources.ResourcesText
import khelp.resources.defaultTexts
import khelp.thread.TaskContext
import khelp.ui.TextAlignment
import khelp.ui.VerticalAlignment
import khelp.ui.extensions.color
import khelp.ui.extensions.contrast
import khelp.ui.extensions.grayVersion
import khelp.ui.extensions.grey
import khelp.ui.extensions.invert
import khelp.ui.extensions.withContrast
import khelp.ui.font.JHelpFont
import khelp.ui.game.GameImage
import khelp.ui.style.ComponentHighLevel
import khelp.ui.style.background.StyleBackgroundColor
import khelp.ui.style.background.StyleBackgroundPaint
import khelp.ui.style.shape.StyleShape
import khelp.ui.style.shape.StyleShapeRoundRectangle
import khelp.ui.style.shape.StyleShapeSausage
import khelp.ui.utilities.BUTTON_FONT
import khelp.ui.utilities.colors.BaseColor
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

fun GUI.constraintLayout(content : GUIConstraintFiller.() -> Unit)
{
    val layout = GUIConstraintLayout()
    val filler = GUIConstraintFiller(layout)
    content(filler)
    this.layout = layout
}

fun GUI.verticalLayout(content : GUIVerticalFiller.() -> Unit)
{
    val layout = GUIVerticalLayout()
    val filter = GUIVerticalFiller(layout)
    content(filter)
    this.layout = layout
}

fun GUI.horizontalLayout(content : GUIHorizontalFiller.() -> Unit)
{
    val layout = GUIHorizontalLayout()
    val filter = GUIHorizontalFiller(layout)
    content(filter)
    this.layout = layout
}

fun GUI.gridLayout(numberColumn : Int = 1, content : GUIGridFiller.() -> Unit)
{
    val layout = GUIGridLayout(numberColumn)
    val filler = GUIGridFiller(layout)
    content(filler)
    this.layout = layout
}

fun <C : GUIConstraints, L : GUILayout<C>> GUI.dialog(layout : L,
                                                      content : GuiLayoutFiller<C, L>.() -> Unit) : GUIDialog<C, L>
{
    val filler = GuiLayoutFiller<C, L>(layout)
    content(filler)
    return this.createDialog(layout)
}

fun GUI.dialogAbsolute(
    content : GuiLayoutFiller<GUIAbsoluteConstraint, GUIAbsoluteLayout>.() -> Unit) : GUIDialog<GUIAbsoluteConstraint, GUIAbsoluteLayout> =
    this.dialog(GUIAbsoluteLayout(), content)

fun GUI.dialogProportion(
    content : GuiLayoutFiller<GUIProportionConstraint, GUIProportionLayout>.() -> Unit) : GUIDialog<GUIProportionConstraint, GUIProportionLayout> =
    this.dialog(GUIProportionLayout(), content)

fun GUI.dialogConstraint(
    content : GUIConstraintFiller.() -> Unit) : GUIDialog<GUIConstraintConstraint, GUIConstraintLayout>
{
    val layout = GUIConstraintLayout()
    val filler = GUIConstraintFiller(layout)
    content(filler)
    return this.createDialog(layout)
}

fun GUI.dialogVertical(content : GUIVerticalFiller.() -> Unit) : GUIDialog<GUIVerticalConstraint, GUIVerticalLayout>
{
    val layout = GUIVerticalLayout()
    val filter = GUIVerticalFiller(layout)
    content(filter)
    return this.createDialog(layout)
}

fun GUI.dialogHorizontal(
    content : GUIHorizontalFiller.() -> Unit) : GUIDialog<GUIHorizontalConstraint, GUIHorizontalLayout>
{
    val layout = GUIHorizontalLayout()
    val filter = GUIHorizontalFiller(layout)
    content(filter)
    return this.createDialog(layout)
}

fun GUI.dialogGrid(numberColumn : Int = 1,
                   content : GUIGridFiller.() -> Unit) : GUIDialog<GUIGridConstraint, GUIGridLayout>
{
    val layout = GUIGridLayout(numberColumn)
    val filler = GUIGridFiller(layout)
    content(filler)
    return this.createDialog(layout)
}

fun GUI.colorChooserButton(colorChoose : (BaseColor<*>) -> Unit) : GUIComponentButton
{
    val dialogColorChooser = this.dialogColorChooser()
    val baseColor = dialogColorChooser.color.value()

    val emptyUp = GUIComponentEmpty()
    emptyUp.borderColor = Color.LIGHT_GRAY
    emptyUp.margin = GUIMargin(4, 4, 4, 4)
    emptyUp.background = StyleBackgroundColor(baseColor.color)
    emptyUp.componentHighLevel = ComponentHighLevel.HIGHEST
    emptyUp.shape = StyleShapeRoundRectangle

    val emptyOver = GUIComponentEmpty()
    emptyOver.borderColor = Color.WHITE
    emptyOver.margin = GUIMargin(4, 4, 4, 4)
    emptyOver.background = StyleBackgroundColor(baseColor.lighter.color)
    emptyOver.componentHighLevel = ComponentHighLevel.FLY
    emptyOver.shape = StyleShapeRoundRectangle

    val emptyDown = GUIComponentEmpty()
    emptyDown.borderColor = Color.BLACK
    emptyDown.margin = GUIMargin(4, 4, 4, 4)
    emptyDown.background = StyleBackgroundColor(baseColor.darker.color)
    emptyDown.componentHighLevel = ComponentHighLevel.AT_GROUND
    emptyDown.shape = StyleShapeRoundRectangle

    val emptyGrey = GUIComponentEmpty()
    emptyGrey.borderColor = Color.GRAY
    emptyGrey.margin = GUIMargin(4, 4, 4, 4)
    emptyGrey.background = StyleBackgroundColor(baseColor.color.grey)
    emptyGrey.componentHighLevel = ComponentHighLevel.NEAR_GROUND
    emptyGrey.shape = StyleShapeRoundRectangle

    val button = GUIComponentButton(emptyUp, emptyOver, emptyDown, emptyGrey)
    button.click = { dialogColorChooser.show() }

    dialogColorChooser.showing.observedBy(TaskContext.INDEPENDENT) { showing ->
        if (! showing)
        {
            val colorChosen = dialogColorChooser.color.value()
            emptyUp.background = StyleBackgroundColor(colorChosen.color)
            emptyOver.background = StyleBackgroundColor(colorChosen.lighter.color)
            emptyDown.background = StyleBackgroundColor(colorChosen.darker.color)
            emptyGrey.background = StyleBackgroundColor(colorChosen.color.grey)
            colorChoose(colorChosen)
        }
    }

    return button
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

fun panelConstraint(content : GUIConstraintFiller.() -> Unit) :
        GUIComponentPanel<GUIConstraintConstraint, GUIConstraintLayout>
{
    val layout = GUIConstraintLayout()
    val filler = GUIConstraintFiller(layout)
    content(filler)
    return GUIComponentPanel<GUIConstraintConstraint, GUIConstraintLayout>(layout)
}

fun panelVertical(content : GUIVerticalFiller.() -> Unit) : GUIComponentPanel<GUIVerticalConstraint, GUIVerticalLayout>
{
    val layout = GUIVerticalLayout()
    val filter = GUIVerticalFiller(layout)
    content(filter)
    return GUIComponentPanel<GUIVerticalConstraint, GUIVerticalLayout>(layout)
}

fun panelHorizontal(
    content : GUIHorizontalFiller.() -> Unit) : GUIComponentPanel<GUIHorizontalConstraint, GUIHorizontalLayout>
{
    val layout = GUIHorizontalLayout()
    val filter = GUIHorizontalFiller(layout)
    content(filter)
    return GUIComponentPanel<GUIHorizontalConstraint, GUIHorizontalLayout>(layout)
}

fun panelGrid(numberColumn : Int = 1,
              content : GUIGridFiller.() -> Unit) : GUIComponentPanel<GUIGridConstraint, GUIGridLayout>
{
    val layout = GUIGridLayout(numberColumn)
    val filler = GUIGridFiller(layout)
    content(filler)
    return GUIComponentPanel<GUIGridConstraint, GUIGridLayout>(layout)
}

fun scrollAbsolute(
    content : GuiLayoutFiller<GUIAbsoluteConstraint, GUIAbsoluteLayout>.() -> Unit) : GUIComponentScroll =
    GUIComponentScroll(panelAbsolute(content))

fun scrollProportion(
    content : GuiLayoutFiller<GUIProportionConstraint, GUIProportionLayout>.() -> Unit) : GUIComponentScroll =
    GUIComponentScroll(panelProportion(content))

fun scrollConstraint(content : GUIConstraintFiller.() -> Unit) : GUIComponentScroll =
    GUIComponentScroll(panelConstraint(content))

fun scrollVertical(content : GUIVerticalFiller.() -> Unit) : GUIComponentScroll =
    GUIComponentScroll(panelVertical(content))

fun scrollHorizontal(content : GUIHorizontalFiller.() -> Unit) : GUIComponentScroll =
    GUIComponentScroll(panelHorizontal(content))

fun scrollGrid(numberColumn : Int = 1, content : GUIGridFiller.() -> Unit) : GUIComponentScroll =
    GUIComponentScroll(panelGrid(numberColumn, content))

fun buttonText(keyText : String,
               resourcesText : ResourcesText = defaultTexts,
               font : JHelpFont = BUTTON_FONT,
               textColor : Color = Color.WHITE,
               textColorBorder : Color = textColor.invert,
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
    disabled.textAlignment = TextAlignment.CENTER
    disabled.verticalAlignment = VerticalAlignment.CENTER
    disabled.background = StyleBackgroundColor(disabledBackground)
    disabled.borderColor = borderColor
    disabled.shape = shape
    disabled.componentHighLevel = ComponentHighLevel.NEAR_GROUND

    return GUIComponentButton(normal, over, down, disabled)
}

fun buttonImage(normalImage : GameImage,
                overImage : GameImage = normalImage.withContrast(2.0),
                downImage : GameImage = normalImage.withContrast(0.5),
                disabledImage : GameImage = normalImage.grayVersion(),
                shape : StyleShape = StyleShapeSausage,
                borderColor : Color = Color.BLACK) : GUIComponentButton
{
    val normal = GUIComponentImage(normalImage)
    normal.background = StyleBackgroundPaint(GameImage.DARK_LIGHT_PAINT)
    normal.shape = shape
    normal.borderColor = borderColor
    normal.componentHighLevel = ComponentHighLevel.HIGHEST

    val over = GUIComponentImage(overImage)
    over.background = StyleBackgroundPaint(GameImage.DARK_LIGHT_PAINT)
    over.shape = shape
    over.borderColor = borderColor
    over.componentHighLevel = ComponentHighLevel.FLY

    val down = GUIComponentImage(downImage)
    down.background = StyleBackgroundPaint(GameImage.DARK_LIGHT_PAINT)
    down.shape = shape
    down.borderColor = borderColor
    down.componentHighLevel = ComponentHighLevel.AT_GROUND

    val disabled = GUIComponentImage(disabledImage)
    disabled.background = StyleBackgroundPaint(GameImage.DARK_LIGHT_PAINT)
    disabled.shape = shape
    disabled.borderColor = borderColor
    disabled.componentHighLevel = ComponentHighLevel.NEAR_GROUND

    return GUIComponentButton(normal, over, down, disabled)
}
