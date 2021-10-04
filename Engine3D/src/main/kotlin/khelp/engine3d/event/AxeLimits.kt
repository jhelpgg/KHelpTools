package khelp.engine3d.event

internal class AxeLimits(initialValue : Float)
{
    private val negativeDetection : Float
    private val positiveDetection : Float

    init
    {
        when
        {
            initialValue < - 0.25f ->
            {
                this.negativeDetection = - 2f
                this.positiveDetection = 0.5f
            }
            initialValue > 0.25f   ->
            {
                this.negativeDetection = - 0.5f
                this.positiveDetection = 2f
            }
            else                   ->
            {
                this.negativeDetection = - 0.25f
                this.positiveDetection = 0.25f
            }
        }
    }

    fun way(value : Float) : AxeWay =
        when
        {
            value < this.negativeDetection -> AxeWay.NEGATIVE
            value > this.positiveDetection -> AxeWay.POSITIVE
            else                           -> AxeWay.NEUTRAL
        }
}