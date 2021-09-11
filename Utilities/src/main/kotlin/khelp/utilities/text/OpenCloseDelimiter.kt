package khelp.utilities.text

internal data class OpenCloseDelimiter(val open: Char, val close: Char, val countOpenClose: Boolean, var counter: Int = 0)
