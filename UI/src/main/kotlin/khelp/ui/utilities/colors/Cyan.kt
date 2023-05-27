package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_CYAN_0050
import khelp.ui.utilities.COLOR_CYAN_0100
import khelp.ui.utilities.COLOR_CYAN_0200
import khelp.ui.utilities.COLOR_CYAN_0300
import khelp.ui.utilities.COLOR_CYAN_0400
import khelp.ui.utilities.COLOR_CYAN_0500
import khelp.ui.utilities.COLOR_CYAN_0600
import khelp.ui.utilities.COLOR_CYAN_0700
import khelp.ui.utilities.COLOR_CYAN_0800
import khelp.ui.utilities.COLOR_CYAN_0900
import khelp.ui.utilities.COLOR_CYAN_A100
import khelp.ui.utilities.COLOR_CYAN_A200
import khelp.ui.utilities.COLOR_CYAN_A400
import khelp.ui.utilities.COLOR_CYAN_A700
import kotlin.math.max
import kotlin.math.min

enum class Cyan(private val color : Int) : BaseColor<Cyan>
{
    /** Cyan 50 */
    CYAN_0050(COLOR_CYAN_0050),

    /** Cyan 100 */
    CYAN_0100(COLOR_CYAN_0100),

    /** Cyan 200 */
    CYAN_0200(COLOR_CYAN_0200),

    /** Cyan 300 */
    CYAN_0300(COLOR_CYAN_0300),

    /** Cyan 400 */
    CYAN_0400(COLOR_CYAN_0400),

    /** Cyan 500 : Reference */
    CYAN_0500(COLOR_CYAN_0500),

    /** Cyan 600 */
    CYAN_0600(COLOR_CYAN_0600),

    /** Cyan 700 */
    CYAN_0700(COLOR_CYAN_0700),

    /** Cyan 800 */
    CYAN_0800(COLOR_CYAN_0800),

    /** Cyan 900 */
    CYAN_0900(COLOR_CYAN_0900),

    /** Cyan A100 */
    CYAN_A100(COLOR_CYAN_A100),

    /** Cyan A200 */
    CYAN_A200(COLOR_CYAN_A200),

    /** Cyan A400 */
    CYAN_A400(COLOR_CYAN_A400),

    /** Cyan A700 */
    CYAN_A700(COLOR_CYAN_A700)

    ;

    override val argb : Int = this.color

    override val lighter : Cyan
        get() = Cyan.values()[max(0, this.ordinal - 1)]

    override val darker : Cyan
        get() = Cyan.values()[min(Cyan.values().size - 1, this.ordinal + 1)]

    override val lightest : Cyan get() = Cyan.CYAN_0050

    override val darkest : Cyan get() = Cyan.CYAN_A700

    override val representative : Cyan get() = Cyan.CYAN_0500
}
