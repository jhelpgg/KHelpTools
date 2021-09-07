package khelp.thread.observable

import khelp.thread.TaskContext
import khelp.thread.future.FutureResult

fun <T1 : Any, T2 : Any, R : Any> observableCombiner(observable1 : Observable<T1>, observable2 : Observable<T2>,
                                                     taskContext : TaskContext, combiner : (T1, T2) -> R)
        : FutureResult<Observable<R>> =
    ObservableJoin<T1, T2, R>(observable1, observable2, taskContext, combiner).observableFuture
