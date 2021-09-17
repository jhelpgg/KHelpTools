package khelp.engine3d.render

import khelp.engine3d.extensions.degreeToRadian
import khelp.engine3d.geometry.Point3D
import khelp.engine3d.geometry.Rotf
import khelp.engine3d.geometry.Vec3f
import khelp.engine3d.utils.ThreadOpenGL
import khelp.utilities.collections.queue.Queue
import khelp.utilities.extensions.bounds
import org.lwjgl.opengl.GL11
import java.util.Stack
import java.util.Vector
import kotlin.math.max
import kotlin.math.min

open class Node(val id : String) : Iterable<Node>
{
    companion object
    {
        private const val MIN_SCALE = 0.001f
    }

    private var limitMinX = Float.NEGATIVE_INFINITY
    private var limitMaxX = Float.POSITIVE_INFINITY
    private var limitMinY = Float.NEGATIVE_INFINITY
    private var limitMaxY = Float.POSITIVE_INFINITY
    private var limitMinZ = Float.NEGATIVE_INFINITY
    private var limitMaxZ = Float.POSITIVE_INFINITY

    private var limitMinAngleX = Float.NEGATIVE_INFINITY
    private var limitMaxAngleX = Float.POSITIVE_INFINITY
    private var limitMinAngleY = Float.NEGATIVE_INFINITY
    private var limitMaxAngleY = Float.POSITIVE_INFINITY
    private var limitMinAngleZ = Float.NEGATIVE_INFINITY
    private var limitMaxAngleZ = Float.POSITIVE_INFINITY

    private var limitMinScaleX = Node.MIN_SCALE
    private var limitMaxScaleX = Float.POSITIVE_INFINITY
    private var limitMinScaleY = Node.MIN_SCALE
    private var limitMaxScaleY = Float.POSITIVE_INFINITY
    private var limitMinScaleZ = Node.MIN_SCALE
    private var limitMaxScaleZ = Float.POSITIVE_INFINITY

    var x : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinX, this.limitMaxX)
        }
    var y : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinY, this.limitMaxY)
        }
    var z : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinZ, this.limitMaxZ)
        }

    var angleX : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinAngleX, this.limitMaxAngleX)
        }
    var angleY : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinAngleY, this.limitMaxAngleY)
        }
    var angleZ : Float = 0f
        set(value)
        {
            field = value.bounds(this.limitMinAngleZ, this.limitMaxAngleZ)
        }

    var scaleX : Float = 1f
        set(value)
        {
            field = value.bounds(this.limitMinScaleX, this.limitMaxScaleX)
        }
    var scaleY : Float = 1f
        set(value)
        {
            field = value.bounds(this.limitMinScaleY, this.limitMaxScaleY)
        }
    var scaleZ : Float = 1f
        set(value)
        {
            field = value.bounds(this.limitMinScaleZ, this.limitMaxScaleZ)
        }

    /**Parent node*/
    var parent : Node? = null
        protected set
    private val children = Vector<Node>(8)

    /**Node Z order*/
    internal var zOrder = 0f
    internal var countInRender = false

    @NodeDSL
    fun node(id : String, nodeCreator : Node.() -> Unit)
    {
        val child = Node(id)
        child.parent = this
        nodeCreator(child)
        this.children.add(child)
    }

    @NodeDSL
    fun object3D(id : String, objectCreator : Object3D.() -> Unit)
    {
        val child = Object3D(id)
        child.parent = this
        objectCreator(child)
        this.children.add(child)
    }

    fun <N : Node> findById(id : String) : N?
    {
        val queue = Queue<Node>()
        queue.inQueue(this)

        while (queue.isNotEmpty())
        {
            val node = queue.outQueue()

            if (id == node.id)
            {
                return node as N
            }

            for (child in node.children)
            {
                queue.inQueue(child)
            }
        }

        return null
    }

    fun limitX(limit1 : Float, limit2 : Float)
    {
        this.limitMinX = min(limit1, limit2)
        this.limitMaxX = max(limit1, limit2)
        this.x = this.x.bounds(this.limitMinX, this.limitMaxX)
    }

    fun limitY(limit1 : Float, limit2 : Float)
    {
        this.limitMinY = min(limit1, limit2)
        this.limitMaxY = max(limit1, limit2)
        this.y = this.y.bounds(this.limitMinY, this.limitMaxY)
    }

    fun limitZ(limit1 : Float, limit2 : Float)
    {
        this.limitMinZ = min(limit1, limit2)
        this.limitMaxZ = max(limit1, limit2)
        this.z = this.z.bounds(this.limitMinZ, this.limitMaxZ)
    }

    fun limitAngleX(limit1 : Float, limit2 : Float)
    {
        this.limitMinAngleX = min(limit1, limit2)
        this.limitMaxAngleX = max(limit1, limit2)
        this.angleX = this.angleX.bounds(this.limitMinAngleX, this.limitMaxAngleX)
    }

    fun limitAngleY(limit1 : Float, limit2 : Float)
    {
        this.limitMinAngleY = min(limit1, limit2)
        this.limitMaxAngleY = max(limit1, limit2)
        this.angleY = this.angleY.bounds(this.limitMinAngleY, this.limitMaxAngleY)
    }

    fun limitAngleZ(limit1 : Float, limit2 : Float)
    {
        this.limitMinAngleZ = min(limit1, limit2)
        this.limitMaxAngleZ = max(limit1, limit2)
        this.angleZ = this.angleZ.bounds(this.limitMinAngleZ, this.limitMaxAngleZ)
    }

    fun limitScaleX(limit1 : Float, limit2 : Float)
    {
        this.limitMinScaleX = max(Node.MIN_SCALE, min(limit1, limit2))
        this.limitMaxScaleX = max(limit1, limit2)
        this.scaleX = this.scaleX.bounds(this.limitMinScaleX, this.limitMaxScaleX)
    }

    fun limitScaleY(limit1 : Float, limit2 : Float)
    {
        this.limitMinScaleY = max(Node.MIN_SCALE, min(limit1, limit2))
        this.limitMaxScaleY = max(limit1, limit2)
        this.scaleY = this.scaleY.bounds(this.limitMinScaleY, this.limitMaxScaleY)
    }

    fun limitScaleZ(limit1 : Float, limit2 : Float)
    {
        this.limitMinScaleZ = max(Node.MIN_SCALE, min(limit1, limit2))
        this.limitMaxScaleZ = max(limit1, limit2)
        this.scaleZ = this.scaleZ.bounds(this.limitMinScaleZ, this.limitMaxScaleZ)
    }

    fun freeX()
    {
        this.limitMinX = Float.NEGATIVE_INFINITY
        this.limitMaxX = Float.POSITIVE_INFINITY
    }

    fun freeY()
    {
        this.limitMinY = Float.NEGATIVE_INFINITY
        this.limitMaxY = Float.POSITIVE_INFINITY
    }

    fun freeZ()
    {
        this.limitMinZ = Float.NEGATIVE_INFINITY
        this.limitMaxZ = Float.POSITIVE_INFINITY
    }

    fun freeAngleX()
    {
        this.limitMinAngleX = Float.NEGATIVE_INFINITY
        this.limitMaxAngleX = Float.POSITIVE_INFINITY
    }

    fun freeAngleY()
    {
        this.limitMinAngleY = Float.NEGATIVE_INFINITY
        this.limitMaxAngleY = Float.POSITIVE_INFINITY
    }

    fun freeAngleZ()
    {
        this.limitMinAngleZ = Float.NEGATIVE_INFINITY
        this.limitMaxAngleZ = Float.POSITIVE_INFINITY
    }

    fun freeScaleX()
    {
        this.limitMinScaleX = Node.MIN_SCALE
        this.limitMaxScaleX = Float.POSITIVE_INFINITY
    }

    fun freeScaleY()
    {
        this.limitMinScaleY = Node.MIN_SCALE
        this.limitMaxScaleY = Float.POSITIVE_INFINITY
    }

    fun freeScaleZ()
    {
        this.limitMinScaleZ = Node.MIN_SCALE
        this.limitMaxScaleZ = Float.POSITIVE_INFINITY
    }

    /**
     * Compute a point projection from world space to node space
     *
     * @param point3D Point to project
     * @return Projected point
     */
    fun reverseProjection(point3D : Point3D) : Point3D
    {
        var point = point3D
        var vect = point.toVect3f()
        val rotZ = Rotf(Vec3f(0f, 0f, 1f), - this.angleZ.degreeToRadian)
        vect = rotZ.rotateVector(vect)
        val rotY = Rotf(Vec3f(0f, 1f, 0f), - this.angleY.degreeToRadian)
        vect = rotY.rotateVector(vect)
        val rotX = Rotf(Vec3f(1f, 0f, 0f), - this.angleX.degreeToRadian)
        vect = rotX.rotateVector(vect)
        point = Point3D(vect.x - this.x, vect.y - this.y, vect.z - this.z)

        if (this.parent != null)
        {
            point = this.parent !!.reverseProjection(point)
        }

        return point
    }

    open fun center() : Point3D = Point3D(this.x, this.y, this.z)

    override fun iterator() : Iterator<Node> = this.children.iterator()

    /**
     * Locate the node in the scene
     */
    @ThreadOpenGL
    internal fun matrix()
    {
        GL11.glTranslatef(this.x, this.y, this.z)
        GL11.glRotatef(this.angleX, 1f, 0f, 0f)
        GL11.glRotatef(this.angleY, 0f, 1f, 0f)
        GL11.glRotatef(this.angleZ, 0f, 0f, 1f)
        GL11.glScalef(this.scaleX, this.scaleY, this.scaleZ)
    }

    /**
     * Apply the matrix for to go root to this node
     */
    @ThreadOpenGL
    internal fun matrixRootToMe()
    {
        val stack = Stack<Node>()
        var node : Node? = this

        while (node != null)
        {
            stack.push(node)
            node = node.parent
        }

        while (! stack.isEmpty())
        {
            stack.pop()
                .matrix()
        }
    }

    /**
     * Render specific, used by sub-classes
     */
    @ThreadOpenGL
    internal open fun renderSpecific()
    {
    }
}
