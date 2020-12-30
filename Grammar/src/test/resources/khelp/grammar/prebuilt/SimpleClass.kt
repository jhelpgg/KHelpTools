// One line comment before package

/*
 * Multiline comment before package
 */

package khelp.grammar.prebuilt

// Imports

/*
 * Imports
 */

import khelp.utilities.regex.NAME

// Between 2 imports

/* Multi in one */

import khelp.utilities.text.interval

/**
 * This i a simple class for parsing tests
 *
 * Nothing more to say
 */
class SimpleClass
{
    /**Interval for test*/
    private val interval = interval('G', 'U')

    /**
     * Indicates if name is valid
     * @param name Name tested
     * @return `true` if name is valid
     */
    fun isValid(name: String): Boolean = NAME.matches(name)

    /**
     * Indicates if character is managed
     * @param char Tested character
     * @return `true` if character is managed
     */
    fun inside(char: Char): Boolean
    {
        return char in interval
    }
}
