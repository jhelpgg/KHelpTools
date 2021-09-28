package khelp.engine3d.animation

import khelp.thread.TaskContext
import java.util.concurrent.atomic.AtomicBoolean

class AnimationTask(private val taskContext : TaskContext = TaskContext.INDEPENDENT,
                    private val task : () -> Unit) : Animation()
{
    private val played = AtomicBoolean(false)

    override fun started()
    {
        this.played.set(false)
    }

    override fun animate(time : Long) : Boolean
    {
        if (this.played.compareAndSet(false, true))
        {
            this.taskContext.parallel(this.task)
        }

        return false
    }
}