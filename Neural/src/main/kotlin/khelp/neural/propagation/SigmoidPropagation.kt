package khelp.neural.propagation

import kotlin.math.exp

object SigmoidPropagation : Propagation
{
    override fun propagation(t : Double) : Double =
        1.0 / (1.0 + exp(- t))

    override fun retroPropagation(t : Double) : Double
    {
        // For retro-propagation we take the derivative of sigmoid
        val sigmoid = this.propagation(t)
        return sigmoid * (1.0 - sigmoid)
    }
}