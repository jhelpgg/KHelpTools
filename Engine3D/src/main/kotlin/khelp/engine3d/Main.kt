package khelp.engine3d

import khelp.engine3d.animation.AnimationManager
import khelp.engine3d.event.ActionCode
import khelp.engine3d.render.WHITE
import khelp.engine3d.render.window3D
import khelp.engine3d.resource.Sounds
import khelp.thread.TaskContext
import java.util.concurrent.atomic.AtomicBoolean

fun main()
{
    val enqueued = AtomicBoolean(false)
    val running = AtomicBoolean(false)
    window3D(800, 600, "Test") {
        val soundSource = soundManager.createSource()

        scene.backgroundColor = WHITE
        scene.root {
            val robot = robot("Robot") {
                z = - 10f
                val sword = sword("Sword") {
                    positionForBack()
                }
                putOnBack(sword)
            }
            soundSource.link(robot)

            AnimationManager.animationList("run") {
                addAnimation(robot.run(1024L, 16))
                animationTask { running.set(false) }
            }

            actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT) { list ->
                for (actionCode in list)
                {
                    when (actionCode)
                    {
                        ActionCode.ACTION_BUTTON_1 ->
                            if (running.compareAndSet(false, true))
                            {
                                AnimationManager.play("run")
                            }
                        ActionCode.ACTION_BUTTON_2 -> robot.angleY += 1f
                        ActionCode.ACTION_BUTTON_3 ->
                        {
                            if (enqueued.compareAndSet(false, true))
                            {
                                soundSource.enqueue(Sounds.ALYA.sound)
                                soundSource.enqueue(Sounds.KUMA.sound)
                                soundSource.enqueue(Sounds.SUCCEED.sound)
                            }
                        }
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
