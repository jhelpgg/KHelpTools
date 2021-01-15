package khelp.kotlinspector.model

enum class ValOrVar
{
    NONE,
    VAL,
    VAR
}

fun parseValOrVar(serialized: String?): ValOrVar =
    when (serialized?.trim())
    {
        "val" -> ValOrVar.VAL
        "var" -> ValOrVar.VAR
        else  -> ValOrVar.NONE
    }