package khelp.engine3d.animation

import java.util.concurrent.atomic.AtomicInteger

class AnimationAtomicTasks(private vararg val tasks : () -> Unit) : Animation()
{
    private val index = AtomicInteger(0)


    override fun started()
    {
        this.index.get()
    }

    override fun animate(time : Long) : Boolean
    {
        val index = this.index.getAndIncrement()

        if (index < this.tasks.size)
        {
            this.tasks[index]()
            return true
        }

        return false
    }
}