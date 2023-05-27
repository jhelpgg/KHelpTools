package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_TEAL_0050
import khelp.ui.utilities.COLOR_TEAL_0100
import khelp.ui.utilities.COLOR_TEAL_0200
import khelp.ui.utilities.COLOR_TEAL_0300
import khelp.ui.utilities.COLOR_TEAL_0400
import khelp.ui.utilities.COLOR_TEAL_0500
import khelp.ui.utilities.COLOR_TEAL_0600
import khelp.ui.utilities.COLOR_TEAL_0700
import khelp.ui.utilities.COLOR_TEAL_0800
import khelp.ui.utilities.COLOR_TEAL_0900
import khelp.ui.utilities.COLOR_TEAL_A100
import khelp.ui.utilities.COLOR_TEAL_A200
import khelp.ui.utilities.COLOR_TEAL_A400
import khelp.ui.utilities.COLOR_TEAL_A700
import kotlin.math.max
import kotlin.math.min

enum class Teal(private val color : Int) : BaseColor<Teal>
{
    /** Teal 50 */
    TEAL_0050(COLOR_TEAL_0050),

    /** Teal 100 */
    TEAL_0100(COLOR_TEAL_0100),

    /** Teal 200 */
    TEAL_0200(COLOR_TEAL_0200),

    /** Teal 300 */
    TEAL_0300(COLOR_TEAL_0300),

    /** Teal 400 */
    TEAL_0400(COLOR_TEAL_0400),

    /** Teal 500 : Reference */
    TEAL_0500(COLOR_TEAL_0500),

    /** Teal 600 */
    TEAL_0600(COLOR_TEAL_0600),

    /** Teal 700 */
    TEAL_0700(COLOR_TEAL_0700),

    /** Teal 800 */
    TEAL_0800(COLOR_TEAL_0800),

    /** Teal 900 */
    TEAL_0900(COLOR_TEAL_0900),

    /** Teal A100 */
    TEAL_A100(COLOR_TEAL_A100),

    /** Teal A200 */
    TEAL_A200(COLOR_TEAL_A200),

    /** Teal A400 */
    TEAL_A400(COLOR_TEAL_A400),

    /** Teal A700 */
    TEAL_A700(COLOR_TEAL_A700)

    ;

    override val argb : Int = this.color

    override val lighter : Teal
        get() = Teal.values()[max(0, this.ordinal - 1)]

    override val darker : Teal
        get() = Teal.values()[min(Teal.values().size - 1, this.ordinal + 1)]

    override val lightest : Teal get() = Teal.TEAL_0050

    override val darkest : Teal get() = Teal.TEAL_A700

    override val representative : Teal get() = Teal.TEAL_0500
}
