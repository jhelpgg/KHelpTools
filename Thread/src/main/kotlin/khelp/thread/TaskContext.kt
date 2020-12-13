package khelp.thread

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob

enum class TaskContext(internal val scope: CoroutineScope, internal val dispatcher: CoroutineDispatcher)
{
    MAIN(MainScope(), Dispatchers.Main),
    IO(CoroutineScope(SupervisorJob() + Dispatchers.IO), Dispatchers.IO),
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