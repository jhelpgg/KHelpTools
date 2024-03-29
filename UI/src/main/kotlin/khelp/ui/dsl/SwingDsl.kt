package khelp.ui.dsl

import khelp.resources.ResourcesText
import khelp.thread.TaskContext
import khelp.ui.components.JHelpFrame
import khelp.ui.components.style.StyledButton
import khelp.ui.components.style.StyledLabel
import khelp.ui.style.StyleImageWithText
import java.awt.Color
import java.awt.Component
import java.awt.Container
import javax.swing.JButton
import javax.swing.JDialog
import kotlin.math.max

fun frame(title : String = "JHelpFrame",
          decorated : Boolean = true, full : Boolean = false,
          creator : JHelpFrame.() -> Unit) : JHelpFrame
{
    val frame = JHelpFrame(title, decorated, full)
    creator(frame)
    frame.showFrame()
    return frame
}

fun Container.borderLayout(horizontalSpaceBetweenComponents : Int = 1,
                           verticalSpaceBetweenComponents : Int = 1,
                           creator : BorderLayoutCreator.() -> Unit)
{
    val borderLayoutCreator = BorderLayoutCreator(this,
                                                  max(1, horizontalSpaceBetweenComponents),
                                                  max(1, verticalSpaceBetweenComponents))
    creator(borderLayoutCreator)
}

fun Container.tableLayout(tableAspect : Boolean = false, creator : TableLayoutCreator.() -> Unit)
{
    val tableLayoutCreator = TableLayoutCreator(this, tableAspect)
    creator(tableLayoutCreator)
}

fun Container.constraintLayout(creator : ConstraintsLayoutCreator.() -> Unit)
{
    val constraintsLayoutCreator = ConstraintsLayoutCreator(this)
    creator(constraintsLayoutCreator)
    constraintsLayoutCreator.create()
}

fun Container.verticalLayout(marginHorizontal : Int = 4, marginLeft : Int = 0,
                             creator : VerticalLayoutCreator.() -> Unit)
{
    val tableLayoutCreator = TableLayoutCreator(this, false)
    creator(VerticalLayoutCreator(tableLayoutCreator, marginHorizontal, marginLeft))
}

fun Container.horizontalLayout(creator : HorizontalLayoutCreator.() -> Unit)
{
    val constraintsLayoutCreator = ConstraintsLayoutCreator(this)
    creator(HorizontalLayoutCreator(constraintsLayoutCreator))
    constraintsLayoutCreator.create()
}

fun button(text : String, buttonCreator : JButtonCreator.() -> Unit) : JButton
{
    val jButtonCreator = JButtonCreator(text)
    buttonCreator(jButtonCreator)
    return jButtonCreator.button
}

fun button(textKey : String, resourcesText : ResourcesText, buttonCreator : JButtonCreator.() -> Unit) : JButton
{
    val jButtonCreator = JButtonCreator(textKey)
    buttonCreator(jButtonCreator)
    val button = jButtonCreator.button
    resourcesText.observableChange.observedBy(TaskContext.INDEPENDENT) { texts -> button.text = texts[textKey] }
    return button
}

fun JDialog.menuBar(creator : MenuBarCreator.() -> Unit)
{
    val menuBarCreator = MenuBarCreator()
    creator(menuBarCreator)
    this.jMenuBar = menuBarCreator.menuBar
}

fun Component.back(color : Color) : Component
{
    this.background = color
    return this
}

fun styledButton(keyText : String, resourcesText : ResourcesText,
                 creator : StyledButtonCreator.() -> Unit) : StyledButton
{
    val styledButtonCreator = StyledButtonCreator(keyText, resourcesText)
    creator(styledButtonCreator)
    return styledButtonCreator.create()
}

fun styledLabel(keyText : String, resourcesText : ResourcesText,
                 creator : StyledLabelCreator.() -> Unit) : StyledLabel<StyleImageWithText>
{
    val styledLabelCreator = StyledLabelCreator(keyText, resourcesText)
    creator(styledLabelCreator)
    return styledLabelCreator.create()
}
