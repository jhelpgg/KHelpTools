package khelp.engine3d.render

enum class TwoSidedRule
{
    /**
     * Use the material setting for the tow side mode
     */
    AS_MATERIAL,
    /**
     * Force the object be one sided
     */
    FORCE_ONE_SIDE,
    /**
     * Force the object be 2 sided
     */
    FORCE_TWO_SIDE
}
