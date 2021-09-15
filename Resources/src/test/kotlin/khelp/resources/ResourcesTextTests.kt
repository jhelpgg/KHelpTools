package khelp.resources

import khelp.io.ClassSource
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.Locale

class ResourcesTextTests
{
    @Test
    fun changeLanguage()
    {
        val oneLine = "one line"
        val severalLines = "several lines"
        val resources = Resources(ClassSource(ResourcesTextTests::class.java))
        val resourcesText = resources.resourcesText("testTexts")

        Thread.sleep(1024)
        Resources.languageObservableData.value(Locale.ENGLISH)
        Thread.sleep(1024)
        Assertions.assertEquals("This is a value in one line", resourcesText[oneLine])
        Assertions.assertEquals("First line\n" +
                                "Second line\n" +
                                "Third line\n" +
                                "   3 spaces at start\n" +
                                "    Tab at start\n" +
                                "\n" +
                                "Empty line just above", resourcesText[severalLines])

        Resources.languageObservableData.value(Locale.FRENCH)
        Thread.sleep(1024)
        Assertions.assertEquals("Juste une ligne et c'est le seul traduit", resourcesText[oneLine])
        Assertions.assertEquals("First line\n" +
                                "Second line\n" +
                                "Third line\n" +
                                "   3 spaces at start\n" +
                                "    Tab at start\n" +
                                "\n" +
                                "Empty line just above", resourcesText[severalLines])
    }
}
