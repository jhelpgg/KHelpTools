package khelp.utilities.provider

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class ProviderTests
{
    private val test by provided<TestInterface>()
    private val label1 by provided<TestInterface>("Label1")
    private val label2 by provided<TestInterface>("Label2")

    @Test
    fun single()
    {
        provideSingle<TestInterface> { MagicTest }
        Assertions.assertEquals(73, test.int())
        Assertions.assertEquals(73, test.int())
        Assertions.assertEquals(73, test.int())
        provideSingle<TestInterface> { AnswerTest }
        Assertions.assertEquals(42, test.int())
        Assertions.assertEquals(42, test.int())
        Assertions.assertEquals(42, test.int())
    }

    @Test
    fun multiple()
    {
        provideMultiple(::cycle)
        Assertions.assertEquals(42, test.int())
        Assertions.assertEquals(73, test.int())
        Assertions.assertEquals(42, test.int())
        Assertions.assertEquals(73, test.int())
        Assertions.assertEquals(42, test.int())
        Assertions.assertEquals(73, test.int())
    }

    @Test
    fun label()
    {
        provideSingle<TestInterface>("Label1") { MagicTest }
        provideSingle<TestInterface>("Label2") { AnswerTest }
        Assertions.assertEquals(73, label1.int())
        Assertions.assertEquals(42, label2.int())
        Assertions.assertEquals(73, label1.int())
        Assertions.assertEquals(42, label2.int())
        Assertions.assertEquals(73, label1.int())
        Assertions.assertEquals(42, label2.int())
    }
}