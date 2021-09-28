package khelp.engine3d

import khelp.engine3d.animation.AnimationManager
import khelp.engine3d.event.ActionCode
import khelp.engine3d.render.LIGHT_GREEN
import khelp.engine3d.render.WHITE
import khelp.engine3d.render.complex.Dice
import khelp.engine3d.render.window3DFull
import khelp.thread.TaskContext
import khelp.utilities.log.debug
import org.lwjgl.glfw.GLFW

fun main()
{
    window3DFull("Test") {
        scene.backgroundColor = WHITE
        scene.root {
            dice("Dice") {
                z = 1f
                color(LIGHT_GREEN)
                valueObservable.observedBy(TaskContext.INDEPENDENT) { value -> debug("Dice value=$value") }
            }

            actionManager.associate(ActionCode.ACTION_BUTTON_1, GLFW.GLFW_KEY_SPACE)
            actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT) { list ->
                if (ActionCode.ACTION_BUTTON_1 in list)
                {
                    findById<Dice>("Dice")?.let { dice ->

                        AnimationManager.addAnimation("rollDice", dice.roll())
                        AnimationManager.play("rollDice")
                    }
                }
            }
        }
    }
}
