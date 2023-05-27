package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_DEEP_ORANGE_0050
import khelp.ui.utilities.COLOR_DEEP_ORANGE_0100
import khelp.ui.utilities.COLOR_DEEP_ORANGE_0200
import khelp.ui.utilities.COLOR_DEEP_ORANGE_0300
import khelp.ui.utilities.COLOR_DEEP_ORANGE_0400
import khelp.ui.utilities.COLOR_DEEP_ORANGE_0500
import khelp.ui.utilities.COLOR_DEEP_ORANGE_0600
import khelp.ui.utilities.COLOR_DEEP_ORANGE_0700
import khelp.ui.utilities.COLOR_DEEP_ORANGE_0800
import khelp.ui.utilities.COLOR_DEEP_ORANGE_0900
import khelp.ui.utilities.COLOR_DEEP_ORANGE_A100
import khelp.ui.utilities.COLOR_DEEP_ORANGE_A200
import khelp.ui.utilities.COLOR_DEEP_ORANGE_A400
import khelp.ui.utilities.COLOR_DEEP_ORANGE_A700
import kotlin.math.max
import kotlin.math.min

enum class DeepOrange(private val color : Int) : BaseColor<DeepOrange>
{
    /** Deep_orange 50 */
    DEEP_ORANGE_0050(COLOR_DEEP_ORANGE_0050),

    /** Deep_orange 100 */
    DEEP_ORANGE_0100(COLOR_DEEP_ORANGE_0100),

    /** Deep_orange 200 */
    DEEP_ORANGE_0200(COLOR_DEEP_ORANGE_0200),

    /** Deep_orange 300 */
    DEEP_ORANGE_0300(COLOR_DEEP_ORANGE_0300),

    /** Deep_orange 400 */
    DEEP_ORANGE_0400(COLOR_DEEP_ORANGE_0400),

    /** Deep_orange 500 : Reference */
    DEEP_ORANGE_0500(COLOR_DEEP_ORANGE_0500),

    /** Deep_orange 600 */
    DEEP_ORANGE_0600(COLOR_DEEP_ORANGE_0600),

    /** Deep_orange 700 */
    DEEP_ORANGE_0700(COLOR_DEEP_ORANGE_0700),

    /** Deep_orange 800 */
    DEEP_ORANGE_0800(COLOR_DEEP_ORANGE_0800),

    /** Deep_orange 900 */
    DEEP_ORANGE_0900(COLOR_DEEP_ORANGE_0900),

    /** Deep_orange A100 */
    DEEP_ORANGE_A100(COLOR_DEEP_ORANGE_A100),

    /** Deep_orange A200 */
    DEEP_ORANGE_A200(COLOR_DEEP_ORANGE_A200),

    /** Deep_orange A400 */
    DEEP_ORANGE_A400(COLOR_DEEP_ORANGE_A400),

    /** Deep_orange A700 */
    DEEP_ORANGE_A700(COLOR_DEEP_ORANGE_A700)

    ;

    override val argb : Int = this.color

    override val lighter : DeepOrange
        get() = DeepOrange.values()[max(0, this.ordinal - 1)]

    override val darker : DeepOrange
        get() = DeepOrange.values()[min(DeepOrange.values().size - 1, this.ordinal + 1)]
    
    override val lightest : DeepOrange get() = DeepOrange.DEEP_ORANGE_0050

    override val darkest : DeepOrange get() = DeepOrange.DEEP_ORANGE_A700

    override val representative : DeepOrange get() = DeepOrange.DEEP_ORANGE_0500
}
