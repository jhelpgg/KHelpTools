package khelp.ui.events

enum class MousePress(val press : (current : Boolean, concern : Boolean) -> Boolean)
{
    PRESS({ current, concern -> if (concern) true else current }),
    RELEASE({ current, concern -> if (concern) false else current }),
    UNDEFINED({ current, _ -> current })
}