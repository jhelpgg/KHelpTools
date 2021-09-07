package khelp.ui.components

import khelp.resources.ResourcesText
import khelp.thread.TaskContext
import javax.swing.JLabel

fun JLabel(key : String, resourcesText : ResourcesText) : JLabel
{
    val label = JLabel(resourcesText[key], JLabel.CENTER)
    resourcesText.observableChange.observedBy(TaskContext.INDEPENDENT) { texts -> label.text = texts[key] }
    return label
}
