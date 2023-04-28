package khelp.match3.game

import khelp.utilities.math.random
import khelp.match3.CELL_SEPARATOR_MAIN_COLOR
import khelp.match3.CELL_SEPARATOR_MIDDLE_COLOR
import khelp.match3.FRAME_DURATION
import khelp.match3.NUMBER_GRID_CELL_HORIZONTAL
import khelp.match3.NUMBER_GRID_CELL_VERTICAL
import khelp.match3.model.Bomb
import khelp.match3.model.Cell
import khelp.match3.model.CellPosition
import khelp.match3.model.EmptyCell
import khelp.match3.model.Gem
import khelp.match3.model.GemType
import khelp.match3.model.HorizontalArrow
import khelp.match3.model.SameColor
import khelp.match3.model.VerticalArrow
import khelp.match3.ui.EventMouse
import khelp.match3.ui.EventMouseType
import khelp.match3.ui.MouseManager
import khelp.match3.ui.POINTS_FONT
import khelp.thread.TaskContext
import khelp.thread.delay
import khelp.thread.flow.Flow
import khelp.ui.extensions.drawText
import khelp.ui.game.GameImage
import khelp.utilities.collections.sortedArray
import khelp.utilities.provider.provided
import khelp.utilities.thread.atomic
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.util.Stack
import java.util.TreeSet
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min

object GameScreen
{
    private const val MINIMUM_NUMBER_DIFFERENT_GEM = 3
    private const val MAXIMUM_NUMBER_DIFFERENT_GEM = 16
    private const val GRID_WIDTH = 64 * NUMBER_GRID_CELL_HORIZONTAL + 5 * (NUMBER_GRID_CELL_HORIZONTAL + 1)
    private const val GRID_HEIGHT = 64 * NUMBER_GRID_CELL_VERTICAL + 5 * (NUMBER_GRID_CELL_VERTICAL + 1)
    const val CELL_SIZE = 64 + 5
    private const val FALLING_STEP = 23
    private const val GRID_X = 64
    private const val GRID_Y = (1024 - GRID_HEIGHT) / 2
    private const val OFFSET_X = GRID_X + CELL_SIZE / 2
    private const val OFFSET_Y = GRID_Y + CELL_SIZE
    private var frame = 0
    private val gameScreen by provided<GameImage>()
    private val mouseManager by provided<MouseManager>()
    private var mouseFlow : Flow<Unit>? = null
    private val shown = AtomicBoolean(false)
    private val grid = Array<Cell>(NUMBER_GRID_CELL_HORIZONTAL * NUMBER_GRID_CELL_VERTICAL) { EmptyCell }
    private var numberOfDifferentGem = MINIMUM_NUMBER_DIFFERENT_GEM
    private val possibleGems = ArrayList<Gem>()
    private var gameScreenState by atomic(GameScreenState.FALLING_CELLS)
    private val fallingCells = sortedArray<FallingCell>()
    private val explosions = sortedArray<ExplodeCell>()
    private var afterExplosion by atomic(GameScreenState.WAITING_USER_ACTION)
    private var pointsPrint by atomic(0)
    private var points by atomic(0)
    private var goal = 1024

    fun showScreen()
    {
        this.mouseFlow = this.mouseManager.eventMouseFlow.then(TaskContext.INDEPENDENT, this::eventMouse)
        this.gameScreen.clear(Color.WHITE)
        this.newGame()

        if (this.shown.compareAndSet(false, true))
        {
            delay(FRAME_DURATION, this::refresh)
        }
    }

    fun hideScreen()
    {
        this.mouseFlow?.cancel()
        this.shown.set(false)
    }

    private fun eventMouse(eventMouse : EventMouse)
    {
        // TODO()

        if (this.gameScreenState == GameScreenState.WAITING_USER_ACTION && eventMouse.eventMouseType == EventMouseType.CLiCK)
        {
            val x = (eventMouse.x - GRID_X) / CELL_SIZE
            val y = (eventMouse.y - GRID_Y) / CELL_SIZE
            var explode = false
            val toExplode = Stack<Pair<Cell, CellPosition>>()

            if (x in 0 until NUMBER_GRID_CELL_HORIZONTAL
                && y in 0 until NUMBER_GRID_CELL_VERTICAL)
            {
                val index = x + y * NUMBER_GRID_CELL_HORIZONTAL
                val cell = this.grid[index]

                if (cell != EmptyCell && cell !is Gem)
                {
                    explode = true
                    toExplode.push(Pair(cell, CellPosition(x, y)))
                }
            }

            if (explode)
            {
                while (toExplode.isNotEmpty())
                {
                    val (cell, position) = toExplode.pop()
                    val (xx, yy) = position
                    val index = xx + yy * NUMBER_GRID_CELL_HORIZONTAL
                    this.grid[index] = EmptyCell
                    this.explosions += ExplodeCell(xx, yy, EmptyCell)
                    this.points ++

                    when (cell)
                    {
                        HorizontalArrow -> this.explodeHorizontal(yy, toExplode)
                        VerticalArrow   -> this.explodeVertical(xx, toExplode)
                        Bomb            -> this.explodeBomb(xx, yy, toExplode)
                        SameColor       -> this.explodeBook()
                        else            -> Unit
                    }
                }

                this.gameScreenState = GameScreenState.EXPLODE_ANIMATION
            }
        }
    }

    private fun explodeHorizontal(y : Int, toExplode : Stack<Pair<Cell, CellPosition>>)
    {
        this.points += 2
        var indexLine = y * NUMBER_GRID_CELL_HORIZONTAL

        for (xx in 0 until NUMBER_GRID_CELL_HORIZONTAL)
        {
            val cellLook = this.grid[indexLine]

            if (cellLook != EmptyCell)
            {
                this.grid[indexLine] = EmptyCell
                this.explosions += ExplodeCell(xx, y, EmptyCell)
                this.points ++

                if (cellLook !is Gem)
                {
                    toExplode.push(Pair(cellLook, CellPosition(xx, y)))
                }
            }

            indexLine ++
        }
    }

    private fun explodeVertical(x : Int, toExplode : Stack<Pair<Cell, CellPosition>>)
    {
        this.points += 2
        var indexColumn = x

        for (yy in 0 until NUMBER_GRID_CELL_VERTICAL)
        {
            val cellLook = this.grid[indexColumn]

            if (cellLook != EmptyCell)
            {
                this.grid[indexColumn] = EmptyCell
                this.explosions += ExplodeCell(x, yy, EmptyCell)
                this.points ++

                if (cellLook !is Gem)
                {
                    toExplode.push(Pair(cellLook, CellPosition(x, yy)))
                }
            }

            indexColumn += NUMBER_GRID_CELL_HORIZONTAL
        }
    }

    private fun explodeBomb(x : Int, y : Int, toExplode : Stack<Pair<Cell, CellPosition>>)
    {
        this.points += 4
        val index = x + y * NUMBER_GRID_CELL_HORIZONTAL

        if (x > 0 && y > 0)
        {
            val indexLook = index - 1 - NUMBER_GRID_CELL_HORIZONTAL
            val cellLook = this.grid[indexLook]

            if (cellLook != EmptyCell)
            {
                this.grid[indexLook] = EmptyCell
                this.explosions += ExplodeCell(x - 1, y - 1, EmptyCell)
                this.points ++

                if (cellLook !is Gem)
                {
                    toExplode.push(Pair(cellLook, CellPosition(x - 1, y - 1)))
                }
            }
        }

        if (x > 0)
        {
            val indexLook = index - 1
            val cellLook = this.grid[indexLook]

            if (cellLook != EmptyCell)
            {
                this.grid[indexLook] = EmptyCell
                this.explosions += ExplodeCell(x - 1, y, EmptyCell)
                this.points ++

                if (cellLook !is Gem)
                {
                    toExplode.push(Pair(cellLook, CellPosition(x - 1, y)))
                }
            }
        }

        if (x > 0 && y < NUMBER_GRID_CELL_VERTICAL - 1)
        {
            val indexLook = index - 1 + NUMBER_GRID_CELL_HORIZONTAL
            val cellLook = this.grid[indexLook]

            if (cellLook != EmptyCell)
            {
                this.grid[indexLook] = EmptyCell
                this.explosions += ExplodeCell(x - 1, y + 1, EmptyCell)
                this.points ++

                if (cellLook !is Gem)
                {
                    toExplode.push(Pair(cellLook, CellPosition(x - 1, y + 1)))
                }
            }
        }

        //

        if (y > 0)
        {
            val indexLook = index - NUMBER_GRID_CELL_HORIZONTAL
            val cellLook = this.grid[indexLook]

            if (cellLook != EmptyCell)
            {
                this.grid[indexLook] = EmptyCell
                this.explosions += ExplodeCell(x, y - 1, EmptyCell)
                this.points ++

                if (cellLook !is Gem)
                {
                    toExplode.push(Pair(cellLook, CellPosition(x, y - 1)))
                }
            }
        }

        if (y < NUMBER_GRID_CELL_VERTICAL - 1)
        {
            val indexLook = index + NUMBER_GRID_CELL_HORIZONTAL
            val cellLook = this.grid[indexLook]

            if (cellLook != EmptyCell)
            {
                this.grid[indexLook] = EmptyCell
                this.explosions += ExplodeCell(x, y + 1, EmptyCell)
                this.points ++

                if (cellLook !is Gem)
                {
                    toExplode.push(Pair(cellLook, CellPosition(x, y + 1)))
                }
            }
        }

        //

        if (x < NUMBER_GRID_CELL_HORIZONTAL - 1 && y > 0)
        {
            val indexLook = index + 1 - NUMBER_GRID_CELL_HORIZONTAL
            val cellLook = this.grid[indexLook]

            if (cellLook != EmptyCell)
            {
                this.grid[indexLook] = EmptyCell
                this.explosions += ExplodeCell(x + 1, y - 1, EmptyCell)
                this.points ++

                if (cellLook !is Gem)
                {
                    toExplode.push(Pair(cellLook, CellPosition(x + 1, y - 1)))
                }
            }
        }

        if (x < NUMBER_GRID_CELL_HORIZONTAL - 1)
        {
            val indexLook = index + 1
            val cellLook = this.grid[indexLook]

            if (cellLook != EmptyCell)
            {
                this.grid[indexLook] = EmptyCell
                this.explosions += ExplodeCell(x + 1, y, EmptyCell)
                this.points ++

                if (cellLook !is Gem)
                {
                    toExplode.push(Pair(cellLook, CellPosition(x + 1, y)))
                }
            }
        }

        if (x < NUMBER_GRID_CELL_HORIZONTAL - 1 && y < NUMBER_GRID_CELL_VERTICAL - 1)
        {
            val indexLook = index + 1 + NUMBER_GRID_CELL_HORIZONTAL
            val cellLook = this.grid[indexLook]

            if (cellLook != EmptyCell)
            {
                this.grid[indexLook] = EmptyCell
                this.explosions += ExplodeCell(x + 1, y + 1, EmptyCell)
                this.points ++

                if (cellLook !is Gem)
                {
                    toExplode.push(Pair(cellLook, CellPosition(x + 1, y + 1)))
                }
            }
        }
    }

    private fun explodeBook(gemType : GemType = this.mostPresentGemType())
    {
        this.points += 8

        for ((index, cell) in this.grid.withIndex())
        {
            if (cell is Gem && cell.gemType == gemType)
            {
                this.grid[index] = EmptyCell
                this.explosions += ExplodeCell(index % NUMBER_GRID_CELL_HORIZONTAL,
                                               index / NUMBER_GRID_CELL_HORIZONTAL,
                                               EmptyCell)
                this.points ++
            }
        }
    }

    private fun mostPresentGemType() : GemType
    {
        var gemType = this.possibleGems.random().gemType
        val counters = HashMap<GemType, Int>()
        var maximum = 0

        for (cell in this.grid)
        {
            if (cell is Gem)
            {
                val cellGemType = cell.gemType
                val value = (counters[cellGemType] ?: 0) + 1
                counters[cellGemType] = value

                if (value > maximum)
                {
                    maximum = value
                    gemType = cellGemType
                }
            }
        }

        return gemType
    }

    fun canCloseNow() : Boolean
    {
        return true
    }

    private fun newGame()
    {
        this.fallingCells.clear()
        this.explosions.clear()
        this.grid.fill(EmptyCell)
        val gemTypes = TreeSet<GemType>()

        for (count in 0 until this.numberOfDifferentGem)
        {
            var gemType = random<GemType>()

            while (! gemTypes.add(gemType))
            {
                gemType = random<GemType>()
            }
        }

        synchronized(this.possibleGems)
        {
            this.possibleGems.clear()

            for (gemType in gemTypes)
            {
                this.possibleGems.add(Gem(gemType))
            }
        }

        this.gameScreenState = GameScreenState.FALLING_CELLS
    }

    private fun refresh()
    {
        this.gameScreen.draw { graphics2D ->
            this.drawBackground(graphics2D)
            this.drawGrid(graphics2D)
        }

        this.frame ++

        if (this.shown.get())
        {
            delay(FRAME_DURATION, this::refresh)
        }
    }

    private fun drawBackground(graphics2D : Graphics2D)
    {
        graphics2D.color = Color.WHITE
        graphics2D.fillRect(0, 0, this.gameScreen.width, this.gameScreen.height)
    }

    private fun drawGrid(graphics2D : Graphics2D)
    {
        graphics2D.color = CELL_SEPARATOR_MAIN_COLOR
        graphics2D.stroke = BasicStroke(3.0f)
        this.drawGridStructure(graphics2D)
        graphics2D.color = CELL_SEPARATOR_MIDDLE_COLOR
        graphics2D.stroke = BasicStroke(1.0f)
        this.drawGridStructure(graphics2D)

        var x = OFFSET_X
        var y = OFFSET_Y
        var lineCount = 0

        for (cell in this.grid)
        {
            cell.draw(x, y, this.frame, graphics2D)
            x += CELL_SIZE
            lineCount ++

            if (lineCount >= NUMBER_GRID_CELL_HORIZONTAL)
            {
                lineCount = 0
                x = OFFSET_X
                y += CELL_SIZE
            }
        }

        this.drawAnimations(graphics2D)

        graphics2D.color = Color.BLACK
        graphics2D.font = POINTS_FONT.font
        graphics2D.drawText(GRID_X + GRID_WIDTH + 32, 32, "${this.pointsPrint}")
        graphics2D.color = Color.RED
        graphics2D.drawText(GRID_X + GRID_WIDTH + 32, 64, "${this.goal}")

        if (this.pointsPrint >= this.goal)
        {
            this.goal += this.goal + this.goal / 2
            this.numberOfDifferentGem = min(this.numberOfDifferentGem + 1, MAXIMUM_NUMBER_DIFFERENT_GEM)

            while (this.possibleGems.size < this.numberOfDifferentGem)
            {
                var gemType = random<GemType>()

                while (this.possibleGems.any { gem -> gem.gemType == gemType })
                {
                    gemType = random<GemType>()
                }

                this.possibleGems.add(Gem(gemType))
            }

//            this.newGame()
        }

        if (this.pointsPrint < this.points)
        {
            this.pointsPrint += ((this.points - this.pointsPrint) shr 3) + 1
        }
    }

    private fun drawGridStructure(graphics2D : Graphics2D)
    {
        graphics2D.drawRoundRect(GRID_X, GRID_Y, GRID_WIDTH, GRID_HEIGHT, 8, 8)

        var x = GRID_X + CELL_SIZE

        for (time in 1 until NUMBER_GRID_CELL_HORIZONTAL)
        {
            graphics2D.drawLine(x, GRID_Y, x, GRID_Y + GRID_HEIGHT)
            x += CELL_SIZE
        }

        var y = GRID_Y + CELL_SIZE

        for (time in 1 until NUMBER_GRID_CELL_VERTICAL)
        {
            graphics2D.drawLine(GRID_X, y, GRID_X + GRID_WIDTH, y)
            y += CELL_SIZE
        }
    }

    private fun drawAnimations(graphics2D : Graphics2D)
    {
        when (this.gameScreenState)
        {
            GameScreenState.FALLING_CELLS     -> this.addGem(graphics2D)
            GameScreenState.EXPLODE_ANIMATION -> this.explosions(graphics2D)
            else                              -> Unit
        }
    }

    private fun addGem(graphics2D : Graphics2D)
    {
        for ((index, cell) in this.grid.withIndex())
        {
            if (cell == EmptyCell)
            {
                if (index < NUMBER_GRID_CELL_HORIZONTAL)
                {
                    val fallingCell = FallingCell(index, 0, this.possibleGems.random())

                    if (fallingCell !in this.fallingCells)
                    {
                        this.fallingCells += fallingCell
                    }
                }
                else if (this.grid[index - NUMBER_GRID_CELL_HORIZONTAL] != EmptyCell)
                {
                    val fallingCell = FallingCell(index % NUMBER_GRID_CELL_HORIZONTAL,
                                                  index / NUMBER_GRID_CELL_HORIZONTAL,
                                                  this.grid[index - NUMBER_GRID_CELL_HORIZONTAL])

                    if (fallingCell !in this.fallingCells)
                    {
                        this.fallingCells += fallingCell
                        this.grid[index - NUMBER_GRID_CELL_HORIZONTAL] = EmptyCell
                    }
                }
            }
        }

        if (this.fallingCells.empty)
        {
            this.afterExplosion = GameScreenState.WAITING_USER_ACTION
            this.gameScreenState = GameScreenState.EXPLODE_ANIMATION
            return
        }

        for (index in this.fallingCells.size - 1 downTo 0)
        {
            val fallingCell = this.fallingCells[index]
            fallingCell.cell.draw(fallingCell.xCell * CELL_SIZE + OFFSET_X,
                                  fallingCell.yCell * CELL_SIZE + OFFSET_Y - fallingCell.deltaY,
                                  this.frame,
                                  graphics2D)
            fallingCell.deltaY -= FALLING_STEP

            if (fallingCell.deltaY <= 0)
            {
                this.fallingCells.remove(index)
                this.grid[fallingCell.xCell + fallingCell.yCell * NUMBER_GRID_CELL_HORIZONTAL] = fallingCell.cell
            }
        }
    }

    private fun explosions(graphics2D : Graphics2D)
    {

        if (this.explosions.empty)
        {
            val alreadyUsed = BooleanArray(this.grid.size) { false }

            for ((index, cell) in this.grid.withIndex())
            {
                if (! alreadyUsed[index] && cell is Gem)
                {
                    alreadyUsed[index] = true
                    val cellX = index % NUMBER_GRID_CELL_HORIZONTAL
                    val cellY = index / NUMBER_GRID_CELL_HORIZONTAL
                    val connected = ArrayList<CellPosition>()
                    var cellPosition = CellPosition(cellX, cellY)
                    val stack = Stack<CellPosition>()
                    stack.push(cellPosition)

                    while (stack.isNotEmpty())
                    {
                        val (x, y) = stack.pop()
                        var indexLook = x - 1 + y * NUMBER_GRID_CELL_HORIZONTAL

                        if (x > 0 && this.grid[indexLook] == cell && ! alreadyUsed[indexLook])
                        {
                            alreadyUsed[indexLook] = true
                            cellPosition = CellPosition(x - 1, y)
                            connected.add(cellPosition)
                            stack.push(cellPosition)
                        }

                        indexLook = x + 1 + y * NUMBER_GRID_CELL_HORIZONTAL

                        if (x < NUMBER_GRID_CELL_HORIZONTAL - 1 && this.grid[indexLook] == cell && ! alreadyUsed[indexLook])
                        {
                            alreadyUsed[indexLook] = true
                            cellPosition = CellPosition(x + 1, y)
                            connected.add(cellPosition)
                            stack.push(cellPosition)
                        }

                        indexLook = x + (y - 1) * NUMBER_GRID_CELL_HORIZONTAL

                        if (y > 0 && this.grid[indexLook] == cell && ! alreadyUsed[indexLook])
                        {
                            alreadyUsed[indexLook] = true
                            cellPosition = CellPosition(x, y - 1)
                            connected.add(cellPosition)
                            stack.push(cellPosition)
                        }

                        indexLook = x + (y + 1) * NUMBER_GRID_CELL_HORIZONTAL

                        if (y < NUMBER_GRID_CELL_VERTICAL - 1 && this.grid[indexLook] == cell && ! alreadyUsed[indexLook])
                        {
                            alreadyUsed[indexLook] = true
                            cellPosition = CellPosition(x, y + 1)
                            connected.add(cellPosition)
                            stack.push(cellPosition)
                        }
                    }

                    if (connected.size >= 2)
                    {
                        val replacement = this.addPointsForSize(connected.size + 1)
                        this.explosions += ExplodeCell(cellX, cellY, replacement)
                        this.grid[index] = EmptyCell

                        for (position in connected)
                        {
                            this.explosions += ExplodeCell(position.x, position.y, EmptyCell)
                            this.grid[position.x + position.y * NUMBER_GRID_CELL_HORIZONTAL] = EmptyCell
                        }
                    }
                }
            }
        }

        if (this.explosions.empty)
        {
            this.gameScreenState = this.afterExplosion
            return
        }

        for (index in this.explosions.size - 1 downTo 0)
        {
            val explosion = this.explosions[index]
            val finished = ! explosion.explosion
                .drawExplosion(OFFSET_X + explosion.cellX * CELL_SIZE,
                               OFFSET_Y + explosion.cellY * CELL_SIZE,
                               graphics2D)

            if (finished)
            {
                this.explosions.remove(index)
                this.grid[explosion.cellX + explosion.cellY * NUMBER_GRID_CELL_HORIZONTAL] = explosion.replacedBy

                if (explosion.replacedBy == EmptyCell)
                {
                    this.afterExplosion = GameScreenState.FALLING_CELLS
                }
            }
        }
    }

    private fun addPointsForSize(size : Int) : Cell
    {
        this.points += ((size - 2) * (size - 1)) / 2

        return when
        {
            size <= 3 -> EmptyCell
            size == 4 -> if (random()) HorizontalArrow else VerticalArrow
            size == 5 -> Bomb
            else      -> SameColor
        }
    }
}
