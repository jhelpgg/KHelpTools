package khelp.thread.future

import khelp.thread.Mutex
import khelp.thread.TaskContext

class Promise<R : Any>
{
    private val mutex = Mutex()
    private var cancellationReason = ""
    private var cancelAction : (String) -> Unit = {}
    var cancelled : Boolean = false
        private set
    val futureResult = FutureResult<R>(TaskContext.INDEPENDENT)

    init
    {
        this.futureResult.onCancel { cancellationException ->
            this.mutex {
                this.cancelled = true
                this.cancellationReason = cancellationException.toString()
                this.cancelAction(this.cancellationReason)
                this.cancelAction = {}
            }
        }
    }

    fun result(result : R)
    {
        this.futureResult.result(result)
    }

    fun fail(exception : Exception)
    {
        this.futureResult.completion(exception)
    }

    fun cancelAction(cancelAction : (String) -> Unit)
    {
        this.mutex {
            if (this.cancelled)
            {
                cancelAction(this.cancellationReason)
            }
            else if (this.futureResult.status() == FutureResultStatus.COMPUTING)
            {
                this.cancelAction = cancelAction
            }
        }
    }
}
