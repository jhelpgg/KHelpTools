package khelp.thread.extensions

import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.thread.observable.Observable

fun <T : Any, R : Any> Observable<T>.doWhen(condition : (T) -> Boolean,
                                            taskContext : TaskContext = TaskContext.INDEPENDENT,
                                            action : (T) -> R)
        : FutureResult<R>
{
    val promise = Promise<R>()
    val observer = this.observedBy(taskContext) { value ->
        if (condition(value))
        {
            try
            {
                promise.result(action(value))
            }
            catch (exception : Exception)
            {
                promise.fail(exception)
            }
        }
    }

    promise.cancelAction { observer.stopObserve() }
    promise.futureResult.then { observer.stopObserve() }
    return promise.futureResult
}
