package khelp.io.extensions

import java.io.File
import java.util.Stack

/**
 * Create directory and its parents directories if necessary
 * @return `true`if directory exists at the exit of method. `false` if directory can't be created, or given path refer to an existing file, not a directory
 */
fun File.createDirectory(): Boolean
{
    if (this.exists())
    {
        return this.isDirectory
    }

    return try
    {
        this.mkdirs()
    }
    catch (_: Exception)
    {
        false
    }
}

/**
 * Create file and its parents directories if necessary
 * @return `true` if file exists at the end of the method. `false` if file can't be created
 */
fun File.createFile(): Boolean
{
    if (this.exists())
    {
        return true
    }

    if (!this.parentFile.createDirectory())
    {
        return false
    }

    return try
    {
        this.createNewFile()
    }
    catch (_: Exception)
    {
        false
    }
}

/**
 * Delete a file or directory.
 *
 * If its directory, it tries to delete its content first.
 *
 * @param tryOnExitIfFail to indicates to try delete when application exit if deletion actually failed
 * @return `true` if deletion succeed. `false`if failed, but for directory, may some content file/directory are deleted
 */
fun File.deleteFull(tryOnExitIfFail: Boolean = false): Boolean
{
    if (!this.exists())
    {
        return true
    }

    val stack = Stack<File>()
    stack.push(this)
    var file: File
    var children: Array<File>?

    while (stack.isNotEmpty())
    {
        file = stack.pop()

        if (file.isDirectory)
        {
            children = file.listFiles()

            if (children != null && children.isNotEmpty())
            {
                stack.push(file)

                for (child in children)
                {
                    stack.push(child)
                }
            }
            else
            {
                if (!file.deleteDirect(tryOnExitIfFail))
                {
                    return false
                }
            }
        }
        else if (!file.deleteDirect(tryOnExitIfFail))
        {
            return false
        }
    }

    return true
}

private fun File.deleteDirect(tryOnExitIfFail: Boolean): Boolean
{
    if (!this.exists())
    {
        return true
    }

    var deleted =
        try
        {
            this.delete()
        }
        catch (_: Exception)
        {
            false
        }

    if (!deleted && tryOnExitIfFail)
    {
        deleted =
            try
            {
                this.deleteOnExit()
                true
            }
            catch (_: Exception)
            {
                false
            }
    }

    return deleted
}
