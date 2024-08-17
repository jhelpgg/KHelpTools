package khelp.algorithm.delaunay

import java.util.Collections
import java.util.Stack
import java.util.concurrent.atomic.AtomicBoolean
import khelp.algorithm.delaunay.adjacent.AdjacentSide
import khelp.algorithm.delaunay.adjacent.adjacent

class Delaunay
{
    private val points = ArrayList<PointIndexed>()
    private val triangles = ArrayList<TriangleIndexed>()
    private val needCompute = AtomicBoolean(true)

    fun points() : List<PointIndexed> =
        Collections.unmodifiableList(this.points)

    fun addPoint(x : Float, y : Float)
    {
        this.needCompute.set(true)
        this.points.add(PointIndexed(this.points.size, x, y))
    }

    fun addIgnorePoint(x : Float, y : Float)
    {
        this.needCompute.set(true)
        this.points.add(PointIndexed(-10 - this.points.size, x, y))
    }

    fun triangles(spy : (points : List<PointIndexed>, triangles : List<TriangleIndexed>) -> Unit = { _, _ -> }) : List<TriangleIndexed>
    {
        if (this.points.size < 3)
        {
            return emptyList()
        }

        if (this.needCompute.compareAndSet(true, false))
        {
            this.computeTriangles(spy)
        }

        return this.triangles
    }

    private fun computeTriangles(spy : (points : List<PointIndexed>, triangles : List<TriangleIndexed>) -> Unit)
    {
        this.triangles.clear()
        spy(this.points, this.triangles)
        val enclosingCircle = this.enclosingCircle()
        val superTriangle = triangleOfInscribedCircle(enclosingCircle, 1.1f)
        this.triangles.add(superTriangle)
        spy(this.points, this.triangles)

        for (point in this.points)
        {
            this.add(point, spy)
        }

        this.cleanTriangles(spy)
    }

    private fun enclosingCircle() : EnclosingCircle
    {
        val enclosingCircle = EnclosingCircle()

        for (point in this.points)
        {
            enclosingCircle.addPoint(point.x, point.y)
        }

        return enclosingCircle
    }

    private fun cleanTriangles(spy : (points : List<PointIndexed>, triangles : List<TriangleIndexed>) -> Unit)
    {
        val iterator = this.triangles.iterator()

        while (iterator.hasNext())
        {
            val triangle = iterator.next()

            if (triangle.point1.index < 0 || triangle.point2.index < 0 || triangle.point3.index < 0)
            {
                iterator.remove()
                spy(this.points, this.triangles)
            }
        }
    }

    private fun add(point : PointIndexed,
                    spy : (points : List<PointIndexed>, triangles : List<TriangleIndexed>) -> Unit)
    {
        val triangle = this.triangleContainsPoint(point)
        this.triangles.remove(triangle)
        val triangle1 = TriangleIndexed(point, triangle.point1, triangle.point2)
        val triangle2 = TriangleIndexed(point, triangle.point2, triangle.point3)
        val triangle3 = TriangleIndexed(point, triangle.point3, triangle.point1)
        this.triangles.add(triangle1)
        this.triangles.add(triangle2)
        this.triangles.add(triangle3)
        spy(this.points, this.triangles)
        this.edgeFlip(triangle1, spy)
        this.edgeFlip(triangle2, spy)
        this.edgeFlip(triangle3, spy)
    }

    private fun triangleContainsPoint(point : PointIndexed) : TriangleIndexed
    {
        for (triangle in this.triangles)
        {
            if (point in triangle)
            {
                return triangle
            }
        }

        throw RuntimeException("Triangle Not Found! point=$point, triangles=${this.triangles}")
    }

    private fun edgeFlip(triangleIndexed : TriangleIndexed,
                         spy : (points : List<PointIndexed>, triangles : List<TriangleIndexed>) -> Unit)
    {
        val stack = Stack<TriangleIndexed>()
        stack.push(triangleIndexed)

        while (stack.isNotEmpty())
        {
            val triangle = stack.pop()

            val iterator = this.triangles.iterator()

            while (iterator.hasNext())
            {
                val triangleTested = iterator.next()

                if (triangle == triangleTested)
                {
                    continue
                }

                val adjacent = adjacent(triangle, triangleTested)

                if (adjacent is AdjacentSide && triangleTested.inCircumscribedCircle(adjacent.firstFree))
                {
                    this.triangles.remove(triangle)
                    this.triangles.remove(triangleTested)
                    val triangle1 = TriangleIndexed(adjacent.firstFree, adjacent.side1, adjacent.secondFree)
                    val triangle2 = TriangleIndexed(adjacent.firstFree, adjacent.side2, adjacent.secondFree)
                    this.triangles.add(triangle1)
                    this.triangles.add(triangle2)
                    spy(this.points, this.triangles)
                    stack.push(triangle1)
                    stack.push(triangle2)
                    break
                }
            }
        }
    }
}