package khelp.grammar

import khelp.utilities.extensions.bounds
import kotlin.math.min

class StringPositionReader(private val string: String)
{
    private val length = this.string.length
    var currentIndex = 0
        private set
    val endReached: Boolean get() = this.currentIndex >= this.length

    fun stepForward(step: Int)
    {
        this.currentIndex = min(this.currentIndex + step, this.length)
    }

    fun setPosition(index: Int)
    {
        this.currentIndex = index.bounds(0, this.length)
    }

    fun textRelativePart(length: Int): String
    {
        val maximum = min(this.currentIndex + length, this.length)

        return if (this.currentIndex >= maximum)
        {
            ""
        }
        else
        {
            this.string.substring(this.currentIndex, maximum)
        }
    }

    fun currentText(): String =
        if (this.currentIndex >= this.length)
        {
            ""
        }
        else
        {
            this.string.substring(this.currentIndex)
        }
}