package khelp.engine3d.geometry.path

import khelp.utilities.serialization.ParsableSerializable
import khelp.utilities.serialization.Parser
import khelp.utilities.serialization.Serializer

internal class PathElementInfo private constructor() : ParsableSerializable
{
    companion object
    {
        fun provider() : PathElementInfo = PathElementInfo()
    }

    private var name = ""
    private var data = FloatArray(6)

    constructor(pathElement : PathElement) : this()
    {
        this.name = pathElement.javaClass.name

        when (pathElement)
        {
            is PathMove      ->
            {
                this.data[0] = pathElement.x
                this.data[1] = pathElement.y
            }
            is PathLineTo    ->
            {
                this.data[0] = pathElement.x
                this.data[1] = pathElement.y
            }
            is PathQuadratic ->
            {
                this.data[0] = pathElement.controlX
                this.data[1] = pathElement.controlY
                this.data[2] = pathElement.x
                this.data[3] = pathElement.y
            }
            is PathCubic     ->
            {
                this.data[0] = pathElement.control1X
                this.data[1] = pathElement.control1Y
                this.data[2] = pathElement.control2X
                this.data[3] = pathElement.control2Y
                this.data[4] = pathElement.x
                this.data[5] = pathElement.y
            }
            else             -> Unit
        }
    }

    fun get() : PathElement =
        when (this.name)
        {
            PathClose::class.java.name     ->
                PathClose
            PathMove::class.java.name      ->
                PathMove(this.data[0], this.data[1])
            PathLineTo::class.java.name    ->
                PathLineTo(this.data[0], this.data[1])
            PathQuadratic::class.java.name ->
                PathQuadratic(this.data[0], this.data[1], this.data[2], this.data[3])
            PathCubic::class.java.name     ->
                PathCubic(this.data[0], this.data[1], this.data[2], this.data[3], this.data[4], this.data[5])
            else                           ->
                throw IllegalStateException("Path '${this.name}'  not defined")
        }

    override fun serialize(serializer : Serializer)
    {
        serializer.setString("name", this.name)
        serializer.setFloatArray("data", this.data)
    }

    override fun parse(parser : Parser)
    {
        this.name = parser.getString("name")
        this.data = parser.getFloatArray("data")
    }
}