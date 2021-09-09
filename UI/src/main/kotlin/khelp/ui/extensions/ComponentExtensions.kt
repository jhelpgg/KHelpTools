package khelp.ui.extensions

import khelp.resources.ResourcesText
import khelp.thread.TaskContext
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JComponent
import javax.swing.border.EtchedBorder

fun JComponent.addTitle(titleKey : String, resourcesText : ResourcesText) : JComponent
{
    resourcesText.observableChange.observedBy(TaskContext.INDEPENDENT) { texts ->
        khelp.ui.utilities.addTitle(this, texts[titleKey])
    }

    return this
}

fun JComponent.addTitle(title : String) : JComponent
{
    khelp.ui.utilities.addTitle(this, title)
    return this
}

fun JComponent.addSubTitle(titleKey : String, resourcesText : ResourcesText) : JComponent
{
    resourcesText.observableChange.observedBy(TaskContext.INDEPENDENT) { texts ->
        khelp.ui.utilities.addSubTitle(this, texts[titleKey])
    }

    return this
}


fun JComponent.addSubTitle(subTitle : String) : JComponent
{
    khelp.ui.utilities.addSubTitle(this, subTitle)
    return this
}


fun JComponent.addBorder() : JComponent
{
    this.border =
        BorderFactory.createEtchedBorder(EtchedBorder.RAISED)
    return this
}

fun JComponent.addLineBorder(margin : Int = 3, color : Color = Color.BLACK) : JComponent
{
    this.border =
        BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1),
            BorderFactory.createEmptyBorder(margin, margin, margin, margin)
        )

    return this
}