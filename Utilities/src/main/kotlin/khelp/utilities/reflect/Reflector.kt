package khelp.utilities.reflect

import java.lang.reflect.Array
import java.util.Calendar
import khelp.utilities.extensions.calendar


/**
 * Default Byte value
 */
const val DEFAULT_BYTE = 0.toByte()

/**
 * Default Character value
 */
const val DEFAULT_CHARACTER = '\u0000'

/**
 * Default Double value
 */
const val DEFAULT_DOUBLE = 0.0

/**
 * Default Float value
 */
const val DEFAULT_FLOAT = 0.0f

/**
 * Default Integer value
 */
const val DEFAULT_INTEGER = 0

/**
 * Default Long value
 */
const val DEFAULT_LONG = 0L

/**
 * Default Short value
 */
const val DEFAULT_SHORT = 0.toShort()

/**
 * Compute a default value for a given class
 *
 * @param clazz Class to have a default value
 * @return Default value
 */
@Suppress("UNCHECKED_CAST")
fun <T> defaultValue(clazz: Class<T>, instanceProvider: (Class<*>) -> Any? = { null }): T
{
    if (clazz.isPrimitive)
    {
        if (Boolean::class.javaPrimitiveType == clazz)
        {
            return false as T
        }

        if (Char::class.javaPrimitiveType == clazz)
        {
            return '\u0000' as T
        }

        if (Byte::class.javaPrimitiveType == clazz)
        {
            return 0.toByte() as T
        }

        if (Short::class.javaPrimitiveType == clazz)
        {
            return 0.toShort() as T
        }

        if (Int::class.javaPrimitiveType == clazz)
        {
            return 0 as T
        }

        if (Long::class.javaPrimitiveType == clazz)
        {
            return 0L as T
        }

        if (Float::class.javaPrimitiveType == clazz)
        {
            return 0.0f as T
        }

        if (Double::class.javaPrimitiveType == clazz)
        {
            return 0.0 as T
        }
    }

    if (Boolean::class.java == clazz)
    {
        return java.lang.Boolean.FALSE as T
    }

    if (Char::class.java == clazz)
    {
        return DEFAULT_CHARACTER as T
    }

    if (Byte::class.java == clazz)
    {
        return DEFAULT_BYTE as T
    }

    if (Short::class.java == clazz)
    {
        return DEFAULT_SHORT as T
    }

    if (Int::class.java == clazz)
    {
        return DEFAULT_INTEGER as T
    }

    if (Long::class.java == clazz)
    {
        return DEFAULT_LONG as T
    }

    if (Float::class.java == clazz)
    {
        return DEFAULT_FLOAT as T
    }

    if (Double::class.java == clazz)
    {
        return DEFAULT_DOUBLE as T
    }

    if (Calendar::class.java == clazz)
    {
        return calendar(1970, 1, 1) as T
    }

    if (clazz.isEnum)
    {
        try
        {
            val values = clazz.getMethod("values")
                .invoke(null)

            if (values != null && Array.getLength(values) > 0)
            {
                return Array.get(values, 0) as T
            }
        }
        catch (ignored: Exception)
        {
        }

    }

    if (CharSequence::class.java.isAssignableFrom(clazz))
    {
        return "" as T
    }

    if (clazz.isArray)
    {
        var componentType = clazz.componentType
        var count = 1

        while (componentType.isArray)
        {
            componentType = componentType.componentType
            count++
        }

        return Array.newInstance(componentType, *IntArray(count)) as T
    }

    val constructor = clazz.declaredConstructors[0]
    val types = constructor.parameterTypes
    val parameters = arrayOfNulls<Any>(types.size)

    for ((index, type) in types.withIndex())
    {
        parameters[index] = instanceProvider(type) ?: defaultValue(type, instanceProvider)
    }

    return constructor.newInstance(*parameters) as T
}
