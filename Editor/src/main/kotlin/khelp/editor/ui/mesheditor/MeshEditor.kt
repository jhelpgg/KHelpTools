package khelp.editor.ui.mesheditor

import khelp.editor.ui.Editor
import khelp.editor.ui.ScreenEditor
import khelp.editor.ui.mesheditor.editable.EditableFace
import khelp.editor.ui.mesheditor.editable.EditableObject
import khelp.engine3d.event.MouseManager3D
import khelp.engine3d.geometry.Point3D
import khelp.engine3d.geometry.path.Path
import khelp.engine3d.render.BLACK
import khelp.engine3d.render.BLUE
import khelp.engine3d.render.GREEN
import khelp.engine3d.render.LIGHT_BLUE
import khelp.engine3d.render.LIGHT_RED
import khelp.engine3d.render.Material
import khelp.engine3d.render.Node
import khelp.engine3d.render.RED
import khelp.engine3d.render.TwoSidedRule
import khelp.engine3d.render.prebuilt.Box
import khelp.engine3d.render.prebuilt.Equation3D
import khelp.engine3d.render.prebuilt.VARIABLE_T
import khelp.math.formal.Constant
import khelp.thread.TaskContext
import khelp.thread.flow.Flow
import khelp.ui.GenericAction
import khelp.ui.dsl.verticalLayout
import khelp.ui.events.MouseState
import khelp.ui.events.MouseStatus
import khelp.utilities.extensions.int
import khelp.utilities.math.isNul
import javax.swing.JButton
import javax.swing.JPanel

class MeshEditor(mouseManager3D : MouseManager3D, nodeSelection : Flow<Node?>) : ScreenEditor
{
    companion object
    {
        private val vertex = Box("MeshEditor.Vertex")
        private const val EDGE_HEADER = "MeshEditor.Edge_"
        private const val VERTEX_HEADER = "MeshEditor.Vertex_"
    }

    private var mouseX = 0
    private var mouseY = 0
    private var overNode : Node? = null
    private val gridNode = Node("MeshEditor.grid")
    private val mainNode = Node("MeshEditor.main")
    override val manipulatedNode : Node get() = this.mainNode
    private val selectMaterial = Material()
    private val normalMaterial = Material()
    private val overMaterial = Material()
    private val editableObject = EditableObject("MeshEditor.edited")
    private var lastSelectedNode : Node? = null
    private var meshHasMoved = false
    private val actionCutEdge = GenericAction(Editor.resourcesText, "cutEdge", TaskContext.INDEPENDENT, this::cutEdge)
    private val buttonCutEdge = JButton(this.actionCutEdge)

    init
    {
        this.actionCutEdge.isEnabled = false
        this.overMaterial.colorDiffuse = RED
        this.overMaterial.colorEmissive = LIGHT_RED

        this.selectMaterial.colorDiffuse = BLUE
        this.selectMaterial.colorEmissive = LIGHT_BLUE

        val material = this.editableObject.object3D.material
        this.editableObject.object3D.showWire = true
        material.colorDiffuse = GREEN
        material.colorEmissive = GREEN
        material.twoSided = true
        this.editableObject.object3D.materialForSelection = this.editableObject.object3D.material
        this.editableObject.object3D.canBePick = false
        this.editableObject.object3D.wireColor = BLACK

        nodeSelection.then(TaskContext.INDEPENDENT, this::nodeOver)
        mouseManager3D.mouseStateObservable.observedBy(TaskContext.INDEPENDENT, this::mouseState)
        this.updateGrid()
        this.mainNode.addChild(this.gridNode)
        this.mainNode.addChild(this.editableObject.object3D)
    }

    override fun applyInside(panel : JPanel)
    {
        panel.verticalLayout {
            + this@MeshEditor.buttonCutEdge
        }
        // TODO Decide of interface
    }

    private fun updateGrid()
    {
        this.gridNode.removeAllChildren()

        for ((index, point) in this.editableObject.points.withIndex())
        {
            val name = "${MeshEditor.VERTEX_HEADER}$index"
            val anchor = Box(name)
            anchor.material = this.normalMaterial
            anchor.materialForSelection = this.overMaterial
            anchor.x = point.x
            anchor.y = point.y
            anchor.z = point.z
            anchor.scaleX = 0.1f
            anchor.scaleY = 0.1f
            anchor.scaleZ = 0.1f
            this.gridNode.addChild(anchor)
        }

        for ((index, edge) in this.editableObject.edges.withIndex())
        {
            if (edge.startIndex < edge.endIndex)
            {
                val startPoint = this.editableObject.points[edge.startIndex]
                val endPoint = this.editableObject.points[edge.endIndex]
                val t = VARIABLE_T
                val functionX = (Constant.ONE - t) * startPoint.x + t * endPoint.x
                val functionY = (Constant.ONE - t) * startPoint.y + t * endPoint.y
                val functionZ = (Constant.ONE - t) * startPoint.z + t * endPoint.z
                val path = Path()
                path.move(- 0.01f, - 0.01f)
                path.line(0.01f, - 0.01f)
                path.line(0.01f, 0.01f)
                path.line(- 0.01f, 0.01f)
                path.close()
                val edge3D = Equation3D("${MeshEditor.EDGE_HEADER}$index",
                                        path, 2,
                                        0f, 1f, 1f,
                                        functionX, functionY, functionZ)
                edge3D.twoSidedRule = TwoSidedRule.FORCE_TWO_SIDE
                edge3D.material = this.normalMaterial
                edge3D.materialForSelection = this.overMaterial
                this.gridNode.addChild(edge3D)
            }
        }
    }

    private fun nodeOver(node : Node?)
    {
        this.overNode = node
    }

    private fun mouseState(mouseState : MouseState)
    {
        val mouseX = mouseState.x
        val mouseY = mouseState.y

        if (mouseState.mouseStatus == MouseStatus.DRAG)
        {
            if (mouseState.shiftDown)
            {
                this.lastSelectedNode?.let { node ->
                    val name = node.id
                    var vertex1 = - 1
                    var vertex2 = - 1

                    if (name.startsWith(MeshEditor.EDGE_HEADER))
                    {
                        val index = name.substring(MeshEditor.EDGE_HEADER.length)
                            .int(- 1)

                        if (index >= 0)
                        {
                            val edge = this.editableObject.edges[index]
                            vertex1 = edge.startIndex
                            vertex2 = edge.endIndex
                        }
                    }
                    else if (name.startsWith(MeshEditor.VERTEX_HEADER))
                    {
                        val index = name.substring(MeshEditor.VERTEX_HEADER.length)
                            .int(- 1)

                        if (index >= 0)
                        {
                            vertex1 = index
                        }
                    }

                    if (vertex1 < 0)
                    {
                        return
                    }

                    var x = 0f
                    var y = 0f
                    var z = 0f
                    var changed = false

                    if (mouseState.leftButtonDown)
                    {
                        x = (mouseX - this.mouseX).toFloat() * 0.01f
                        y = (this.mouseY - mouseY).toFloat() * 0.01f
                        changed = ! isNul(x) || ! isNul(y)
                    }
                    else if (mouseState.rightButtonDown)
                    {
                        z = (mouseY - this.mouseY).toFloat() * 0.01f
                        changed = ! isNul(z)
                    }

                    if (changed)
                    {
                        val proj = this.mainNode.reverseProjection(Point3D(x, y, z))
                        proj.translate(this.mainNode.x, this.mainNode.y, this.mainNode.z)
                        this.editableObject.points[vertex1].translate(proj.x, proj.y, proj.z)

                        if (vertex2 >= 0)
                        {
                            this.editableObject.points[vertex2].translate(proj.x, proj.y, proj.z)
                        }

                        this.editableObject.refreshObject()
                        this.meshHasMoved = true
                    }
                }
            }
            else
            {
                when
                {
                    mouseState.rightButtonDown  ->
                        this.mainNode.z += (mouseY - this.mouseY).toFloat() * 0.01f
                    mouseState.middleButtonDown ->
                    {
                        this.mainNode.x += (mouseX - this.mouseX).toFloat() * 0.01f
                        this.mainNode.y += (this.mouseY - mouseY).toFloat() * 0.01f
                    }
                    mouseState.leftButtonDown   ->
                    {
                        this.mainNode.angleY += (mouseX - this.mouseX).toFloat() * 0.1f
                        this.mainNode.angleX += (mouseY - this.mouseY).toFloat() * 0.1f
                    }
                }
            }
        }
        else if (mouseState.clicked)
        {
            this.overNode?.let { node ->
                if (mouseState.controlDown)
                {
                    this.lastSelectedNode?.let { selected ->
                        if (selected != node
                            && selected.id.startsWith(MeshEditor.VERTEX_HEADER)
                            && node.id.startsWith(MeshEditor.VERTEX_HEADER))
                        {
                            this.joinNodes(node.id, selected.id)
                        }
                    }
                }
                else
                {
                    this.lastSelectedNode?.applyMaterialHierarchically(this.normalMaterial, this.overMaterial)
                    this.lastSelectedNode = node
                    this.actionCutEdge.isEnabled = node.id.startsWith(MeshEditor.EDGE_HEADER)
                    node.applyMaterialHierarchically(this.selectMaterial)
                }
            }
        }
        else if (this.meshHasMoved && ! mouseState.leftButtonDown)
        {
            this.meshHasMoved = false
            this.lastSelectedNode = null
            this.actionCutEdge.isEnabled = false
            this.updateGrid()
        }

        this.mouseX = mouseX
        this.mouseY = mouseY
    }

    private fun cutEdge()
    {
        val selectedNode = this.lastSelectedNode ?: return
        val name = selectedNode.id

        if (! name.startsWith(MeshEditor.EDGE_HEADER))
        {
            return
        }

        val edgeIndex = name.substring(MeshEditor.EDGE_HEADER.length)
            .int(- 1)

        if (edgeIndex < 0)
        {
            return
        }

        val edge = this.editableObject.edges[edgeIndex]
        val secondEdgeIndex =
            this.editableObject.edges.indexOfFirst { edgeTested -> edgeTested.startIndex == edge.endIndex && edgeTested.endIndex == edge.startIndex }
        val start = this.editableObject.points[edge.startIndex]
        val end = this.editableObject.points[edge.endIndex]
        val middlePoint = start.middle(end)
        val middleIndex = this.editableObject.addPoint(middlePoint)

        for (indexFace in 0 until this.editableObject.faces.size)
        {
            val face = this.editableObject.faces[indexFace]
            var indexEdge = face.edges.indexOf(edgeIndex)

            if (indexEdge >= 0)
            {
                val endIndex = edge.endIndex
                edge.endIndex = middleIndex
                val newEdgeIndex = this.editableObject.addEdge(middleIndex, endIndex)
                this.editableObject.faces[indexFace].edges.add(indexEdge + 1, newEdgeIndex)
            }

            indexEdge = face.edges.indexOf(secondEdgeIndex)

            if (indexEdge >= 0)
            {
                val secondEdge = this.editableObject.edges[secondEdgeIndex]
                val endIndex = secondEdge.endIndex
                secondEdge.endIndex = middleIndex
                val newEdgeIndex = this.editableObject.addEdge(middleIndex, endIndex)
                this.editableObject.faces[indexFace].edges.add(indexEdge + 1, newEdgeIndex)
            }
        }

        this.meshHasMoved = false
        this.lastSelectedNode = null
        this.actionCutEdge.isEnabled = false
        this.editableObject.refreshObject()
        this.updateGrid()
    }

    private fun joinNodes(nodeVertex1 : String, nodeVertex2 : String)
    {
        val vertex1 =
            nodeVertex1
                .substring(MeshEditor.VERTEX_HEADER.length)
                .int(- 1)
        val vertex2 =
            nodeVertex2
                .substring(MeshEditor.VERTEX_HEADER.length)
                .int(- 1)

        if (vertex1 < 0 || vertex2 < 0)
        {
            return
        }

        val edgeIndices1 = ArrayList<Int>()
        val edgeIndices2 = ArrayList<Int>()

        for ((index, edge) in this.editableObject.edges.withIndex())
        {
            if (edge.startIndex == vertex1 || edge.endIndex == vertex1)
            {
                if (edge.startIndex == vertex2 || edge.endIndex == vertex2)
                {
                    return
                }

                edgeIndices1.add(index)
            }
            else if (edge.startIndex == vertex2 || edge.endIndex == vertex2)
            {
                edgeIndices2.add(index)
            }
        }

        if (edgeIndices1.isEmpty() || edgeIndices2.isEmpty())
        {
            return
        }

        val faceIndices1 = ArrayList<Pair<Int, Int>>()
        val faceIndices2 = ArrayList<Pair<Int, Int>>()

        for ((indexFace, face) in this.editableObject.faces.withIndex())
        {
            for (edgeIndex in edgeIndices1)
            {
                val index = face.edges.indexOf(edgeIndex)

                if (index >= 0)
                {
                    faceIndices1.add(Pair(indexFace, edgeIndex))
                }
            }

            for (edgeIndex in edgeIndices2)
            {
                val index = face.edges.indexOf(edgeIndex)

                if (index >= 0)
                {
                    faceIndices2.add(Pair(indexFace, edgeIndex))
                }
            }
        }

        for (index in faceIndices1.size - 1 downTo 0)
        {
            val faceIndex = faceIndices1[index].first

            if (faceIndices2.none { (faceIndex2, _) -> faceIndex2 == faceIndex })
            {
                faceIndices1.removeAt(index)
            }
        }

        for (index in faceIndices2.size - 1 downTo 0)
        {
            val faceIndex = faceIndices2[index].first

            if (faceIndices1.none { (faceIndex2, _) -> faceIndex2 == faceIndex })
            {
                faceIndices2.removeAt(index)
            }
        }

        if (faceIndices1.isEmpty() || faceIndices2.isEmpty())
        {
            return
        }

        val faceIndex = faceIndices1[0].first

        if (faceIndices1.any { (faceIndex2, _) -> faceIndex2 != faceIndex }
            || faceIndices2.any { (faceIndex2, _) -> faceIndex2 != faceIndex }
            || faceIndices1.size != 2 || faceIndices2.size != 2)
        {
            return
        }

        val edgeIndex1Start = faceIndices1[0].second
        val edgeIndex2Start = faceIndices2[0].second

        val editableFace = this.editableObject.faces[faceIndex]
        this.editableObject.faces.removeAt(faceIndex)

        val newEdgeIndex1 = this.editableObject.addEdge(vertex1, vertex2)
        val newEdgeIndex2 = this.editableObject.addEdge(vertex2, vertex1)
        val newFace1 = EditableFace()
        val newFace2 = EditableFace()
        var inside1 = editableFace.edges.indexOf(edgeIndex1Start) <= editableFace.edges.indexOf(edgeIndex2Start)

        for (edgeIndex in editableFace.edges)
        {
            if (edgeIndex == edgeIndex2Start)
            {
                newFace2.edges.add(edgeIndex)
                newFace2.edges.add(newEdgeIndex2)
                inside1 = true
            }
            else if (edgeIndex == edgeIndex1Start)
            {
                newFace1.edges.add(edgeIndex)
                newFace1.edges.add(newEdgeIndex1)
                inside1 = false
            }
            else if (inside1)
            {
                newFace1.edges.add(edgeIndex)
            }
            else
            {
                newFace2.edges.add(edgeIndex)
            }
        }

        this.editableObject.faces.add(newFace1)
        this.editableObject.faces.add(newFace2)

        this.meshHasMoved = false
        this.lastSelectedNode = null
        this.actionCutEdge.isEnabled = false
        this.editableObject.refreshObject()
        this.updateGrid()
    }
}