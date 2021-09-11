package khelp.ui.font

/**
 * Choice for bold and italic to decide to keep as defined by a stream or force a value
 */
enum class FontValue
{
    /**
     * Force the value to be `true` (It transforms the font at need)
     */
    TRUE,

    /**
     * Force the value to be `false` (It transforms the font at need)
     */
    FALSE,

    /**
     * Use what is defined in the stream value
     */
    AS_DEFINED
}
