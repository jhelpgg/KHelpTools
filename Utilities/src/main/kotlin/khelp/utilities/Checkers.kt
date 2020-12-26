package khelp.utilities

inline fun argumentCheck(condition: Boolean, messageIfFail: () -> String)
{
    if (!condition)
    {
        throw  IllegalArgumentException(messageIfFail())
    }
}

inline fun stateCheck(condition: Boolean, messageIfFail: () -> String)
{
    if (!condition)
    {
        throw  IllegalStateException(messageIfFail())
    }
}
