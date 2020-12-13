package khelp.utilities.log

enum class LogLevel(val header: String, val order: Byte)
{
    /**
     * Verbose level
     */
    VERBOSE("VERBOSE ", 4),
    /**
     * Debug level
     */
    DEBUG("DEBUG ", 3),
    /**
     * Information level
     */
    INFORMATION("INFORMATION ", 2),
    /**
     * Warning level
     */
    WARNING("/!\\ WARNING /!\\ ", 1),
    /**
     * Error level
     */
    ERROR("$@-FAILED-@$ ", 0);
}