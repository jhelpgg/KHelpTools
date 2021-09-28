package khelp.ui.game.interpolation

import khelp.ui.game.GameImage
import khelp.utilities.math.Percent
import kotlin.math.max

class GameImageInterpolation(start : GameImage, end : GameImage,
                             private val gameImageInterpolationType : GameImageInterpolationType)
{
    private val startPixels : IntArray
    private val endPixels : IntArray
    private val resultPixels : IntArray
    val width : Int
    val height : Int
    val result : GameImage

    init
    {
        this.width = max(start.width, end.width)
        this.height = max(start.height, end.height)
        this.startPixels = start.resize(this.width, this.height)
            .grabPixels()
        this.endPixels = end.resize(this.width, this.height)
            .grabPixels()
        val size = this.width * this.height
        this.resultPixels = IntArray(size)
        System.arraycopy(this.startPixels, 0, this.resultPixels, 0, size)
        this.result = GameImage(this.width, this.height)
        this.result.putPixels(0, 0, this.width, this.height, this.resultPixels)
    }

    fun percent(percent : Percent)
    {
        this.gameImageInterpolationType.interpolate(this.startPixels, this.endPixels, this.resultPixels,
                                                    percent,
                                                    this.width, this.height)
        this.result.putPixels(0, 0, this.width, this.height, this.resultPixels)
    }
}
