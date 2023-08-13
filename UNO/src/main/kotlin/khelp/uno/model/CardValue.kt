package khelp.uno.model

enum class CardValue(val value : Int, val fileName : String, val inFullStack : Boolean)
{
    ZERO(value = 0, fileName = "0.png", inFullStack = true),
    ONE(value = 1, fileName = "1.png", inFullStack = true),
    TWO(value = 2, fileName = "2.png", inFullStack = true),
    THREE(value = 3, fileName = "3.png", inFullStack = true),
    FOUR(value = 4, fileName = "4.png", inFullStack = true),
    FIVE(value = 5, fileName = "5.png", inFullStack = true),
    SIX(value = 6, fileName = "6.png", inFullStack = true),
    SEVEN(value = 7, fileName = "7.png", inFullStack = true),
    EIGHT(value = 8, fileName = "8.png", inFullStack = true),
    NINE(value = 9, fileName = "9.png", inFullStack = true),
    COLOR(value = 100, fileName = "color.png", inFullStack = false),
    MORE_2(value = 50, fileName = "more2.png", inFullStack = true),
    MORE_4(value = 100, fileName = "more4.png", inFullStack = false),
    RETURN(value = 50, fileName = "return.png", inFullStack = true),
    SKIP(value = 50, fileName = "skip.png", inFullStack = true)
}
