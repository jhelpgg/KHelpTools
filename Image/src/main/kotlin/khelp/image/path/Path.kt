package khelp.image.path

import khelp.image.JHelpImage
import khelp.ui.font.JHelpFont
import khelp.ui.utilities.AFFINE_TRANSFORM
import khelp.ui.utilities.FLATNESS
import khelp.utilities.extensions.bounds
import java.awt.Shape
import java.awt.geom.PathIterator
import kotlin.math.max
import kotlin.math.min

/**
 * Describe a path composed of segments.
 *
 * Segments may be small to approximate a curve
 */
class Path : Iterable<Segment>
{
    /**Path segments*/
    private val segments = ArrayList<Segment>()

    /**
     * Draw path in image
     *
     * @param parent       Image where draw (MUST be in draw mode)
     * @param element      Image to repeat
     * @param alphaMix     Indicates if mix alpha
     * @param percentStart Percent start
     * @param percentEnd   Percent end
     */
    internal fun drawPath(parent : JHelpImage, element : JHelpImage, alphaMix : Boolean,
                          percentStart : Double, percentEnd : Double)
    {
        val size = this.segments.size
        val start = (size * min(percentStart, percentEnd)).toInt()
            .bounds(0, size)
        val limit = (size * max(percentStart, percentEnd)).toInt()
            .bounds(0, size)
        val xElement = element.width / 2.0
        val yElement = element.height / 2.0
        var segment : Segment

        for (i in start until limit)
        {
            segment = this.segments[i]
            parent.repeatOnLine(Math.round(segment.x1 - xElement)
                                    .toInt(), Math.round(segment.y1 - yElement)
                                    .toInt(),
                                Math.round(segment.x2 - xElement)
                                    .toInt(), Math.round(segment.y2 - yElement)
                                    .toInt(),
                                element, alphaMix)
        }
    }

    /**
     * Append one segment
     *
     * @param x1 X start
     * @param y1 Y start
     * @param x2 X end
     * @param y2 Y end
     */
    fun append(x1 : Double, y1 : Double, x2 : Double, y2 : Double) = this.segments.add(Segment(x1, y1, x2, y2))

    /**
     * Add an other path
     *
     * @param path Path to add
     */
    fun append(path : Path) = this.segments.addAll(path.segments)

    /**
     * Add a shape
     *
     * @param shape Shape to add
     */
    fun append(shape : Shape)
    {
        val pathIterator = shape.getPathIterator(AFFINE_TRANSFORM, FLATNESS)
        var xStart = 0.0
        var yStart = 0.0
        var x = 0.0
        var y = 0.0
        var xx : Double
        var yy : Double
        val elements = DoubleArray(6)
        var type : Int

        while (! pathIterator.isDone)
        {
            type = pathIterator.currentSegment(elements)

            when (type)
            {
                PathIterator.SEG_MOVETO ->
                {
                    x = elements[0]
                    xStart = x
                    y = elements[1]
                    yStart = y
                }
                PathIterator.SEG_LINETO ->
                {
                    xx = elements[0]
                    yy = elements[1]

                    this.append(x, y, xx, yy)

                    x = xx
                    y = yy
                }
                PathIterator.SEG_CLOSE  ->
                {
                    this.append(x, y, xStart, yStart)

                    x = xStart
                    y = yStart
                }
            }

            pathIterator.next()
        }
    }

    /**
     * Append a text
     *
     * @param text Text to write
     * @param font Font to use
     * @param x    X
     * @param y    Y
     */
    fun appendText(text : String, font : JHelpFont, x : Int, y : Int) = this.append(font.shape(text, x, y))

    /**
     * Obtain one segment
     *
     * @param index Segment index
     * @return Segment
     */
    operator fun get(index : Int) = this.segments[index]

    /**
     * Iterator on segments
     * @return Iterator on segments
     * @see Iterable.iterator
     */
    override fun iterator() = this.segments.iterator();

    val size
        get() = this.segments.size
}