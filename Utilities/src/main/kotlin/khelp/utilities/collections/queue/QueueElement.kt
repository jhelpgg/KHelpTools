package khelp.utilities.collections.queue

internal class QueueElement<T>(val element: T, var next: QueueElement<T>? = null)
