package khelp.editor.ui.mesheditor.editable

import khelp.engine3d.geometry.Point3D
import khelp.engine3d.render.MeshDSL
import khelp.engine3d.render.Object3D
import khelp.engine3d.render.prebuilt.Box

class EditableObject(id : String)
{
    var points = ArrayList<Point3D>()
    var edges = ArrayList<Edge>()
    var faces = ArrayList<EditableFace>()
    val object3D = Object3D(id)

    init
    {
        this.append(Box("${id}_init"))
        this.refreshObject()
    }

    @MeshDSL
    fun addFace(editableFace : EditableFaceEditor.() -> Unit)
    {
        val editableFaceEditor = EditableFaceEditor(this)
        editableFace(editableFaceEditor)
        editableFaceEditor.finalizeFace()
        this.faces.add(editableFaceEditor.editableFace)
    }

    fun addPoint(point3D : Point3D) : Int
    {
        val index = this.points.indexOf(point3D)

        if (index >= 0)
        {
            return index
        }

        this.points.add(point3D)
        return this.points.size - 1
    }

    fun addEdge(indexPoint1 : Int, indexPoint2 : Int) : Int
    {
        val edge = Edge(indexPoint1, indexPoint2)
        val index = this.edges.indexOf(edge)

        if (index >= 0)
        {
            return index
        }

        this.edges.add(edge)
        return this.edges.size - 1
    }

    fun removeEdge(edgeIndex : Int)
    {
        if (edgeIndex < 0 || edgeIndex >= this.edges.size)
        {
            return
        }

        this.edges.removeAt(edgeIndex)

        for (face in this.faces)
        {
            for (index in 0 until face.edges.size)
            {
                if (face.edges[index] >= edgeIndex)
                {
                    face.edges[index] = (face.edges[index] + this.edges.size - 1) % this.edges.size
                }
            }
        }
    }

    fun append(object3D : Object3D)
    {
        for (face in object3D.mesh)
        {
            addFace {
                for (vertex in face)
                {
                    addPoint(vertex.position)
                }
            }
        }
    }

    fun clear()
    {
        this.points.clear()
        this.edges.clear()
        this.faces.clear()
    }

    fun refreshObject()
    {
        this.object3D.mesh {
            clear()

            for (face in faces)
            {
                val computeUVAndNormal = ComputeUVAndNormal()

                for (edgeIndex in face.edges)
                {
                    val edge = edges[edgeIndex]
                    computeUVAndNormal.add(points[edge.startIndex])
                }

                face {
                    for (edgeIndex in face.edges)
                    {
                        val position = points[edges[edgeIndex].startIndex]
                        add(position, computeUVAndNormal.computeUV(position), computeUVAndNormal.normal)
                    }
                }
            }
        }

        this.object3D.refresh()
    }
}