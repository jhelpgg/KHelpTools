package khelp.io

import java.io.File
import java.io.IOException
import khelp.io.extensions.createDirectory
import khelp.io.extensions.createFile
import khelp.utilities.log.exception
import khelp.utilities.text.StringCutter

private class BaseDirectories


/**
 * Path separator used in URL, ZIP, JAR
 */
const val PATH_SEPARATOR = '/'
/**
 * Path the represents the parent directory
 */
const val PREVIOUS_DIRECTORY = ".."
const val CURRENT_DIRECTORY = "."

/**
 * Directory external of the code
 */
val outsideDirectory = File("")

/**
 * "Home" directory
 */
val homeDirectory: File by lazy {
    var directory = File(outsideDirectory, "home")

    try
    {
        val home = System.getProperty("user.home")

        if (home != null)
        {
            val homeDirectory = File(home)

            if (homeDirectory.exists() && homeDirectory.canRead() && homeDirectory.canWrite())
            {
                directory = homeDirectory
            }
        }
    }
    catch (exception: Exception)
    {
        exception(exception, "Failed to get home directory, use outside directory")
    }

    directory
}

/**
 * Temporary directory
 */
val temporaryDirectory: File by lazy {
    var directory: File? = null
    var path = System.getProperty("user.home")

    if (path != null)
    {
        directory = File(path)

        if (!directory.exists() || !directory.canRead() || !directory.canWrite())
        {
            directory = null
        }
    }

    if (directory == null)
    {
        path = System.getProperty("user.dir")

        if (path != null)
        {
            directory = File(path)
            if (!directory.exists() || !directory.canRead() || !directory.canWrite())
            {
                directory = null
            }
        }
    }

    if (directory == null)
    {
        directory = outsideDirectory
    }

    directory = File(directory, "JHelp/temporary")
    directory.mkdirs()
    directory
}


/**
 * Create a temporary directory.
 *
 * That is to say, a new empty directory inside the temporary directory
 *
 * @return Created directory
 * @throws IOException On creation issue
 */
@Throws(IOException::class)
fun createTemporaryDirectory(): File
{
    var name = 0
    var file = File(temporaryDirectory, "temp_$name")

    while (file.exists())
    {
        name++
        file = File(temporaryDirectory, "temp_$name")
    }

    if (!file.createDirectory())
    {
        throw IOException("Can't create temporary directory " + file.absolutePath)
    }

    file.deleteOnExit()
    return file
}

/**
 * Create a temporary file.
 *
 * That is to say, a file inside the temporary directory
 *
 * @param string File name
 * @return Created file
 * @throws IOException On creation issue
 */
@Throws(IOException::class)
fun createTemporaryFile(string: String): File
{
    val file = File(temporaryDirectory, string)

    if (!file.createFile())
    {
        throw IOException("Can't create temporary file " + file.absolutePath)
    }

    file.deleteOnExit()
    return file
}

/**
 * Obtain a file outside of the code.
 *
 * If this class is in a jar called A.jar, and this jar is in /My/Path/A.jar then the file will be relative to /My/Path
 *
 * @param path Relative path
 * @return The file
 */
fun obtainExternalFile(path: String): File =
    obtainFile(outsideDirectory, path)


/**
 * Obtain a file relative to a directory
 *
 * @param directory Directory reference
 * @param path      Path search
 * @param separator Separator used in path. By default it used `/`
 * @return The file
 */
fun obtainFile(directory: File, path: String, separator: Char = PATH_SEPARATOR): File
{
    var file = directory
    val stringCutter = StringCutter(path, separator)

    var next = stringCutter.next()

    while (next != null)
    {
        if (PREVIOUS_DIRECTORY == next)
        {
            file = file.parentFile
        }
        else if (CURRENT_DIRECTORY != next && next.isNotEmpty())
        {
            file = File(file, next)
        }

        next = stringCutter.next()
    }

    return file
}
