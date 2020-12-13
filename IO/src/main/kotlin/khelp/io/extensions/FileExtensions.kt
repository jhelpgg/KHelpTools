package khelp.io.extensions

import java.io.File
import java.util.Stack

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
