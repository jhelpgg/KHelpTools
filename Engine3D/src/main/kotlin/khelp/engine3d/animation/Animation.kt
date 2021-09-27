package khelp.engine3d.animation

import khelp.thread.delay
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData

abstract class Animation
{
    private var startTime : Long = 0L
    internal var playing = false
        private set
    private val stateObservableData = ObservableData<AnimationState>(AnimationState.IDLE)
    val stateObservable : Observable<AnimationState> = this.stateObservableData.observable

    protected open fun started()
    {
    }

    protected abstract fun animate(time : Long) : Boolean

    internal fun start()
    {
        this.startTime = System.currentTimeMillis()
        this.started()
        this.animate()
        this.stateObservableData.value(AnimationState.PLAYING)
    }

    internal fun animate()
    {
        this.playing = this.animate(System.currentTimeMillis() - this.startTime)

        if(!this.playing)
        {
            this.stateObservableData.value(AnimationState.IDLE)
        }
    }
}
