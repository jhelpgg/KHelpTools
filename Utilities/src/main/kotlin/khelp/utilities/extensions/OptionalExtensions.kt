package khelp.utilities.extensions

import java.util.Optional

/**
 * Apply a function if the option is present or an other action if absent
 * @param present Action to do if optional is present. The parameter is the value inside the optional
 * @param absent Action to do if optional absent
 * @param T Optional embed value type
 * @param R Result type
 * @return Result of **present** or **absent** function
 */
fun <T, R> Optional<T>.ifElse(present: (T) -> R, absent: () -> R) =
    if (this.isPresent) present(this.get())
    else absent()
