package khelp.thread

import khelp.thread.future.FutureResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Launch a task in given task context type
 * @param context The context, by default the [TaskContext.INDEPENDENT] context
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be computed
 */
fun <R : Any> parallel(context: TaskContext = TaskContext.INDEPENDENT, task: () -> R): FutureResult<R>
{
    val futureResult = FutureResult<R>(context)

    val job = context.scope.launch {
        withContext(context.dispatcher)
        {
            futureResult.result(task.suspended()())
        }
    }

    futureResult.job(job)
    return futureResult
}

/**
 * Launch a task in given task context type
 * @param context The context, by default the [TaskContext.INDEPENDENT] context
 * @param parameter Parameter gives to the task
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be computed
 */
fun <P, R : Any> parallel(context: TaskContext = TaskContext.INDEPENDENT,
                          parameter: P, task: (P) -> R): FutureResult<R>
{
    val futureResult = FutureResult<R>(context)

    val job = context.scope.launch {
        withContext(context.dispatcher)
        {
            futureResult.result(task.suspended()(parameter))
        }
    }

    futureResult.job(job)
    return futureResult
}

/**
 * Launch a task in given task context type
 * @param context The context, by default the [TaskContext.INDEPENDENT] context
 * @param parameter1 First parameter gives to the task
 * @param parameter2 Second parameter gives to the task
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be computed
 */
fun <P1, P2, R : Any> parallel(context: TaskContext = TaskContext.INDEPENDENT,
                               parameter1: P1, parameter2: P2, task: (P1, P2) -> R): FutureResult<R>
{
    val futureResult = FutureResult<R>(context)

    val job = context.scope.launch {
        withContext(context.dispatcher)
        {
            futureResult.result(task.suspended()(parameter1, parameter2))
        }
    }

    futureResult.job(job)
    return futureResult
}

/**
 * Launch a task in given task context type
 * @param context The context, by default the [TaskContext.INDEPENDENT] context
 * @param parameter1 First parameter gives to the task
 * @param parameter2 Second parameter gives to the task
 * @param parameter3 Third parameter gives to the task
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be computed
 */
fun <P1, P2, P3, R : Any> parallel(context: TaskContext = TaskContext.INDEPENDENT,
                                   parameter1: P1,
                                   parameter2: P2,
                                   parameter3: P3,
                                   task: (P1, P2, P3) -> R): FutureResult<R>
{
    val futureResult = FutureResult<R>(context)

    val job = context.scope.launch {
        withContext(context.dispatcher)
        {
            futureResult.result(task.suspended()(parameter1, parameter2, parameter3))
        }
    }

    futureResult.job(job)
    return futureResult
}

/**
 * Launch a task in a given delay in [TaskContext.INDEPENDENT] context
 * @param delayMilliseconds Delay before launch the task in milliseconds
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be compute. It also can be used to cancel the task launch before delay expires, useful for timeout implementation
 */
fun <R : Any> delay(delayMilliseconds: Int, task: () -> R): FutureResult<R> =
    delay(TaskContext.INDEPENDENT, delayMilliseconds, task)

/**
 * Launch a task in a given delay in given task context type
 * @param context The context, by default the [TaskContext.INDEPENDENT] context
 * @param delayMilliseconds Delay before launch the task in milliseconds
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be compute. It also can be used to cancel the task launch before delay expires, useful for timeout implementation
 */
fun <R : Any> delay(context: TaskContext, delayMilliseconds: Int, task: () -> R): FutureResult<R>
{
    val futureResult = FutureResult<R>(context)

    val job = context.scope.launch {
        withContext(context.dispatcher)
        {
            delay(delayMilliseconds.toLong())
            futureResult.result(task.suspended()())
        }
    }

    futureResult.job(job)
    return futureResult
}

/**
 * Launch a task in a given delay in [TaskContext.INDEPENDENT] context
 * @param delayMilliseconds Delay before launch the task in milliseconds
 * @param parameter Parameter gives to the task
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be compute. It also can be used to cancel the task launch before delay expires, useful for timeout implementation
 */
fun <P, R : Any> delay(delayMilliseconds: Int, parameter: P, task: (P) -> R) =
    delay(TaskContext.INDEPENDENT, delayMilliseconds, parameter, task)

/**
 * Launch a task in a given delay in given task context type
 * @param context The context, by default the [TaskContext.INDEPENDENT] context
 * @param delayMilliseconds Delay before launch the task in milliseconds
 * @param parameter Parameter gives to the task
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be compute. It also can be used to cancel the task launch before delay expires, useful for timeout implementation
 */
fun <P, R : Any> delay(context: TaskContext, delayMilliseconds: Int, parameter: P, task: (P) -> R): FutureResult<R>
{
    val futureResult = FutureResult<R>(context)

    val job = context.scope.launch {
        withContext(context.dispatcher)
        {
            delay(delayMilliseconds.toLong())
            futureResult.result(task.suspended()(parameter))
        }
    }

    futureResult.job(job)
    return futureResult
}

/**
 * Launch a task in a given delay in [TaskContext.INDEPENDENT] context
 * @param delayMilliseconds Delay before launch the task in milliseconds
 * @param parameter1 First parameter gives to the task
 * @param parameter2 Second parameter gives to the task
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be compute. It also can be used to cancel the task launch before delay expires, useful for timeout implementation
 */
fun <P1, P2, R : Any> delay(delayMilliseconds: Int,
                            parameter1: P1, parameter2: P2, task: (P1, P2) -> R) =
    delay(TaskContext.INDEPENDENT, delayMilliseconds, parameter1, parameter2, task)

/**
 * Launch a task in a given delay in given task context type
 * @param context The context, by default the [TaskContext.INDEPENDENT] context
 * @param delayMilliseconds Delay before launch the task in milliseconds
 * @param parameter1 First parameter gives to the task
 * @param parameter2 Second parameter gives to the task
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be compute. It also can be used to cancel the task launch before delay expires, useful for timeout implementation
 */
fun <P1, P2, R : Any> delay(context: TaskContext, delayMilliseconds: Int,
                            parameter1: P1, parameter2: P2, task: (P1, P2) -> R): FutureResult<R>
{
    val futureResult = FutureResult<R>(context)

    val job = context.scope.launch {
        withContext(context.dispatcher)
        {
            delay(delayMilliseconds.toLong())
            futureResult.result(task.suspended()(parameter1, parameter2))
        }
    }

    futureResult.job(job)
    return futureResult
}

/**
 * Launch a task in a given delay in [TaskContext.INDEPENDENT] context
 * @param delayMilliseconds Delay before launch the task in milliseconds
 * @param parameter1 First parameter gives to the task
 * @param parameter2 Second parameter gives to the task
 * @param parameter3 Third parameter gives to the task
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be compute. It also can be used to cancel the task launch before delay expires, useful for timeout implementation
 */
fun <P1, P2, P3, R : Any> delay(delayMilliseconds: Int,
                                parameter1: P1, parameter2: P2, parameter3: P3, task: (P1, P2, P3) -> R) =
    delay(khelp.thread.TaskContext.INDEPENDENT, delayMilliseconds, parameter1, parameter2, parameter3, task)

/**
 * Launch a task in a given delay in given task context type
 * @param context The context, by default the [TaskContext.INDEPENDENT] context
 * @param delayMilliseconds Delay before launch the task in milliseconds
 * @param parameter1 First parameter gives to the task
 * @param parameter2 Second parameter gives to the task
 * @param parameter3 Third parameter gives to the task
 * @param task Task to do
 * @return [FutureResult] that link to the task execution. It will receive the task result as soon as it will be compute. It also can be used to cancel the task launch before delay expires, useful for timeout implementation
 */
fun <P1, P2, P3, R : Any> delay(context: TaskContext,
                                delayMilliseconds: Int,
                                parameter1: P1,
                                parameter2: P2,
                                parameter3: P3,
                                task: (P1, P2, P3) -> R): FutureResult<R>
{
    val futureResult = FutureResult<R>(context)

    val job = context.scope.launch {
        withContext(context.dispatcher)
        {
            delay(delayMilliseconds.toLong())
            futureResult.result(task.suspended()(parameter1, parameter2, parameter3))
        }
    }

    futureResult.job(job)
    return futureResult
}

// Automatic suspend tasks

private fun <R> (() -> R).suspended(): suspend () -> R =
    { this() }

private fun <P, R> ((P) -> R).suspended(): suspend (P) -> R =
    { p -> this(p) }

private fun <P1, P2, R> ((P1, P2) -> R).suspended(): suspend (P1, P2) -> R =
    { p1, p2 -> this(p1, p2) }

private fun <P1, P2, P3, R> ((P1, P2, P3) -> R).suspended(): suspend (P1, P2, P3) -> R =
    { p1, p2, p3 -> this(p1, p2, p3) }
