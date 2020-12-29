package khelp.utilities.extensions

import java.util.Base64
import java.util.Locale
import khelp.utilities.regex.LOCALE_SEPARATOR
import khelp.utilities.regex.RegularExpression
import khelp.utilities.regex.RegularExpressionGroup

val String.utf8 get() = this.toByteArray(Charsets.UTF_8)

val String.base64
    get() = Base64.getDecoder()
        .decode(this)

val String.regularExpression: RegularExpression get() = RegularExpression.text(this)

val String.ignoreCaseRegularExpression: RegularExpression
    get()
    {
        if (this.isEmpty())
        {
            return this.regularExpression
        }

        val characters = this.toCharArray()
        var regularExpression = characters[0].ignoreCaseRegularExpression

        for (index in 1 until characters.size)
        {
            regularExpression += characters[index].ignoreCaseRegularExpression
        }

        return regularExpression
    }

val String.anyWordExceptThis: RegularExpression
    get()
    {
        if (this.isEmpty())
        {
            return this.regularExpression
        }

        return RegularExpression("\\w*(?<!$this)")
    }

infix fun String.OR(regularExpression: RegularExpression) =
    this.regularExpression OR regularExpression

infix fun String.OR(regularExpression: RegularExpressionGroup) =
    this.regularExpression OR regularExpression

fun String.toLocale(): Locale
{
    val split = LOCALE_SEPARATOR.split(this, 3)

    return when (split.size)
    {
        1    -> Locale(split[0])
        2    -> Locale(split[0], split[1])
        else -> Locale(split[0], split[1], split[2])
    }
}
