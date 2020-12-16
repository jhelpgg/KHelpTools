package khelp.utilities.provider

import org.junit.Assert
import org.junit.Test

class ProviderTests
{
    private val test by provided<TestInterface>()
    private val label1 by provided<TestInterface>("Label1")
    private val label2 by provided<TestInterface>("Label2")

    @Test
    fun single()
    {
        provideSingle<TestInterface> { MagicTest }
        Assert.assertEquals(73, test.int())
        Assert.assertEquals(73, test.int())
        Assert.assertEquals(73, test.int())
        provideSingle<TestInterface> { AnswerTest }
        Assert.assertEquals(42, test.int())
        Assert.assertEquals(42, test.int())
        Assert.assertEquals(42, test.int())
    }

    @Test
    fun multiple()
    {
        provideMultiple(::cycle)
        Assert.assertEquals(42, test.int())
        Assert.assertEquals(73, test.int())
        Assert.assertEquals(42, test.int())
        Assert.assertEquals(73, test.int())
        Assert.assertEquals(42, test.int())
        Assert.assertEquals(73, test.int())
    }

    @Test
    fun label()
    {
        provideSingle<TestInterface>("Label1") { MagicTest }
        provideSingle<TestInterface>("Label2") { AnswerTest }
        Assert.assertEquals(73, label1.int())
        Assert.assertEquals(42, label2.int())
        Assert.assertEquals(73, label1.int())
        Assert.assertEquals(42, label2.int())
        Assert.assertEquals(73, label1.int())
        Assert.assertEquals(42, label2.int())
    }
}