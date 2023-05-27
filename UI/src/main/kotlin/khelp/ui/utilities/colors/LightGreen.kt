package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_LIGHT_GREEN_0050
import khelp.ui.utilities.COLOR_LIGHT_GREEN_0100
import khelp.ui.utilities.COLOR_LIGHT_GREEN_0200
import khelp.ui.utilities.COLOR_LIGHT_GREEN_0300
import khelp.ui.utilities.COLOR_LIGHT_GREEN_0400
import khelp.ui.utilities.COLOR_LIGHT_GREEN_0500
import khelp.ui.utilities.COLOR_LIGHT_GREEN_0600
import khelp.ui.utilities.COLOR_LIGHT_GREEN_0700
import khelp.ui.utilities.COLOR_LIGHT_GREEN_0800
import khelp.ui.utilities.COLOR_LIGHT_GREEN_0900
import khelp.ui.utilities.COLOR_LIGHT_GREEN_A100
import khelp.ui.utilities.COLOR_LIGHT_GREEN_A200
import khelp.ui.utilities.COLOR_LIGHT_GREEN_A400
import khelp.ui.utilities.COLOR_LIGHT_GREEN_A700
import kotlin.math.max
import kotlin.math.min

enum class LightGreen(private val color : Int) : BaseColor<LightGreen>
{
    /** Light_green 50 */
    LIGHT_GREEN_0050(COLOR_LIGHT_GREEN_0050),

    /** Light_green 100 */
    LIGHT_GREEN_0100(COLOR_LIGHT_GREEN_0100),

    /** Light_green 200 */
    LIGHT_GREEN_0200(COLOR_LIGHT_GREEN_0200),

    /** Light_green 300 */
    LIGHT_GREEN_0300(COLOR_LIGHT_GREEN_0300),

    /** Light_green 400 */
    LIGHT_GREEN_0400(COLOR_LIGHT_GREEN_0400),

    /** Light_green 500 : Reference */
    LIGHT_GREEN_0500(COLOR_LIGHT_GREEN_0500),

    /** Light_green 600 */
    LIGHT_GREEN_0600(COLOR_LIGHT_GREEN_0600),

    /** Light_green 700 */
    LIGHT_GREEN_0700(COLOR_LIGHT_GREEN_0700),

    /** Light_green 800 */
    LIGHT_GREEN_0800(COLOR_LIGHT_GREEN_0800),

    /** Light_green 900 */
    LIGHT_GREEN_0900(COLOR_LIGHT_GREEN_0900),

    /** Light_green A100 */
    LIGHT_GREEN_A100(COLOR_LIGHT_GREEN_A100),

    /** Light_green A200 */
    LIGHT_GREEN_A200(COLOR_LIGHT_GREEN_A200),

    /** Light_green A400 */
    LIGHT_GREEN_A400(COLOR_LIGHT_GREEN_A400),

    /** Light_green A700 */
    LIGHT_GREEN_A700(COLOR_LIGHT_GREEN_A700)

    ;

    override val argb : Int = this.color

    override val lighter : LightGreen
        get() = LightGreen.values()[max(0, this.ordinal - 1)]

    override val darker : LightGreen
        get() = LightGreen.values()[min(LightGreen.values().size - 1, this.ordinal + 1)]

    override val lightest : LightGreen get() = LightGreen.LIGHT_GREEN_0050

    override val darkest : LightGreen get() = LightGreen.LIGHT_GREEN_A700

    override val representative : LightGreen get() = LightGreen.LIGHT_GREEN_0500
}
