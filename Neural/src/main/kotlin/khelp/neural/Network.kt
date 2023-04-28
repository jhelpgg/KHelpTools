package khelp.neural

import khelp.neural.propagation.Propagation
import khelp.neural.propagation.SigmoidPropagation
import khelp.utilities.argumentCheck

class Network(val numberEntry : Int, val numberHidden : Int, val numberExit : Int,
              propagation : Propagation = SigmoidPropagation)
{
    private val entryHiddenMatrix : Matrix
    private val hiddenExitMatrix : Matrix
    private val hidden : Vector
    private val forward = propagation::propagation
    private val backward = propagation::retroPropagation

    init
    {
        argumentCheck(this.numberEntry > 0) { "Number of entry must be at least 1, not ${this.numberEntry}" }
        argumentCheck(this.numberHidden > 0) { "Number of hidden must be at least 1, not ${this.numberHidden}" }
        argumentCheck(this.numberExit > 0) { "Number of exit must be at least 1, not ${this.numberExit}" }

        this.entryHiddenMatrix = Matrix(this.numberEntry, this.numberHidden)
        this.hiddenExitMatrix = Matrix(this.numberHidden, this.numberExit)
        this.hidden = Vector(this.numberHidden)
    }

    fun predict(entries : Vector, exit : Vector = Vector(this.numberExit)) : Vector
    {
        argumentCheck(
            entries.size == this.numberEntry) { "Entries number ${entries.size} not corresponds to network number of entries : ${this.numberEntry}" }
        argumentCheck(
            exit.size == this.numberExit) { "Exit number ${exit.size} not corresponds to network number of exit : ${this.numberExit}" }

        this.entryHiddenMatrix.apply(entries, this.hidden, this.forward)
        this.hiddenExitMatrix.apply(this.hidden, exit, this.forward)
        return exit
    }

    fun train(entries : Vector, expected : Vector) : Double
    {
        //                  -1   m
        // (Cost) LogLoss = ___ S   (y  LOG(a  )  + ( 1 - y ) LOG(1 - a  )
        //                   m  i=1   i      i             i           i

        argumentCheck(
            entries.size == this.numberEntry) { "Entries number ${entries.size} not corresponds to network number of entries : ${this.numberEntry}" }
        argumentCheck(
            expected.size == this.numberExit) { "Expected number ${expected.size} not corresponds to network number of exit : ${this.numberExit}" }

        val result = this.predict(entries)
        val errorHiddenExit = expected - result

        var distance = 0.0

        for (value in errorHiddenExit.vector)
        {
            distance += value
        }

        distance /= this.numberExit

        errorHiddenExit(this.backward)
        val errorEntryHidden = this.hiddenExitMatrix.transpose().apply(errorHiddenExit, function = this.backward)

        for(entryIndex in 0 until this.numberHidden) {
            var index = entryIndex
            val entryFactor = errorEntryHidden.vector[entryIndex]

            for(y in 0 until this.numberExit) {
                this.entryHiddenMatrix.matrix[index] += entryFactor
                index += this.numberEntry
            }
        }

        for(hiddenIndex in 0 until  this.numberEntry) {
            var index = hiddenIndex
            val hiddenFactor = errorHiddenExit.vector[hiddenIndex] * this.hidden.vector[hiddenIndex]

            for(y in 0 until this.numberHidden) {
                this.hiddenExitMatrix.matrix[index] += hiddenFactor
                index += this.numberHidden
            }
        }

        return distance
    }
}