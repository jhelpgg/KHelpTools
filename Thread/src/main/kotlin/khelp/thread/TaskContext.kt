package khelp.thread

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob

/**
 * Task context for launch tasks in parallel
 */
enum class TaskContext(internal val scope: CoroutineScope, internal val dispatcher: CoroutineDispatcher)
{
    /**
     * Task will be played in main context. Use it with care for short operations that implies UI operation, like change a color of button, text in label, ....
     *
     * Make heavy operation in it will slow down application and make bad user experience.
     */
    MAIN(MainScope(), Dispatchers.Main),

    /**
     * Context reserved to IO operations.
     */
    IO(CoroutineScope(SupervisorJob() + Dispatchers.IO), Dispatchers.IO),

    /**
     * Context to do task in independent context.
     */
    INDEPENDENT(CoroutineScope(SupervisorJob() + Dispatchers.Default), Dispatchers.Default);

    fun <R : Any> parallel(task: () -> R) =
        parallel(this, task)

    fun <P, R : Any> parallel(parameter: P, task: (P) -> R) =
        parallel(this, parameter, task)

    fun <P1, P2, R : Any> parallel(parameter1: P1, parameter2: P2, task: (P1, P2) -> R) =
        parallel(this, parameter1, parameter2, task)

    fun <P1, P2, P3, R : Any> parallel(parameter1: P1, parameter2: P2, parameter3: P3, task: (P1, P2, P3) -> R) =
        parallel(this, parameter1, parameter2, parameter3, task)
}