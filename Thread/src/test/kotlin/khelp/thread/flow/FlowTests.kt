package khelp.thread.flow

import khelp.thread.Locker
import khelp.thread.TaskContext
import khelp.thread.delay
import khelp.utilities.extensions.int
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class FlowTests
{
    @Test
    fun thenCancel()
    {
        val string1 = AtomicReference("")
        val integer1 = AtomicInteger(0)
        val integer2 = AtomicInteger(0)
        val string2 = AtomicReference("")
        val locker = Locker()

        val flowData = FlowData<String>()
        val flowToInt = flowData.flow.then(TaskContext.INDEPENDENT) { string ->
            string1.set(string)
            string.int()
        }
        val flowReversed = flowToInt.then(TaskContext.INDEPENDENT) { integer ->
            integer1.set(integer)
            - integer
        }
        val flowToString = flowReversed.then(TaskContext.INDEPENDENT) { integer ->
            integer2.set(integer)
            integer.toString()
        }
        val flowCollect = flowToString.then(TaskContext.INDEPENDENT) { string ->
            string2.set(string)
            locker.unlock()
        }

        Assertions.assertEquals("", string1.get())
        Assertions.assertEquals(0, integer1.get())
        Assertions.assertEquals(0, integer2.get())
        Assertions.assertEquals("", string2.get())

        flowData.publish("-42")
        var timeout = delay(1024) { locker.unlock() }
        locker.lock()
        timeout.cancel("Done")

        Assertions.assertEquals("-42", string1.get())
        Assertions.assertEquals(- 42, integer1.get())
        Assertions.assertEquals(42, integer2.get())
        Assertions.assertEquals("42", string2.get())

        flowData.publish("-73")
        timeout = delay(1024) { locker.unlock() }
        locker.lock()
        timeout.cancel("Done")

        Assertions.assertEquals("-73", string1.get())
        Assertions.assertEquals(- 73, integer1.get())
        Assertions.assertEquals(73, integer2.get())
        Assertions.assertEquals("73", string2.get())

        flowToString.cancel()

        flowData.publish("666")
        timeout = delay(1024) { locker.unlock() }
        locker.lock()
        timeout.cancel("Done")

        Assertions.assertEquals("666", string1.get())
        Assertions.assertEquals(666, integer1.get())
        Assertions.assertEquals(73, integer2.get())
        Assertions.assertEquals("73", string2.get())
    }

    @Test
    fun severalBranches()
    {
        val operand11 = AtomicInteger(0)
        val operand12 = AtomicInteger(0)
        val operand21 = AtomicInteger(0)
        val operand22 = AtomicInteger(0)
        val result1 = AtomicInteger(0)
        val result2 = AtomicInteger(0)
        val locker = Locker()
        val flowData = FlowData<Pair<Int, Int>>()

        flowData.flow
            .then(TaskContext.INDEPENDENT) { (integer1, integer2) ->
                operand11.set(integer1)
                operand12.set(integer2)
                integer1 + integer2
            }
            .then(TaskContext.INDEPENDENT) { integer ->
                result1.set(integer)
                locker.unlock()
            }

        flowData.flow
            .then(TaskContext.INDEPENDENT) { (integer1, integer2) ->
                operand21.set(integer1)
                operand22.set(integer2)
                integer1 - integer2
            }
            .then(TaskContext.INDEPENDENT) { integer ->
                result2.set(integer)
                locker.unlock()
            }

        flowData.publish(Pair(73, 42))
        locker.lock()
        locker.lock()

        Assertions.assertEquals(73, operand11.get())
        Assertions.assertEquals(42, operand12.get())
        Assertions.assertEquals(73 + 42, result1.get())
        Assertions.assertEquals(73, operand21.get())
        Assertions.assertEquals(42, operand22.get())
        Assertions.assertEquals(73 - 42, result2.get())
    }

    @Test
    fun combine()
    {
        val operand1 = AtomicInteger(0)
        val operand2 = AtomicInteger(0)
        val result = AtomicInteger(0)
        val flowData1 = FlowData<Int>()
        val flowData2 = FlowData<Int>()
        val locker = Locker()
        flowCombiner(flowData1.flow, flowData2.flow) { number1, number2 ->
            operand1.set(number1)
            operand2.set(number2)
            number1 + number2
        }
            .then(TaskContext.INDEPENDENT) { integer ->
                result.set(integer)
                locker.unlock()
            }
        flowData1.publish(73)
        flowData2.publish(42)
        locker.lock()
        Assertions.assertEquals(73, operand1.get())
        Assertions.assertEquals(42, operand2.get())
        Assertions.assertEquals(73 + 42, result.get())
    }
}
