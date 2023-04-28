package khelp.utilities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CheckersTests
{
    @Test
    fun argumentCheckTest()
    {
        argumentCheck(true) { "Issue" }

        try
        {
            argumentCheck(false) { "Issue" }
            Assertions.fail<Unit>("Should throw an exception")
        }
        catch (illegalArgumentException : IllegalArgumentException)
        {
            Assertions.assertEquals("Issue", illegalArgumentException.message)
        }
    }

    @Test
    fun stateCheckTest()
    {
        stateCheck(true) { "Issue" }

        try
        {
            stateCheck(false) { "Issue" }
            Assertions.fail<Unit>("Should throw an exception")
        }
        catch (illegalStateException : IllegalStateException)
        {
            Assertions.assertEquals("Issue", illegalStateException.message)
        }
    }

    @Test
    fun notNullCheckTest()
    {
        notNullCheck("Something") { "Issue" }

        try
        {
            val oupsy : Any? = null
            notNullCheck(oupsy) { "Issue" }
            Assertions.fail<Unit>("Should throw an exception")
        }
        catch (nullPointerException : NullPointerException)
        {
            Assertions.assertEquals("Issue", nullPointerException.message)
        }
    }

    @Test
    fun elementExistsCheckTest()
    {
        elementExistsCheck(true) { "Issue" }

        try
        {
            elementExistsCheck(false) { "Issue" }
            Assertions.fail<Unit>("Should throw an exception")
        }
        catch (noSuchElementException : NoSuchElementException)
        {
            Assertions.assertEquals("Issue", noSuchElementException.message)
        }
    }
}
