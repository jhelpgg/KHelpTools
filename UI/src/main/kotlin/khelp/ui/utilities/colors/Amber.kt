package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_AMBER_0050
import khelp.ui.utilities.COLOR_AMBER_0100
import khelp.ui.utilities.COLOR_AMBER_0200
import khelp.ui.utilities.COLOR_AMBER_0300
import khelp.ui.utilities.COLOR_AMBER_0400
import khelp.ui.utilities.COLOR_AMBER_0500
import khelp.ui.utilities.COLOR_AMBER_0600
import khelp.ui.utilities.COLOR_AMBER_0700
import khelp.ui.utilities.COLOR_AMBER_0800
import khelp.ui.utilities.COLOR_AMBER_0900
import khelp.ui.utilities.COLOR_AMBER_A100
import khelp.ui.utilities.COLOR_AMBER_A200
import khelp.ui.utilities.COLOR_AMBER_A400
import khelp.ui.utilities.COLOR_AMBER_A700
import kotlin.math.max
import kotlin.math.min

enum class Amber(private val color : Int) : BaseColor<Amber>
{
    /** Amber 50 */
    AMBER_0050(COLOR_AMBER_0050),

    /** Amber 100 */
    AMBER_0100(COLOR_AMBER_0100),

    /** Amber 200 */
    AMBER_0200(COLOR_AMBER_0200),

    /** Amber 300 */
    AMBER_0300(COLOR_AMBER_0300),

    /** Amber 400 */
    AMBER_0400(COLOR_AMBER_0400),

    /** Amber 500 : Reference */
    AMBER_0500(COLOR_AMBER_0500),

    /** Amber 600 */
    AMBER_0600(COLOR_AMBER_0600),

    /** Amber 700 */
    AMBER_0700(COLOR_AMBER_0700),

    /** Amber 800 */
    AMBER_0800(COLOR_AMBER_0800),

    /** Amber 900 */
    AMBER_0900(COLOR_AMBER_0900),

    /** Amber A100 */
    AMBER_A100(COLOR_AMBER_A100),

    /** Amber A200 */
    AMBER_A200(COLOR_AMBER_A200),

    /** Amber A400 */
    AMBER_A400(COLOR_AMBER_A400),

    /** Amber A700 */
    AMBER_A700(COLOR_AMBER_A700)

    ;

    override val argb : Int = this.color

    override val lighter : Amber
        get() = Amber.values()[max(0, this.ordinal - 1)]

    override val darker : Amber
        get() = Amber.values()[min(Amber.values().size - 1, this.ordinal + 1)]

    override val lightest : Amber get() = Amber.AMBER_0050

    override val darkest : Amber get() = Amber.AMBER_A700

    override val representative : Amber get() = Amber.AMBER_0500
}
