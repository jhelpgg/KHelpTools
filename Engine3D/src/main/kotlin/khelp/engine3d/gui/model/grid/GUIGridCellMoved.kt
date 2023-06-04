package khelp.engine3d.gui.model.grid

class GUIGridCellMoved<V : Any>(val oldIndex : Int, val newIndex : Int,
                                val element : V, val visible : Boolean) : GUIGridEvent<V>
