package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_BLUE_0050
import khelp.ui.utilities.COLOR_BLUE_0100
import khelp.ui.utilities.COLOR_BLUE_0200
import khelp.ui.utilities.COLOR_BLUE_0300
import khelp.ui.utilities.COLOR_BLUE_0400
import khelp.ui.utilities.COLOR_BLUE_0500
import khelp.ui.utilities.COLOR_BLUE_0600
import khelp.ui.utilities.COLOR_BLUE_0700
import khelp.ui.utilities.COLOR_BLUE_0800
import khelp.ui.utilities.COLOR_BLUE_0900
import khelp.ui.utilities.COLOR_BLUE_A100
import khelp.ui.utilities.COLOR_BLUE_A200
import khelp.ui.utilities.COLOR_BLUE_A400
import khelp.ui.utilities.COLOR_BLUE_A700
import kotlin.math.max
import kotlin.math.min

enum class Blue(private val color : Int) : BaseColor<Blue>
{
    /** Blue 50 */
    BLUE_0050(COLOR_BLUE_0050),

    /** Blue 100 */
    BLUE_0100(COLOR_BLUE_0100),

    /** Blue 200 */
    BLUE_0200(COLOR_BLUE_0200),

    /** Blue 300 */
    BLUE_0300(COLOR_BLUE_0300),

    /** Blue 400 */
    BLUE_0400(COLOR_BLUE_0400),

    /** Blue 500 : Reference */
    BLUE_0500(COLOR_BLUE_0500),

    /** Blue 600 */
    BLUE_0600(COLOR_BLUE_0600),

    /** Blue 700 */
    BLUE_0700(COLOR_BLUE_0700),

    /** Blue 800 */
    BLUE_0800(COLOR_BLUE_0800),

    /** Blue 900 */
    BLUE_0900(COLOR_BLUE_0900),

    /** Blue A100 */
    BLUE_A100(COLOR_BLUE_A100),

    /** Blue A200 */
    BLUE_A200(COLOR_BLUE_A200),

    /** Blue A400 */
    BLUE_A400(COLOR_BLUE_A400),

    /** Blue A700 */
    BLUE_A700(COLOR_BLUE_A700)

    ;

    override val argb : Int = this.color

    override val lighter : Blue
        get() = Blue.values()[max(0, this.ordinal - 1)]

    override val darker : Blue
        get() = Blue.values()[min(Blue.values().size - 1, this.ordinal + 1)]

    override val lightest : Blue get() = Blue.BLUE_0050

    override val darkest : Blue get() = Blue.BLUE_A700

    override val representative : Blue get() = Blue.BLUE_0500
}
