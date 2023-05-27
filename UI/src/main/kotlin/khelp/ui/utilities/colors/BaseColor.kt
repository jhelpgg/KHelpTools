package khelp.ui.utilities.colors

sealed interface BaseColor<BC : BaseColor<BC>>
{
    /** Integer ARGB value */
    val argb : Int

    /** If their no lighter version, just return the same color */
    val lighter : BC

    /** If their no darker version, just return the same color */
    val darker : BC

    /** Lightest color */
    val lightest : BC

    /** Darkest color */
    val darkest : BC

    /** Representative color */
    val representative : BC
}
