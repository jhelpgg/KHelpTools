package khelp.ui.components

import java.awt.Component
import java.awt.Container

class ContainerIterator(private val container : Container)
{
    private var index = 0

    operator fun hasNext() : Boolean = this.index < this.container.componentCount

    operator fun next() : Component = this.container.getComponent(this.index ++)
}

operator fun ContainerIterator.iterator() = this

operator fun Container.iterator() = ContainerIterator(this)
