package khelp.utilities

/**
 * Check if an argument respect a condition.
 *
 * If condition failed an [IllegalArgumentException] is throw with given message
 */
@Throws(IllegalArgumentException::class)
inline fun argumentCheck(condition : Boolean, messageIfFail : () -> String)
{
    if (!condition)
    {
        throw IllegalArgumentException(messageIfFail())
    }
}

/**
 * Check if a state respect a condition.
 *
 * If condition failed an [IllegalStateException] is throw with given message
 */
@Throws(IllegalStateException::class)
inline fun stateCheck(condition : Boolean, messageIfFail : () -> String)
{
    if (!condition)
    {
        throw IllegalStateException(messageIfFail())
    }
}

/**
 * Check if a value is not `null`
 *
 * If value is `null` a [NullPointerException] is throw
 *
 * If not `null` it returns the value cast to not `null` version.
 */
@Throws(NullPointerException::class)
inline fun <T> notNullCheck(value : T?, messageIfNull : () -> String) : T =
    value ?: throw NullPointerException(messageIfNull())

/**
 * Check if a required element exists respect a condition.
 *
 * If condition failed an [NoSuchElementException] is throw with given message
 */
@Throws(NoSuchElementException::class)
inline fun elementExistsCheck(condition : Boolean, messageIfFail : () -> String)
{
    if (!condition)
    {
        throw NoSuchElementException(messageIfFail())
    }
}

@Throws(AssertionError::class)
inline fun <R> assertion(condition : Boolean, message : String = "Assertion failed", action : () -> R) : R
{
    if (condition.not())
    {
        throw AssertionError(message)
    }

    return action()
}