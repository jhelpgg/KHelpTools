package khelp.engine3d.render.complex

import fr.jhelp.utilities.random
import khelp.engine3d.animation.Animation
import khelp.engine3d.animation.AnimationList
import khelp.engine3d.animation.NodePosition
import khelp.engine3d.render.Color4f
import khelp.engine3d.render.GRAY
import khelp.engine3d.render.Node
import khelp.engine3d.render.Texture
import khelp.engine3d.render.prebuilt.Box
import khelp.engine3d.render.prebuilt.CrossUV
import khelp.engine3d.resource.Textures
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.utilities.extensions.bounds
import kotlin.math.max

/**
 * Create a dice
 * @param value Initial value: 1, 2, 3, 4, 5 or 6
 * @throws IllegalArgumentException If value not 1, 2, 3, 4, 5 or 6
 */
class Dice(name : String, value : Int = random(1, 6)) : Node(name)
{
    companion object
    {
        private val positions = arrayOf(NodePosition(), // 1: Face
                                        NodePosition(angleY = - 90f), // 2: Right
                                        NodePosition(angleX = - 90f), // 3: Bottom
                                        NodePosition(angleX = 90f), // 4: Top
                                        NodePosition(angleY = 90f), // 5: Left
                                        NodePosition(angleY = 180f)) // 6: Back
    }

    private val valueObservableData = ObservableData<Int>(value)
    val valueObservable : Observable<Int> = this.valueObservableData.observable

    /**Box draw the dice*/
    private val dice = Box("${name}_dice", CrossUV())

    init
    {
        if (value < 1 || value > 6)
        {
            throw IllegalArgumentException("value must be: 1, 2, 3, 4, 5, or 6. Not $value")
        }

        this.dice.material.textureDiffuse = Texture(Textures.DICE.image)
        val position = Dice.positions[value - 1]
        this.dice.angleX = position.angleX
        this.dice.angleY = position.angleY
        this.dice.angleZ = position.angleZ
        this.addChild(this.dice)
    }

    /**
     * Create animation tha change the dice value
     * @param value New dice value: 1, 2, 3, 4, 5 or 6
     * @param timeMilliseconds Animation duration
     * @return Created animation
     * @throws IllegalArgumentException If value not 1, 2, 3, 4, 5 or 6
     */
    fun value(value : Int, timeMilliseconds : Long = 1L) : Animation
    {
        val valueLimited = value.bounds(1, 6)
        val animation = AnimationList()
        val position = Dice.positions[valueLimited - 1]
        animation.animationNodePositionElement(this.dice) {
            add(max(1L, timeMilliseconds)) {
                x = position.x
                y = position.y
                z = position.z

                angleX = position.angleX
                angleY = position.angleY
                angleZ = position.angleZ

                scaleX = position.scaleX
                scaleY = position.scaleY
                scaleZ = position.scaleZ
            }
        }
        animation.animationTask { this.valueObservableData.value(valueLimited) }
        return animation
    }

    /**
     * Create animation tha roll the dice
     * @return Created animation
     */
    fun roll() : Animation
    {
        var frame = 1L
        val time = random(12, 25)
        var lastValue = this.valueObservableData.value()
        var value = lastValue
        val animation = AnimationList()
        animation.animationNodePositionElement(this.dice) {
            for (index in 0 .. time)
            {
                do
                {
                    value = random(1, 6)
                }
                while (value == lastValue)

                lastValue = value
                val position = Dice.positions[value - 1]
                add(frame * 32L) {
                    x = position.x
                    y = position.y
                    z = position.z

                    angleX = position.angleX
                    angleY = position.angleY
                    angleZ = position.angleZ

                    scaleX = position.scaleX
                    scaleY = position.scaleY
                    scaleZ = position.scaleZ
                }

                frame += index + 1
            }
        }
        animation.animationTask { this.valueObservableData.value(value) }
        return animation
    }

    fun color(color : Color4f = GRAY)
    {
        this.dice.material.colorDiffuse = color
    }
}