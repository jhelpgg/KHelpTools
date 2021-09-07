package khelp.ui.dsl

import java.awt.BorderLayout
import java.awt.Component
import java.awt.Container
import javax.swing.JPanel

class BorderLayoutCreator internal constructor(private val container : Container,
                                               horizontalSpaceBetweenComponents : Int,
                                               verticalSpaceBetweenComponents : Int)
{
    init
    {
        this.container.layout = BorderLayout(horizontalSpaceBetweenComponents, verticalSpaceBetweenComponents)
    }

    fun lineStart(component : Component)
    {
        this.container.add(component, BorderLayout.LINE_START)
    }

    fun lineEnd(component : Component)
    {
        this.container.add(component, BorderLayout.LINE_END)
    }

    fun pageStart(component : Component)
    {
        this.container.add(component, BorderLayout.PAGE_START)
    }

    fun pageEnd(component : Component)
    {
        this.container.add(component, BorderLayout.PAGE_END)
    }

    fun center(component : Component)
    {
        this.container.add(component, BorderLayout.CENTER)
    }

    fun lineStart(panelCreator : JPanel.() -> Unit)
    {
        val panel = JPanel()
        panelCreator(panel)
        this.container.add(panel, BorderLayout.LINE_START)
    }

    fun lineEnd(panelCreator : JPanel.() -> Unit)
    {
        val panel = JPanel()
        panelCreator(panel)
        this.container.add(panel, BorderLayout.LINE_END)
    }

    fun pageStart(panelCreator : JPanel.() -> Unit)
    {
        val panel = JPanel()
        panelCreator(panel)
        this.container.add(panel, BorderLayout.PAGE_START)
    }

    fun pageEnd(panelCreator : JPanel.() -> Unit)
    {
        val panel = JPanel()
        panelCreator(panel)
        this.container.add(panel, BorderLayout.PAGE_END)
    }

    fun center(panelCreator : JPanel.() -> Unit)
    {
        val panel = JPanel()
        panelCreator(panel)
        this.container.add(panel, BorderLayout.CENTER)
    }
}