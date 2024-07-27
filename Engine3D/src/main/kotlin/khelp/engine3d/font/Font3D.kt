package khelp.engine3d.font

import java.awt.geom.PathIterator
import khelp.engine3d.render.Node
import khelp.engine3d.render.NodeWithMaterial
import khelp.engine3d.render.Object3D
import khelp.engine3d.render.ObjectClone
import khelp.engine3d.utils.delaunay.Delaunay
import khelp.ui.utilities.AFFINE_TRANSFORM
import khelp.ui.utilities.BUTTON_FONT
import khelp.utilities.collections.Cache
import khelp.utilities.math.square
import kotlin.math.round
import kotlin.math.sqrt

object Font3D
{
    private val cache = Cache<Char, Object3D>(1024, this::createCharacter)

    fun text(text : String) : Node
    {
        val node = Node(text)
        var width = 0f

        for ((index, char) in text.withIndex())
        {
            if (char <= ' ')
            {
                width += 0.3f
                continue
            }

            val nodeWithMaterial = this.character("${text}_$index", char)
            node.addChild(nodeWithMaterial)
            val virtualBox = nodeWithMaterial.virtualBox
            nodeWithMaterial.x = width - virtualBox.minX
            width += virtualBox.maxX - virtualBox.minX + 0.1f
        }

        val delta = width / 2f

        for (child in node)
        {
            child.x -= delta
        }

        return node
    }

    fun character(id : String, character : Char) : NodeWithMaterial
    {
        val node = ObjectClone(id, this.cache[character])
        node.angleX = 180f
        return node
    }

    private fun createCharacter(character : Char) : Object3D
    {
        val (delaunay, edges) = this.computeDelaunay(character)
        return this.createObject(character, delaunay, edges)
    }

    private fun computeDelaunay(character : Char) : Pair<Delaunay, List<Edge>>
    {
        val edges = ArrayList<Edge>()
        val font = BUTTON_FONT
        val shape = font.shape("$character")
        val path = shape.getPathIterator(AFFINE_TRANSFORM, 0.1)
        val coordinates = FloatArray(2)
        val delaunay = Delaunay()
        var firstX = 0f
        var firstY = 0f
        var lastX = 0f
        var lastY = 0f
        val delaunayFiller = DelaunayFiller(shape, delaunay)
        var distanceBorder = 0f
        var startIndex = 0

        while (path.isDone.not())
        {
            val tag = path.currentSegment(coordinates)
            val x = coordinates[0]
            val y = coordinates[1]

            when (tag)
            {
                PathIterator.SEG_MOVETO ->
                {
                    delaunayFiller.moveTo(x, y)
                    lastX = x
                    lastY = y
                    firstX = x
                    firstY = y
                    distanceBorder = 0f
                    startIndex = edges.size
                }

                PathIterator.SEG_LINETO ->
                {
                    val distance = sqrt(square(x - lastX) + square(y - lastY))
                    val nb = round(distance * 2f).toInt()

                    if (nb > 0)
                    {
                        val step = 1f / nb.toFloat()
                        var coeficient = step * (nb - 1f)

                        for (count in 1 until nb)
                        {
                            delaunayFiller.lineTo((x + (lastX - x) * coeficient), (y + (lastY - y) * coeficient))
                            coeficient -= step
                        }
                    }

                    delaunayFiller.lineTo(x, y)
                    val edge = Edge(lastX, lastY, x, y)
                    distanceBorder += edge.weight
                    edges.add(edge)
                    lastX = x
                    lastY = y
                }

                PathIterator.SEG_CLOSE  ->
                {
                    val edge = Edge(lastX, lastY, firstX, firstY)
                    distanceBorder += edge.weight
                    edges.add(edge)
                    var before = 0f

                    for (index in startIndex until edges.size)
                    {
                        edges[index].before = before
                        before += edges[index].weight / distanceBorder
                        edges[index].weight = before
                    }

                    delaunayFiller.close()
                }
            }

            path.next()
        }

        return Pair(delaunay, edges)
    }

    private fun createObject(character : Char, delaunay : Delaunay, edges : List<Edge>) : Object3D
    {
        val triangles = delaunay.triangles()
        var minX = Float.POSITIVE_INFINITY
        var maxX = Float.NEGATIVE_INFINITY
        var minY = Float.POSITIVE_INFINITY
        var maxY = Float.NEGATIVE_INFINITY

        for (triangle in triangles)
        {
            minX = minOf(minX, triangle.point1.x, triangle.point2.x, triangle.point3.x)
            maxX = maxOf(maxX, triangle.point1.x, triangle.point2.x, triangle.point3.x)
            minY = minOf(minY, triangle.point1.y, triangle.point2.y, triangle.point3.y)
            maxY = maxOf(maxY, triangle.point1.y, triangle.point2.y, triangle.point3.y)
        }

        val dx = maxX - minX
        val dy = maxY - minY
        val cx = (maxX + minX) / 2f
        val cy = (maxY + minY) / 2f
        val x : (Float) -> Float = { value -> (value - cx) * 0.1f }
        val y : (Float) -> Float = { value -> (value - cy) * 0.1f }
        val u : (Float) -> Float = { value -> (value - minX) / dx }
        val v : (Float) -> Float = { value -> (value - minY) / dy }

        val object3D = Object3D("Character_$character")

        object3D.mesh {
            for (triangle in triangles)
            {
                face {
                    add(x(triangle.point1.x), y(triangle.point1.y), -0.5f,
                        u(triangle.point1.x), v(triangle.point1.y),
                        0f, 0f, -1f)

                    add(x(triangle.point2.x), y(triangle.point2.y), -0.5f,
                        u(triangle.point2.x), v(triangle.point2.y),
                        0f, 0f, -1f)

                    add(x(triangle.point3.x), y(triangle.point3.y), -0.5f,
                        u(triangle.point3.x), v(triangle.point3.y),
                        0f, 0f, -1f)
                }

                face {
                    add(x(triangle.point1.x), y(triangle.point1.y), 0.5f,
                        u(triangle.point1.x), v(triangle.point1.y),
                        0f, 0f, 1f)

                    add(x(triangle.point3.x), y(triangle.point3.y), 0.5f,
                        u(triangle.point3.x), v(triangle.point3.y),
                        0f, 0f, 1f)

                    add(x(triangle.point2.x), y(triangle.point2.y), 0.5f,
                        u(triangle.point2.x), v(triangle.point2.y),
                        0f, 0f, 1f)
                }
            }

            for (edge in edges)
            {
                val coefficient = sqrt(edge.x1 * edge.x1 + edge.y1 * edge.y1)
                val nx = edge.y1 / coefficient
                val ny = edge.x1 / coefficient

                face {
                    add(x(edge.x1), y(edge.y1), -0.5f,
                        edge.before, 0f,
                        nx, ny, 0f)

                    add(x(edge.x1), y(edge.y1), 0.5f,
                        edge.before, 1f,
                        nx, ny, 0f)

                    add(x(edge.x2), y(edge.y2), 0.5f,
                        edge.weight, 1f,
                        nx, ny, 0f)

                    add(x(edge.x2), y(edge.y2), -0.5f,
                        edge.weight, 0f,
                        nx, ny, 0f)
                }
            }
        }

        return object3D
    }
}