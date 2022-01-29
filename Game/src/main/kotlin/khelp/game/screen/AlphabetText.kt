package khelp.game.screen

import khelp.game.resources.AlphabetImage
import khelp.game.resources.CHARACTER_SIZE
import java.awt.Graphics2D
import java.util.Stack
import kotlin.math.max
import kotlin.math.min

class AlphabetText(width : Int, height : Int)
{
    companion object
    {
        private fun numberOfCharacter(text : String) : Int
        {
            var count = 0

            for (character in text)
            {
                if (character != '[' && character != ']' && character != '<' && character != '>')
                {
                    count ++
                }
            }

            return count
        }
    }

    val width : Int = max(8 * CHARACTER_SIZE, width)
    val height : Int = max(CHARACTER_SIZE, height)
    private val numberCharacterHorizontal = this.width / CHARACTER_SIZE
    private val numberCharacterVertical = this.height / CHARACTER_SIZE
    private val marginLeft = (this.width - this.numberCharacterHorizontal * CHARACTER_SIZE) / 2
    private val marginTop = (this.height - this.numberCharacterVertical * CHARACTER_SIZE) / 2
    private var y = 0
    private var maxY = 0
    private val lines = ArrayList<String>()

    val hasNext : Boolean get() = this.y < this.maxY
    val hasPrevious : Boolean get() = this.y > 0

    fun setText(text : String)
    {
        synchronized(this.lines)
        {
            this.y = 0
            this.lines.clear()

            for (line in text.trim()
                .replace("\t", "   ")
                .split('\n'))
            {
                var lineToAdd = line
                var numberCharacter = AlphabetText.numberOfCharacter(lineToAdd)
                var spaceIndex = lineToAdd.lastIndexOf(' ')

                while (numberCharacter > this.numberCharacterHorizontal && spaceIndex > 0)
                {
                    val lineFirstPart = lineToAdd.substring(0, spaceIndex)

                    if (AlphabetText.numberOfCharacter(lineFirstPart) <= this.numberCharacterHorizontal)
                    {
                        this.lines.add(lineFirstPart)
                        lineToAdd = lineToAdd.substring(spaceIndex + 1)
                        numberCharacter = AlphabetText.numberOfCharacter(lineToAdd)
                        spaceIndex = lineToAdd.lastIndexOf(' ')
                    }
                    else
                    {
                        spaceIndex = lineToAdd.lastIndexOf(' ', spaceIndex - 1)
                    }
                }

                this.lines.add(lineToAdd)
            }

            this.maxY = max(0, this.lines.size - this.numberCharacterVertical)
        }
    }

    fun next()
    {
        if (this.y < this.maxY)
        {
            this.y ++
        }
    }

    fun previous()
    {
        if (this.y > 0)
        {
            this.y --
        }
    }

    fun draw(x : Int, y : Int, graphics2D : Graphics2D)
    {
        synchronized(this.lines)
        {
            val stackAlphabet = Stack<AlphabetImage>()
            stackAlphabet.push(AlphabetImage.WOOD)
            val limit = this.y + min(this.numberCharacterVertical, this.lines.size - 1 - this.y)
            var yy = y + this.marginTop

            for (index in this.y .. limit)
            {
                var xx = x + this.marginLeft

                for (character in this.lines[index])
                {
                    when (character)
                    {
                        '<'      -> stackAlphabet.push(AlphabetImage.SILVER)
                        '['      -> stackAlphabet.push(AlphabetImage.GOLD)
                        '>', ']' ->
                            if (stackAlphabet.size > 1)
                            {
                                stackAlphabet.pop()
                            }
                        ' '      -> xx += CHARACTER_SIZE
                        else     ->
                        {
                            val alphabet = stackAlphabet.peek()
                            alphabet.draw(character, xx, yy, graphics2D)
                            xx += CHARACTER_SIZE
                        }
                    }
                }

                yy += CHARACTER_SIZE
            }
        }
    }
}