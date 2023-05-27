package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_BROWN_0050
import khelp.ui.utilities.COLOR_BROWN_0100
import khelp.ui.utilities.COLOR_BROWN_0200
import khelp.ui.utilities.COLOR_BROWN_0300
import khelp.ui.utilities.COLOR_BROWN_0400
import khelp.ui.utilities.COLOR_BROWN_0500
import khelp.ui.utilities.COLOR_BROWN_0600
import khelp.ui.utilities.COLOR_BROWN_0700
import khelp.ui.utilities.COLOR_BROWN_0800
import khelp.ui.utilities.COLOR_BROWN_0900
import kotlin.math.max
import kotlin.math.min

enum class Brown(private val color : Int) : BaseColor<Brown>
{
    /** Brown 50 */
    BROWN_0050(COLOR_BROWN_0050),

    /** Brown 100 */
    BROWN_0100(COLOR_BROWN_0100),

    /** Brown 200 */
    BROWN_0200(COLOR_BROWN_0200),

    /** Brown 300 */
    BROWN_0300(COLOR_BROWN_0300),

    /** Brown 400 */
    BROWN_0400(COLOR_BROWN_0400),

    /** Brown 500 : Reference */
    BROWN_0500(COLOR_BROWN_0500),

    /** Brown 600 */
    BROWN_0600(COLOR_BROWN_0600),

    /** Brown 700 */
    BROWN_0700(COLOR_BROWN_0700),

    /** Brown 800 */
    BROWN_0800(COLOR_BROWN_0800),

    /** Brown 900 */
    BROWN_0900(COLOR_BROWN_0900),

    ;

    override val argb : Int = this.color

    override val lighter : Brown
        get() = Brown.values()[max(0, this.ordinal - 1)]

    override val darker : Brown
        get() = Brown.values()[min(Brown.values().size - 1, this.ordinal + 1)]

    override val lightest : Brown get() = Brown.BROWN_0050

    override val darkest : Brown get() = Brown.BROWN_0900

    override val representative : Brown get() = Brown.BROWN_0500
}
