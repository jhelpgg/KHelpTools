package khelp.engine3d.gui.layout

import khelp.engine3d.gui.component.GUIComponent
import khelp.utilities.extensions.transform
import java.awt.Dimension

abstract class GUILayout<C : GUIConstraints>
{
    private val components = ArrayList<Pair<GUIComponent, C>>()

    fun add(component : GUIComponent, constraint : C)
    {
        this.components.add(Pair<GUIComponent, C>(component, constraint))
    }

    internal fun components() : Iterable<GUIComponent> =
        this.components.transform { (component, _) -> component }

    internal fun layout(parentWidth : Int, parentHeight : Int)
    {
        this.layout(parentWidth, parentHeight, this.components)
    }

    internal fun preferredSize() : Dimension =
        this.preferredSize(this.components)

    protected abstract fun layout(parentWidth : Int, parentHeight : Int, components : List<Pair<GUIComponent, C>>)

    protected abstract fun preferredSize(components : List<Pair<GUIComponent, C>>) : Dimension
}
