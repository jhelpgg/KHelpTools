package khelp.utilities.text

/**
 * Default escape characters : \ (see [StringExtractor])
 */
val DEFAULT_ESCAPE_CHARACTERS = "\\"
/**
 * Default escape separators : [space], [Line return \n], [tabulation \t], [carriage return \r] (see
 * [StringExtractor])
 */
val DEFAULT_SEPARATORS = " \n\t\r"
/**
 * Default string limiters : " and ' (see [StringExtractor])
 */
val DEFAULT_STRING_LIMITERS = "\"'"

/**
 * Compute the last index <= of given offset in the char sequence of one of given characters
 *
 * @param charSequence Char sequence where search one character
 * @param offset       Offset maximum for search
 * @param characters   Characters search
 * @return Index of the last character <= given offset found in char sequence that inside in the given list. -1 if
 * the char
 * sequence doesn't contain any of given characters before the given offset
 */
fun lastIndexOf(charSequence: CharSequence, offset: Int = charSequence.length, vararg characters: Char): Int
{
    val start = Math.min(charSequence.length - 1, offset)
    var character: Char

    for (index in start downTo 0)
    {
        character = charSequence[index]

        for (car in characters)
        {
            if (car == character)
            {
                return index
            }
        }
    }

    return -1
}
