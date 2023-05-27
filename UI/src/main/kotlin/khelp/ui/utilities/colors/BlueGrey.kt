package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_BLUE_GREY_0050
import khelp.ui.utilities.COLOR_BLUE_GREY_0100
import khelp.ui.utilities.COLOR_BLUE_GREY_0200
import khelp.ui.utilities.COLOR_BLUE_GREY_0300
import khelp.ui.utilities.COLOR_BLUE_GREY_0400
import khelp.ui.utilities.COLOR_BLUE_GREY_0500
import khelp.ui.utilities.COLOR_BLUE_GREY_0600
import khelp.ui.utilities.COLOR_BLUE_GREY_0700
import khelp.ui.utilities.COLOR_BLUE_GREY_0800
import khelp.ui.utilities.COLOR_BLUE_GREY_0900
import kotlin.math.max
import kotlin.math.min

enum class BlueGrey(private val color : Int) : BaseColor<BlueGrey>
{
    /** Blue_grey 50 */
    BLUE_GREY_0050(COLOR_BLUE_GREY_0050),

    /** Blue_grey 100 */
    BLUE_GREY_0100(COLOR_BLUE_GREY_0100),

    /** Blue_grey 200 */
    BLUE_GREY_0200(COLOR_BLUE_GREY_0200),

    /** Blue_grey 300 */
    BLUE_GREY_0300(COLOR_BLUE_GREY_0300),

    /** Blue_grey 400 */
    BLUE_GREY_0400(COLOR_BLUE_GREY_0400),

    /** Blue_grey 500 : Reference */
    BLUE_GREY_0500(COLOR_BLUE_GREY_0500),

    /** Blue_grey 600 */
    BLUE_GREY_0600(COLOR_BLUE_GREY_0600),

    /** Blue_grey 700 */
    BLUE_GREY_0700(COLOR_BLUE_GREY_0700),

    /** Blue_grey 800 */
    BLUE_GREY_0800(COLOR_BLUE_GREY_0800),

    /** Blue_grey 900 */
    BLUE_GREY_0900(COLOR_BLUE_GREY_0900)

    ;

    override val argb : Int = this.color

    override val lighter : BlueGrey
        get() = BlueGrey.values()[max(0, this.ordinal - 1)]

    override val darker : BlueGrey
        get() = BlueGrey.values()[min(BlueGrey.values().size - 1, this.ordinal + 1)]

    override val lightest : BlueGrey get() = BlueGrey.BLUE_GREY_0050

    override val darkest : BlueGrey get() = BlueGrey.BLUE_GREY_0900

    override val representative : BlueGrey get() = BlueGrey.BLUE_GREY_0500
}
