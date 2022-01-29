package khelp.utilities.text

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TextsTests
{
    @Test
    fun lastIndexOfTests()
    {
        Assertions.assertEquals(- 1, lastIndexOf("Apple Pie", 2, 'z', 'o', 'f'))
        Assertions.assertEquals(0, lastIndexOf("Apple Pie", 12, 'Z', 'A', 'D'))
        Assertions.assertEquals(0, lastIndexOf("Apple Pie", 2, 'Z', 'A', 'D'))
        Assertions.assertEquals(6, lastIndexOf("Apple Pie", 12, 'P', 'A', 'D'))
        Assertions.assertEquals(6, lastIndexOf("Apple Pie", 12, 'P', 'A', 'D'))
        Assertions.assertEquals(6, lastIndexOf("Apple Pie", 12, 'P', 'O', 'D'))
        Assertions.assertEquals(- 1, lastIndexOf("Apple Pie", 5, 'P', 'O', 'D'))
    }

    @Test
    fun replaceHolesTests()
    {
        Assertions.assertEquals("red apple", replaceHoles("{0} {1}", "red", "apple"))
        Assertions.assertEquals("pomme rouge", replaceHoles("{1} {0}", "rouge", "pomme"))
        Assertions.assertEquals("man with dog or dog with man ?",
                                replaceHoles("{0} with {1} or {1} with {0} ?", "man", "dog"))
    }

    @Test
    fun replacePluralsTests()
    {
        Assertions.assertEquals("plane", replacePlurals("plane{0|0|s}", 1))
        Assertions.assertEquals("planes", replacePlurals("plane{0|0|s}", 2))
        Assertions.assertEquals("foot", replacePlurals("foot{0|3|eet}", 1))
        Assertions.assertEquals("feet", replacePlurals("foot{0|3|eet}", 2))
        Assertions.assertEquals("plane foot, foot plane",
                                replacePlurals("plane{0|0|s} foot{1|3|eet}, foot{1|3|eet} plane{0|0|s}", 1, 1))
        Assertions.assertEquals("planes foot, foot planes",
                                replacePlurals("plane{0|0|s} foot{1|3|eet}, foot{1|3|eet} plane{0|0|s}", 2, 1))
        Assertions.assertEquals("plane feet, feet plane",
                                replacePlurals("plane{0|0|s} foot{1|3|eet}, foot{1|3|eet} plane{0|0|s}", 1, 2))
        Assertions.assertEquals("planes feet, feet planes",
                                replacePlurals("plane{0|0|s} foot{1|3|eet}, foot{1|3|eet} plane{0|0|s}", 2, 2))
    }

    @Test
    fun conjugatePluralTests()
    {
        Assertions.assertEquals("1 foot", conjugatePlural("{0} foot{0|3|eet}", 1))
        Assertions.assertEquals("2 feet", conjugatePlural("{0} foot{0|3|eet}", 2))
        Assertions.assertEquals("1 foot, 1 plane. Repeat : 1 plane, 1 foot", conjugatePlural(
            "{0} foot{0|3|eet}, {1} plane{1|0|s}. Repeat : {1} plane{1|0|s}, {0} foot{0|3|eet}",
            1, 1))
        Assertions.assertEquals("2 feet, 1 plane. Repeat : 1 plane, 2 feet", conjugatePlural(
            "{0} foot{0|3|eet}, {1} plane{1|0|s}. Repeat : {1} plane{1|0|s}, {0} foot{0|3|eet}",
            2, 1))
        Assertions.assertEquals("1 foot, 5 planes. Repeat : 5 planes, 1 foot", conjugatePlural(
            "{0} foot{0|3|eet}, {1} plane{1|0|s}. Repeat : {1} plane{1|0|s}, {0} foot{0|3|eet}",
            1, 5))
        Assertions.assertEquals("2 feet, 5 planes. Repeat : 5 planes, 2 feet", conjugatePlural(
            "{0} foot{0|3|eet}, {1} plane{1|0|s}. Repeat : {1} plane{1|0|s}, {0} foot{0|3|eet}",
            2, 5))
    }
}