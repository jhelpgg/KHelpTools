package khelp.utilities.collections

object CaseNotSensitiveOrder : Comparator<String>
{
    override fun compare(string1: String, string2: String): Int =
        string1.compareTo(string2, true)
}