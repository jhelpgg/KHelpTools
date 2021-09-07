package khelp.thread.flow

import khelp.thread.TaskContext

fun <T1 : Any, T2 : Any, R> flowCombiner(flow1 : Flow<T1>, flow2 : Flow<T2>, combiner : (T1, T2) -> R) : Flow<R> =
    FlowJoin<T1, T2, R>(flow1, flow2, TaskContext.INDEPENDENT, combiner).flow


fun <T1 : Any, T2 : Any, R> flowCombiner(flow1 : Flow<T1>, flow2 : Flow<T2>,
                                         taskContext : TaskContext, combiner : (T1, T2) -> R) : Flow<R> =
    FlowJoin<T1, T2, R>(flow1, flow2, taskContext, combiner).flow
