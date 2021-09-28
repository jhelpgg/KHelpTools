package khelp.engine3d.render

import khelp.engine3d.extensions.degreeToRadian
import khelp.engine3d.geometry.Point3D
import khelp.engine3d.geometry.Rotf
import khelp.engine3d.geometry.Vec3f
import khelp.engine3d.geometry.VirtualBox
import khelp.engine3d.render.complex.Dice
import khelp.engine3d.render.font.Font3D
import khelp.engine3d.render.prebuilt.Box
import khelp.engine3d.render.prebuilt.BoxUV
import khelp.engine3d.render.prebuilt.FaceUV
import khelp.engine3d.render.prebuilt.Plane
import khelp.engine3d.render.prebuilt.Revolution
import khelp.engine3d.render.prebuilt.Sphere
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
    var visible : Boolean = true
    open val center : Point3D get() = Point3D(this.x, this.y, this.z)
    open val virtualBox : VirtualBox get() = VirtualBox()
    private val children = Vector<Node>(8)
    val root : Node by lazy {
        var root = this

        while (root.parent != null)
        {
            root = root.parent !!
        }

        root
    }

    fun totalBox() : VirtualBox
    {
        val virtualBox = VirtualBox()
        val stack = Stack<Node>()
        stack.push(this)

        while (stack.isNotEmpty())
        {
            val node = stack.pop()
            virtualBox.add(node.projectedBox())

            for (child in node.children)
            {
                stack.push(child)
            }
        }

        return virtualBox
    }

    private fun projectedBox() : VirtualBox
    {
        val box = this.virtualBox

        if (box.empty)
        {
            return box
        }

        val virtualBox = VirtualBox()
        virtualBox.add(this.projection(Point3D(box.minX, box.minY, box.minZ)))
        virtualBox.add(this.projection(Point3D(box.minX, box.minY, box.maxZ)))
        virtualBox.add(this.projection(Point3D(box.minX, box.maxY, box.minZ)))
        virtualBox.add(this.projection(Point3D(box.minX, box.maxY, box.maxZ)))
        virtualBox.add(this.projection(Point3D(box.maxX, box.minY, box.minZ)))
        virtualBox.add(this.projection(Point3D(box.maxX, box.minY, box.maxZ)))
        virtualBox.add(this.projection(Point3D(box.maxX, box.maxY, box.minZ)))
        virtualBox.add(this.projection(Point3D(box.maxX, box.maxY, box.maxZ)))
        return virtualBox
    }

    fun projectionPure(box : VirtualBox) : VirtualBox
    {
        val virtualBox = VirtualBox()
        virtualBox.add(this.projectionPure(Point3D(box.minX, box.minY, box.minZ)))
        virtualBox.add(this.projectionPure(Point3D(box.minX, box.minY, box.maxZ)))
        virtualBox.add(this.projectionPure(Point3D(box.minX, box.maxY, box.minZ)))
        virtualBox.add(this.projectionPure(Point3D(box.minX, box.maxY, box.maxZ)))
        virtualBox.add(this.projectionPure(Point3D(box.maxX, box.minY, box.minZ)))
        virtualBox.add(this.projectionPure(Point3D(box.maxX, box.minY, box.maxZ)))
        virtualBox.add(this.projectionPure(Point3D(box.maxX, box.maxY, box.minZ)))
        virtualBox.add(this.projectionPure(Point3D(box.maxX, box.maxY, box.maxZ)))
        return virtualBox
    }

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

    @NodeDSL
    fun plane(id : String, faceUV : FaceUV = FaceUV(), planeCreator : Plane.() -> Unit)
    {
        val child = Plane(id, faceUV)
        child.parent = this
        planeCreator(child)
        this.children.add(child)
    }

    @NodeDSL
    fun box(id : String, boxUV : BoxUV = BoxUV(), boxCreator : Box.() -> Unit)
    {
        val child = Box(id, boxUV)
        child.parent = this
        boxCreator(child)
        this.children.add(child)
    }

    @NodeDSL
    fun sphere(id : String, slice : Int = 33, stack : Int = 33, multiplierU : Float = 1f, multiplierV : Float = 1f,
               sphereCreator : Sphere.() -> Unit)
    {
        val child = Sphere(id, slice, stack, multiplierU, multiplierV)
        child.parent = this
        sphereCreator(child)
        this.children.add(child)
    }

    @NodeDSL
    fun clone(id : String, nodeClonedId : String, objectCloneCreator : ObjectClone.() -> Unit)
    {
        val node = this.root.findById<Node>(nodeClonedId)
                   ?: throw NoSuchElementException("$nodeClonedId not yet defined")

        val child : ObjectClone =
            when (node)
            {
                is ObjectClone -> ObjectClone(id, node.reference)
                is Object3D    -> ObjectClone(id, node)
                else           ->
                    throw IllegalArgumentException(
                        "$nodeClonedId don't refer to ObjectClone or Object3D, but to ${node.javaClass.name}")
            }

        child.parent = this
        objectCloneCreator(child)
        this.children.add(child)
    }

    @NodeDSL
    fun revolution(id : String, revolutionCreator : Revolution.() -> Unit)
    {
        val child = Revolution(id)
        revolutionCreator(child)
        child.parent = this
        this.children.add(child)
    }

    @NodeDSL
    fun text(id : String, text : String, fontFamily : String = "Arial", textCreator : Node.() -> Unit)
    {
        val child = Font3D.font3D(fontFamily)
            .computeText(id, text)
        textCreator(child)
        child.parent = this
        this.children.add(child)
    }

    @NodeDSL
    fun dice(id : String, diceCreator : Dice.() -> Unit)
    {
        val child = Dice(id)
        diceCreator(child)
        child.parent = this
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

    fun cloneHierarchy() : Node
    {
        val clone =
            when (this)
            {
                is Object3D    -> ObjectClone("clone_${this.id}", this)
                is ObjectClone -> ObjectClone("clone_${this.id}", this.reference)
                else           -> Node("clone_${this.id}")
            }

        clone.x = this.x
        clone.y = this.y
        clone.z = this.z
        clone.angleX = this.angleX
        clone.angleY = this.angleY
        clone.angleZ = this.angleZ
        clone.scaleX = this.scaleX
        clone.scaleY = this.scaleY
        clone.scaleZ = this.scaleZ

        if (this is NodeWithMaterial && clone is NodeWithMaterial)
        {
            clone.material = this.material
            clone.materialForSelection = this.materialForSelection
            clone.twoSidedRule = this.twoSidedRule
        }

        for (child in this.children)
        {
            val childClone = child.cloneHierarchy()
            childClone.parent = clone
            clone.children.add(childClone)
        }

        return clone
    }

    fun applyMaterialHierarchically(material : Material, materialForSelection : Material = material)
    {
        val stack = Stack<Node>()
        stack.push(this)

        while (stack.isNotEmpty())
        {
            val node = stack.pop()

            if (node is NodeWithMaterial)
            {
                node.material = material
                node.materialForSelection = materialForSelection
            }

            for (child in node.children)
            {
                stack.push(child)
            }
        }
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
     * Compute a point projection from node space to world space
     *
     * @param point3D Point to project
     * @return Projected point
     */
    fun projection(point3D : Point3D) : Point3D
    {
        var point = point3D
        val stack = Stack<Node>()
        var node = this
        var parent = this.parent

        while (parent != null)
        {
            stack.push(node)
            node = parent
            parent = parent.parent
        }

        while (stack.isNotEmpty())
        {
            point = stack.pop()
                .projectionPure(point)
        }

        return this.projectionPure(point)
    }

    fun projectionPure(point3D : Point3D) : Point3D
    {
        var point = point3D
        point = point.add(this.x, this.y, this.z)
        var vect = point.toVect3f()
        val rotX = Rotf(Vec3f(1f, 0f, 0f), this.angleX.degreeToRadian)
        vect = rotX.rotateVector(vect)
        val rotY = Rotf(Vec3f(0f, 1f, 0f), this.angleY.degreeToRadian)
        vect = rotY.rotateVector(vect)
        val rotZ = Rotf(Vec3f(0f, 0f, 1f), this.angleZ.degreeToRadian)
        vect = rotZ.rotateVector(vect)
        return Point3D(vect)
    }

    /**
     * Compute a point projection from world space to node space
     *
     * @param point3D Point to project
     * @return Projected point
     */
    fun reverseProjection(point3D : Point3D) : Point3D
    {
        var node = this
        var parent = this.parent
        var point = point3D

        while (parent != null)
        {
            point = node.reverseProjectionPure(point3D)
            node = parent
            parent = node.parent
        }

        return point
    }

    internal fun reverseProjectionWithRoot(point3D : Point3D) : Point3D
    {
        var node : Node? = this
        var point = point3D

        while (node != null)
        {
            point = node.reverseProjectionPure(point3D)
            node = node.parent
        }

        return point
    }

    fun reverseProjection(virtualBox : VirtualBox) : VirtualBox
    {
        val box = VirtualBox()
        box.add(this.reverseProjection(Point3D(virtualBox.minX, virtualBox.minY, virtualBox.minZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.minX, virtualBox.minY, virtualBox.maxZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.minX, virtualBox.maxY, virtualBox.minZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.minX, virtualBox.maxY, virtualBox.maxZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.maxX, virtualBox.minY, virtualBox.minZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.maxX, virtualBox.minY, virtualBox.maxZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.maxX, virtualBox.maxY, virtualBox.minZ)))
        box.add(this.reverseProjection(Point3D(virtualBox.maxX, virtualBox.maxY, virtualBox.maxZ)))
        return box
    }

    private fun reverseProjectionPure(point3D : Point3D) : Point3D
    {
        var point = point3D
        var vect = point.toVect3f()
        val rotZ = Rotf(Vec3f(0f, 0f, 1f), - this.angleZ.degreeToRadian)
        vect = rotZ.rotateVector(vect)
        val rotY = Rotf(Vec3f(0f, 1f, 0f), - this.angleY.degreeToRadian)
        vect = rotY.rotateVector(vect)
        val rotX = Rotf(Vec3f(1f, 0f, 0f), - this.angleX.degreeToRadian)
        vect = rotX.rotateVector(vect)
        return Point3D(vect.x - this.x, vect.y - this.y, vect.z - this.z)
    }

    override fun iterator() : Iterator<Node> = this.children.iterator()

    internal fun addChild(node : Node)
    {
        node.parent = this
        this.children.add(node)
    }

    internal val numberOfChild : Int = this.children.size

    internal fun child(index : Int) : Node = this.children[index]

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

    override fun toString() : String =
        "${this.javaClass.name} : ${this.id} : ${this.hashCode()}"
}
