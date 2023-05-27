package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_BLACK
import khelp.ui.utilities.COLOR_GREY_0050
import khelp.ui.utilities.COLOR_GREY_0100
import khelp.ui.utilities.COLOR_GREY_0200
import khelp.ui.utilities.COLOR_GREY_0300
import khelp.ui.utilities.COLOR_GREY_0400
import khelp.ui.utilities.COLOR_GREY_0500
import khelp.ui.utilities.COLOR_GREY_0600
import khelp.ui.utilities.COLOR_GREY_0700
import khelp.ui.utilities.COLOR_GREY_0800
import khelp.ui.utilities.COLOR_GREY_0900
import khelp.ui.utilities.COLOR_WHITE
import kotlin.math.max
import kotlin.math.min

enum class Grey(private val color : Int) : BaseColor<Grey>
{
    /** White */
    WHITE(COLOR_WHITE),

    /** Grey 50 */
    GREY_0050(COLOR_GREY_0050),

    /** Grey 100 */
    GREY_0100(COLOR_GREY_0100),

    /** Grey 200 */
    GREY_0200(COLOR_GREY_0200),

    /** Grey 300 */
    GREY_0300(COLOR_GREY_0300),

    /** Grey 400 */
    GREY_0400(COLOR_GREY_0400),

    /** Grey 500 : Reference */
    GREY_0500(COLOR_GREY_0500),

    /** Grey 600 */
    GREY_0600(COLOR_GREY_0600),

    /** Grey 700 */
    GREY_0700(COLOR_GREY_0700),

    /** Grey 800 */
    GREY_0800(COLOR_GREY_0800),

    /** Grey 900 */
    GREY_0900(COLOR_GREY_0900),

    /** Black */
    BLACK(COLOR_BLACK)

    ;

    override val argb : Int = this.color

    override val lighter : Grey
        get() = Grey.values()[max(0, this.ordinal - 1)]

    override val darker : Grey
        get() = Grey.values()[min(Grey.values().size - 1, this.ordinal + 1)]

    override val lightest : Grey get() = Grey.WHITE

    override val darkest : Grey get() = Grey.BLACK

    override val representative : Grey get() = Grey.GREY_0500
}
