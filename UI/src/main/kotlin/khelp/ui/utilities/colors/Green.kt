package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_GREEN_0050
import khelp.ui.utilities.COLOR_GREEN_0100
import khelp.ui.utilities.COLOR_GREEN_0200
import khelp.ui.utilities.COLOR_GREEN_0300
import khelp.ui.utilities.COLOR_GREEN_0400
import khelp.ui.utilities.COLOR_GREEN_0500
import khelp.ui.utilities.COLOR_GREEN_0600
import khelp.ui.utilities.COLOR_GREEN_0700
import khelp.ui.utilities.COLOR_GREEN_0800
import khelp.ui.utilities.COLOR_GREEN_0900
import khelp.ui.utilities.COLOR_GREEN_A100
import khelp.ui.utilities.COLOR_GREEN_A200
import khelp.ui.utilities.COLOR_GREEN_A400
import khelp.ui.utilities.COLOR_GREEN_A700
import kotlin.math.max
import kotlin.math.min

enum class Green(private val color : Int) : BaseColor<Green>
{
    /** Green 50 */
    GREEN_0050(COLOR_GREEN_0050),

    /** Green 100 */
    GREEN_0100(COLOR_GREEN_0100),

    /** Green 200 */
    GREEN_0200(COLOR_GREEN_0200),

    /** Green 300 */
    GREEN_0300(COLOR_GREEN_0300),

    /** Green 400 */
    GREEN_0400(COLOR_GREEN_0400),

    /** Green 500 : Reference */
    GREEN_0500(COLOR_GREEN_0500),

    /** Green 600 */
    GREEN_0600(COLOR_GREEN_0600),

    /** Green 700 */
    GREEN_0700(COLOR_GREEN_0700),

    /** Green 800 */
    GREEN_0800(COLOR_GREEN_0800),

    /** Green 900 */
    GREEN_0900(COLOR_GREEN_0900),

    /** Green A100 */
    GREEN_A100(COLOR_GREEN_A100),

    /** Green A200 */
    GREEN_A200(COLOR_GREEN_A200),

    /** Green A400 */
    GREEN_A400(COLOR_GREEN_A400),

    /** Green A700 */
    GREEN_A700(COLOR_GREEN_A700)

    ;

    override val argb : Int = this.color

    override val lighter : Green
        get() = Green.values()[max(0, this.ordinal - 1)]

    override val darker : Green
        get() = Green.values()[min(Green.values().size - 1, this.ordinal + 1)]

    override val lightest : Green get() = Green.GREEN_0050

    override val darkest : Green get() = Green.GREEN_A700

    override val representative : Green get() = Green.GREEN_0500
}
