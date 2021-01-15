package khelp.kotlinspector

import org.junit.jupiter.api.Assertions

fun <T> assumeNotNull(element: T?): T
{
    Assertions.assertNotNull(element)
    return element!!
}

inline fun <T, reified A> assumeIs(element: T?): A
{
    Assertions.assertNotNull(element, "element is null")
    Assertions.assertTrue(element is A, "$element not instance of ${A::class.java.name}")
    return element as A
}