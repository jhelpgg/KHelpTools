package khelp.ui.layout.table

import khelp.ui.components.iterator
import khelp.ui.extensions.addLineBorder
import khelp.ui.utilities.computeMaximumDimension
import khelp.ui.utilities.computeMinimumDimension
import khelp.ui.utilities.computePreferredDimension
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.Insets
import java.awt.LayoutManager2
import javax.swing.JComponent
import kotlin.math.max
import kotlin.math.min


class TableLayout(var tableAspect : Boolean = false) : LayoutManager2
{
    /**
     * Last cell height computed by [.layoutContainer]
     */
    private var cellHeight = 0

    /**
     * Last cell width computed by [.layoutContainer]
     */
    private var cellWidth = 0

    /**
     * Associate components to their layout
     */
    private val components = HashMap<Component, TableLayoutConstraint>()

    /**
     * Fixed number of cells
     */
    private var fixedNumberOfCells : Dimension? = null

    /**
     * Margins around components
     */
    private val margins : Insets = Insets(0, 0, 0, 0)

    /**
     * Last minimum cell X computed by [.layoutContainer]
     */
    private var minimumX = 0

    /**
     * Last minimum cell Y computed by [.layoutContainer]
     */
    private var minimumY = 0

    constructor(margin : Int, tableAspect : Boolean = false) : this(tableAspect)
    {
        val inset = max(0, margin)
        this.setMargins(inset, inset, inset, inset)
    }

    constructor(top : Int, left : Int, bottom : Int, right : Int, tableAspect : Boolean = false) : this(tableAspect)
    {
        this.setMargins(max(0, top), max(0, left), max(0, bottom), max(0, right))
    }

    /**
     * Let the number of cells and the minimums computed by the components and their constraints
     */
    fun dynamicStructure()
    {
        this.fixedNumberOfCells = null
    }

    /**
     * Force the number of cells.<br></br>
     * After this the minimum cell X and Y will be 0
     *
     * @param numberOfColumns Number of columns
     * @param numberOfRows   Number of rows
     */
    fun fixStructure(numberOfColumns : Int, numberOfRows : Int)
    {
        this.fixedNumberOfCells = Dimension(max(1, numberOfColumns), max(1, numberOfRows))
    }

    /**
     * Margin around components
     *
     * @return Margin around components
     */
    fun getMargins() : Insets
    {
        return Insets(this.margins.top, this.margins.left, this.margins.bottom, this.margins.right)
    }

    /**
     * Modify margins around components
     *
     * @param margins New margins
     */
    fun setMargins(margins : Insets)
    {
        this.margins.top = max(0, margins.top)
        this.margins.left = max(0, margins.left)
        this.margins.bottom = max(0, margins.bottom)
        this.margins.right = max(0, margins.right)
    }

    /**
     * Modify margins around component
     *
     * @param top    Margin to add on top
     * @param left   Margin to add on left
     * @param bottom Margin to add on bottom
     * @param right  Margin to add on right
     */
    fun setMargins(top : Int, left : Int, bottom : Int, right : Int)
    {
        this.margins.top = max(0, top)
        this.margins.left = max(0, left)
        this.margins.bottom = max(0, bottom)
        this.margins.right = max(0, right)
    }

    override fun addLayoutComponent(component : Component, constraints : Any)
    {
        if (constraints !is TableLayoutConstraint)
        {
            throw IllegalArgumentException(
                "Constraints must be a ${TableLayoutConstraint::class.java.name} not a ${constraints.javaClass.name}")
        }

        if (this.tableAspect && component is JComponent)
        {
            component.addLineBorder()
        }

        this.components[component] = constraints
    }

    override fun addLayoutComponent(name : String, component : Component) = Unit

    override fun removeLayoutComponent(component : Component)
    {
        this.components.remove(component)
    }

    override fun preferredLayoutSize(parent : Container) : Dimension =
        this.computeSize(parent, ::computePreferredDimension)

    override fun minimumLayoutSize(parent : Container) : Dimension =
        this.computeSize(parent, ::computeMinimumDimension)

    override fun maximumLayoutSize(parent : Container) : Dimension =
        this.computeSize(parent, ::computeMaximumDimension)

    override fun layoutContainer(parent : Container)
    {
        val parentSize : Dimension = parent.size

        val insets : Insets = parent.insets
        parentSize.width = parentSize.width - insets.right - insets.right
        parentSize.height = parentSize.height - insets.top - insets.bottom

        synchronized(parent.treeLock) {
            var minX = Int.MAX_VALUE
            var maxX = Int.MIN_VALUE
            var minY = Int.MAX_VALUE
            var maxY = Int.MIN_VALUE
            var constraints : TableLayoutConstraint?

            if (this.fixedNumberOfCells != null)
            {
                minX = 0
                minY = 0
                maxX = this.fixedNumberOfCells !!.width
                maxY = this.fixedNumberOfCells !!.height
            }
            else
            {
                for (component in parent.iterator())
                {
                    constraints = this.components[component]

                    if (component.isVisible && constraints != null)
                    {
                        minX = min(minX, constraints.x)
                        maxX = max(maxX, constraints.x + constraints.numberCellWidth)
                        minY = min(minY, constraints.y)
                        maxY = max(maxY, constraints.y + constraints.numberCellHeight)
                    }
                }
            }

            this.minimumX = minX
            this.minimumY = minY

            if (minX < maxX && minY < maxY)
            {
                this.cellWidth = parentSize.width / (maxX - minX)
                this.cellHeight = parentSize.height / (maxY - minY)

                for (component in parent.iterator())
                {
                    constraints = this.components[component]

                    if (component.isVisible && constraints != null)
                    {
                        component.setLocation(this.margins.left + (constraints.x - minX) * this.cellWidth,
                                              this.margins.top + (constraints.y - minY) * this.cellHeight)
                        component.setSize(
                            constraints.numberCellWidth * this.cellWidth - this.margins.left - this.margins.right,
                            constraints.numberCellHeight * this.cellHeight - this.margins.top - this.margins.bottom)
                    }
                }
            }
        }
    }

    override fun getLayoutAlignmentX(target : Container) : Float = 0f

    override fun getLayoutAlignmentY(target : Container) : Float = 0f

    override fun invalidateLayout(target : Container) = Unit

    /**
     * Initialize the size computing
     */
    private fun initializeComputingSizes()
    {
        for (constraints in this.components.values)
        {
            constraints.currentWidth = 0
            constraints.currentHeight = 0
        }
    }

    /**
     * Compute the size with current collected information
     *
     * @return Computed size
     */
    private fun computeSize() : Dimension
    {
        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var minY = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE
        var cellWidth = 1
        var cellHeight = 1

        if (this.fixedNumberOfCells != null)
        {
            minX = 0
            minY = 0
            maxX = this.fixedNumberOfCells !!.width
            maxY = this.fixedNumberOfCells !!.height
        }

        for (constraints in this.components.values)
        {
            if (constraints.currentWidth > 0 && constraints.currentHeight > 0)
            {
                minX = min(minX, constraints.x)
                maxX = max(maxX, constraints.x + constraints.numberCellWidth)
                minY = min(minY, constraints.y)
                maxY = max(maxY, constraints.y + constraints.numberCellHeight)
                cellWidth = max(cellWidth, constraints.currentWidth / constraints.numberCellWidth)
                cellHeight = max(cellHeight, constraints.currentHeight / constraints.numberCellHeight)
            }
        }

        if (minX >= maxX || minY >= maxY)
        {
            return Dimension(1, 1)
        }

        if (this.fixedNumberOfCells != null)
        {
            minX = 0
            minY = 0
            maxX = this.fixedNumberOfCells !!.width
            maxY = this.fixedNumberOfCells !!.height
        }

        return Dimension((maxX - minX) * (this.margins.left + cellWidth + this.margins.right),
                         (maxY - minY) * (this.margins.top + cellHeight + this.margins.bottom))
    }

    private fun computeSize(parent : Container, componentSize : (Component) -> Dimension) : Dimension
    {
        this.initializeComputingSizes()

        var dimension : Dimension
        var constraints : TableLayoutConstraint?

        for (component in parent.iterator())
        {
            constraints = this.components[component]

            if (component.isVisible && constraints != null)
            {
                dimension = componentSize(component)
                constraints.currentWidth = dimension.width
                constraints.currentHeight = dimension.height
            }
        }

        return this.computeSize()
    }
}
