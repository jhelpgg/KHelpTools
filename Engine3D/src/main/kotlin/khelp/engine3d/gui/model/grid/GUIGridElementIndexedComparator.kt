package khelp.engine3d.gui.model.grid

internal class GUIGridElementIndexedComparator<V : Any>(
    private val comparator : Comparator<V>) : Comparator<GUIGridElementIndexed<V>>
{
    override fun compare(firstElement : GUIGridElementIndexed<V>, secondElement : GUIGridElementIndexed<V>) : Int =
        this.comparator.compare(firstElement.element, secondElement.element)
}
