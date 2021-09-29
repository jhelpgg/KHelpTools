package khelp.engine3d

import khelp.engine3d.animation.AnimationManager
import khelp.engine3d.event.ActionCode
import khelp.engine3d.render.WHITE
import khelp.engine3d.render.window3DFull
import khelp.thread.TaskContext

fun main()
{
    window3DFull("Test") {
        scene.backgroundColor = WHITE
        scene.root {
            val robot = robot("Robot") {
                z = - 10f
                val sword = sword("Sword") {
                    positionForBack()
                }
                putOnBack(sword)
            }

            AnimationManager.addAnimation("walk", robot.run(1024L, 16))

            actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT) { list ->
                for (actionCode in list)
                {
                    when (actionCode)
                    {
                        ActionCode.ACTION_BUTTON_1 -> AnimationManager.play("walk")
                        ActionCode.ACTION_BUTTON_2 -> robot.angleY += 1f
                        ActionCode.ACTION_UP       -> robot.y += 0.01f
                        ActionCode.ACTION_DOWN     -> robot.y -= 0.01f
                        ActionCode.ACTION_LEFT     -> robot.x -= 0.01f
                        ActionCode.ACTION_RIGHT    -> robot.x += 0.01f
                        else                       -> Unit
                    }
                }
            }
        }
    }
}
