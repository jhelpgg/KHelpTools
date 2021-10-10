package khelp.editor.ui.mesheditor.editable

import khelp.engine3d.geometry.Point3D

class EditableFaceEditor internal constructor(private val editableObject : EditableObject)
{
    internal val editableFace = EditableFace()
    private var firstPoint = - 1
    private var previousPoint = - 1

    fun addPoint(point3D : Point3D)
    {
        val index = this.editableObject.addPoint(point3D)

        if (this.firstPoint < 0)
        {
            this.firstPoint = index
        }

        if (this.previousPoint >= 0)
        {
            val edgeIndex = this.editableObject.addEdge(this.previousPoint, index)
            this.editableFace.edges.add(edgeIndex)
        }

        this.previousPoint = index
    }

    internal fun finalizeFace()
    {
        if (this.previousPoint >= 0 && this.firstPoint >= 0 && this.previousPoint != this.firstPoint)
        {
            val edgeIndex = this.editableObject.addEdge(this.previousPoint, this.firstPoint)
            this.editableFace.edges.add(edgeIndex)
        }
    }
}