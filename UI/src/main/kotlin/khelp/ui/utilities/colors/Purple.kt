package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_PURPLE_0050
import khelp.ui.utilities.COLOR_PURPLE_0100
import khelp.ui.utilities.COLOR_PURPLE_0200
import khelp.ui.utilities.COLOR_PURPLE_0300
import khelp.ui.utilities.COLOR_PURPLE_0400
import khelp.ui.utilities.COLOR_PURPLE_0500
import khelp.ui.utilities.COLOR_PURPLE_0600
import khelp.ui.utilities.COLOR_PURPLE_0700
import khelp.ui.utilities.COLOR_PURPLE_0800
import khelp.ui.utilities.COLOR_PURPLE_0900
import khelp.ui.utilities.COLOR_PURPLE_A100
import khelp.ui.utilities.COLOR_PURPLE_A200
import khelp.ui.utilities.COLOR_PURPLE_A400
import khelp.ui.utilities.COLOR_PURPLE_A700
import kotlin.math.max
import kotlin.math.min

enum class Purple(private val color : Int) : BaseColor<Purple>
{
    /** Purple 50 */
    PURPLE_0050(COLOR_PURPLE_0050),

    /** Purple 100 */
    PURPLE_0100(COLOR_PURPLE_0100),

    /** Purple 200 */
    PURPLE_0200(COLOR_PURPLE_0200),

    /** Purple 300 */
    PURPLE_0300(COLOR_PURPLE_0300),

    /** Purple 400 */
    PURPLE_0400(COLOR_PURPLE_0400),

    /** Purple 500 : Reference */
    PURPLE_0500(COLOR_PURPLE_0500),

    /** Purple 600 */
    PURPLE_0600(COLOR_PURPLE_0600),

    /** Purple 700 */
    PURPLE_0700(COLOR_PURPLE_0700),

    /** Purple 800 */
    PURPLE_0800(COLOR_PURPLE_0800),

    /** Purple 900 */
    PURPLE_0900(COLOR_PURPLE_0900),

    /** Purple A100 */
    PURPLE_A100(COLOR_PURPLE_A100),

    /** Purple A200 */
    PURPLE_A200(COLOR_PURPLE_A200),

    /** Purple A400 */
    PURPLE_A400(COLOR_PURPLE_A400),

    /** Purple A700 */
    PURPLE_A700(COLOR_PURPLE_A700)

    ;

    override val argb : Int = this.color

    override val lighter : Purple
        get() = Purple.values()[max(0, this.ordinal - 1)]

    override val darker : Purple
        get() = Purple.values()[min(Purple.values().size - 1, this.ordinal + 1)]

    override val lightest : Purple get() = Purple.PURPLE_0050

    override val darkest : Purple get() = Purple.PURPLE_A700

    override val representative : Purple get() = Purple.PURPLE_0500
}
