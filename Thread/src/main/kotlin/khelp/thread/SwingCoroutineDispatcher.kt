package khelp.thread

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import javax.swing.SwingUtilities
import kotlin.coroutines.CoroutineContext

object SwingCoroutineDispatcher : CoroutineDispatcher()
{
    override fun dispatch(context : CoroutineContext, block : Runnable)
    {
        SwingUtilities.invokeLater(block)
    }
}