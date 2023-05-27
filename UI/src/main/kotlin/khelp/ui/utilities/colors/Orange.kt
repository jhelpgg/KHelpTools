package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_ORANGE_0050
import khelp.ui.utilities.COLOR_ORANGE_0100
import khelp.ui.utilities.COLOR_ORANGE_0200
import khelp.ui.utilities.COLOR_ORANGE_0300
import khelp.ui.utilities.COLOR_ORANGE_0400
import khelp.ui.utilities.COLOR_ORANGE_0500
import khelp.ui.utilities.COLOR_ORANGE_0600
import khelp.ui.utilities.COLOR_ORANGE_0700
import khelp.ui.utilities.COLOR_ORANGE_0800
import khelp.ui.utilities.COLOR_ORANGE_0900
import khelp.ui.utilities.COLOR_ORANGE_A100
import khelp.ui.utilities.COLOR_ORANGE_A200
import khelp.ui.utilities.COLOR_ORANGE_A400
import khelp.ui.utilities.COLOR_ORANGE_A700
import kotlin.math.max
import kotlin.math.min

enum class Orange(private val color : Int) : BaseColor<Orange>
{
    /** Orange 50 */
    ORANGE_0050(COLOR_ORANGE_0050),

    /** Orange 100 */
    ORANGE_0100(COLOR_ORANGE_0100),

    /** Orange 200 */
    ORANGE_0200(COLOR_ORANGE_0200),

    /** Orange 300 */
    ORANGE_0300(COLOR_ORANGE_0300),

    /** Orange 400 */
    ORANGE_0400(COLOR_ORANGE_0400),

    /** Orange 500 : Reference */
    ORANGE_0500(COLOR_ORANGE_0500),

    /** Orange 600 */
    ORANGE_0600(COLOR_ORANGE_0600),

    /** Orange 700 */
    ORANGE_0700(COLOR_ORANGE_0700),

    /** Orange 800 */
    ORANGE_0800(COLOR_ORANGE_0800),

    /** Orange 900 */
    ORANGE_0900(COLOR_ORANGE_0900),

    /** Orange A100 */
    ORANGE_A100(COLOR_ORANGE_A100),

    /** Orange A200 */
    ORANGE_A200(COLOR_ORANGE_A200),

    /** Orange A400 */
    ORANGE_A400(COLOR_ORANGE_A400),

    /** Orange A700 */
    ORANGE_A700(COLOR_ORANGE_A700)

    ;

    override val argb : Int = this.color

    override val lighter : Orange
        get() = Orange.values()[max(0, this.ordinal - 1)]

    override val darker : Orange
        get() = Orange.values()[min(Orange.values().size - 1, this.ordinal + 1)]

    override val lightest : Orange get() = Orange.ORANGE_0050

    override val darkest : Orange get() = Orange.ORANGE_A700

    override val representative : Orange get() = Orange.ORANGE_0500
}
