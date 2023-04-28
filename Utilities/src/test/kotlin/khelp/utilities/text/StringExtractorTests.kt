package khelp.utilities.text

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StringExtractorTests
{
    @Test
    fun noSeparatorTest() {
        val extractor = StringExtractor("Hello world ! 'This is a phrase'")

        var next = extractor.next()
        Assertions.assertNotNull(next)
        Assertions.assertEquals("Hello", next)
        Assertions.assertFalse(extractor.isSeparator)
        Assertions.assertFalse(extractor.isString)

        next = extractor.next()
        Assertions.assertNotNull(next)
        Assertions.assertEquals("world", next)
        Assertions.assertFalse(extractor.isSeparator)
        Assertions.assertFalse(extractor.isString)

        next = extractor.next()
        Assertions.assertNotNull(next)
        Assertions.assertEquals("!", next)
        Assertions.assertFalse(extractor.isSeparator)
        Assertions.assertFalse(extractor.isString)

        next = extractor.next()
        Assertions.assertNotNull(next)
        Assertions.assertEquals("This is a phrase", next)
        Assertions.assertFalse(extractor.isSeparator)
        Assertions.assertTrue(extractor.isString)

        next = extractor.next()
        Assertions.assertNull(next)
    }

    @Test
    fun withSeparatorTest() {
        val extractor = StringExtractor("Hello world ! 'This is a phrase'", returnSeparators = true)

        var next = extractor.next()
        Assertions.assertNotNull(next)
        Assertions.assertEquals("Hello", next)
        Assertions.assertFalse(extractor.isSeparator)
        Assertions.assertFalse(extractor.isString)

        next = extractor.next()
        Assertions.assertNotNull(next)
        Assertions.assertEquals(" ", next)
        Assertions.assertTrue(extractor.isSeparator)
        Assertions.assertFalse(extractor.isString)

        next = extractor.next()
        Assertions.assertNotNull(next)
        Assertions.assertEquals("world", next)
        Assertions.assertFalse(extractor.isSeparator)
        Assertions.assertFalse(extractor.isString)

        next = extractor.next()
        Assertions.assertNotNull(next)
        Assertions.assertEquals(" ", next)
        Assertions.assertTrue(extractor.isSeparator)
        Assertions.assertFalse(extractor.isString)

        next = extractor.next()
        Assertions.assertNotNull(next)
        Assertions.assertEquals("!", next)
        Assertions.assertFalse(extractor.isSeparator)
        Assertions.assertFalse(extractor.isString)

        next = extractor.next()
        Assertions.assertNotNull(next)
        Assertions.assertEquals(" ", next)
        Assertions.assertTrue(extractor.isSeparator)
        Assertions.assertFalse(extractor.isString)

        next = extractor.next()
        Assertions.assertNotNull(next)
        Assertions.assertEquals("This is a phrase", next)
        Assertions.assertFalse(extractor.isSeparator)
        Assertions.assertTrue(extractor.isString)

        next = extractor.next()
        Assertions.assertNull(next)
    }
}