package khelp.engine3d.render

enum class TwoSidedRule
{
    /**
     * Use the material setting for the two sides mode
     */
    AS_MATERIAL,
    /**
     * Force the object be one side
     */
    FORCE_ONE_SIDE,
    /**
     * Force the object be two sides
     */
    FORCE_TWO_SIDE
}
