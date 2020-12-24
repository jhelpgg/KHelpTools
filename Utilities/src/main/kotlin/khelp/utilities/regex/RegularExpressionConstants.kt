package khelp.utilities.regex

import khelp.utilities.extensions.allCharactersExcludeThose
import khelp.utilities.extensions.atMost
import khelp.utilities.extensions.between
import khelp.utilities.extensions.plus
import khelp.utilities.extensions.regularExpression
import khelp.utilities.extensions.zeroOrMore
import khelp.utilities.text.digitInterval
import khelp.utilities.text.letterInterval
import khelp.utilities.text.letterOrDigitInterval
import khelp.utilities.text.letterOrDigitUnderscoreInterval
import khelp.utilities.text.lowerCaseInterval
import khelp.utilities.text.upperCaseInterval

val ANY = RegularExpression(".")

val WHITE_SPACE = RegularExpression("\\s")

val NOT_WHITE_SPACE = RegularExpression("\\S")

val LOWER_CASE = lowerCaseInterval.regularExpression

val UPPER_CASE = upperCaseInterval.regularExpression

val LETTER = letterInterval.regularExpression

val NOT_LETTER = letterInterval.allCharactersExcludeThose

val DIGIT = digitInterval.regularExpression

val NOT_DIGIT = digitInterval.allCharactersExcludeThose

val LETTER_OR_DIGIT = letterOrDigitInterval.regularExpression

val WORD = LETTER.oneOrMore()

val NAME = LETTER + letterOrDigitUnderscoreInterval.zeroOrMore()

val EMAIL =
    (letterOrDigitInterval + '+' + '.' + '_' + '%' + '-').between(1, 256) +
    '@' +
    LETTER_OR_DIGIT + (letterOrDigitInterval + '-').atMost(64) +
    ('.' + LETTER_OR_DIGIT + (letterOrDigitInterval + '-').atMost(25)).oneOrMore()

val INTEGER = DIGIT.oneOrMore()

val REAL = INTEGER + ('.' + INTEGER).zeroOrOne()

val LOCALE_SEPARATOR = charArrayOf('-', '_').regularExpression
