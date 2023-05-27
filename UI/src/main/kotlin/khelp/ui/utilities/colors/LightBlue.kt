package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_LIGHT_BLUE_0050
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0100
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0200
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0300
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0400
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0500
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0600
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0700
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0800
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0900
import khelp.ui.utilities.COLOR_LIGHT_BLUE_A100
import khelp.ui.utilities.COLOR_LIGHT_BLUE_A200
import khelp.ui.utilities.COLOR_LIGHT_BLUE_A400
import khelp.ui.utilities.COLOR_LIGHT_BLUE_A700
import kotlin.math.max
import kotlin.math.min

enum class LightBlue(private val color : Int) : BaseColor<LightBlue>
{
    /** Light_blue 50 */
    LIGHT_BLUE_0050(COLOR_LIGHT_BLUE_0050),

    /** Light_blue 100 */
    LIGHT_BLUE_0100(COLOR_LIGHT_BLUE_0100),

    /** Light_blue 200 */
    LIGHT_BLUE_0200(COLOR_LIGHT_BLUE_0200),

    /** Light_blue 300 */
    LIGHT_BLUE_0300(COLOR_LIGHT_BLUE_0300),

    /** Light_blue 400 */
    LIGHT_BLUE_0400(COLOR_LIGHT_BLUE_0400),

    /** Light_blue 500 : Reference */
    LIGHT_BLUE_0500(COLOR_LIGHT_BLUE_0500),

    /** Light_blue 600 */
    LIGHT_BLUE_0600(COLOR_LIGHT_BLUE_0600),

    /** Light_blue 700 */
    LIGHT_BLUE_0700(COLOR_LIGHT_BLUE_0700),

    /** Light_blue 800 */
    LIGHT_BLUE_0800(COLOR_LIGHT_BLUE_0800),

    /** Light_blue 900 */
    LIGHT_BLUE_0900(COLOR_LIGHT_BLUE_0900),

    /** Light_blue A100 */
    LIGHT_BLUE_A100(COLOR_LIGHT_BLUE_A100),

    /** Light_blue A200 */
    LIGHT_BLUE_A200(COLOR_LIGHT_BLUE_A200),

    /** Light_blue A400 */
    LIGHT_BLUE_A400(COLOR_LIGHT_BLUE_A400),

    /** Light_blue A700 */
    LIGHT_BLUE_A700(COLOR_LIGHT_BLUE_A700)

    ;

    override val argb : Int = this.color

    override val lighter : LightBlue
        get() = LightBlue.values()[max(0, this.ordinal - 1)]

    override val darker : LightBlue
        get() = LightBlue.values()[min(LightBlue.values().size - 1, this.ordinal + 1)]

    override val lightest : LightBlue get() = LightBlue.LIGHT_BLUE_0050

    override val darkest : LightBlue get() = LightBlue.LIGHT_BLUE_A700

    override val representative : LightBlue get() = LightBlue.LIGHT_BLUE_0500
}
