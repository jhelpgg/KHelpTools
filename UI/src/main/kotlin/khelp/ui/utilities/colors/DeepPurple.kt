package khelp.ui.utilities.colors

import khelp.ui.utilities.COLOR_DEEP_PURPLE_0050
import khelp.ui.utilities.COLOR_DEEP_PURPLE_0100
import khelp.ui.utilities.COLOR_DEEP_PURPLE_0200
import khelp.ui.utilities.COLOR_DEEP_PURPLE_0300
import khelp.ui.utilities.COLOR_DEEP_PURPLE_0400
import khelp.ui.utilities.COLOR_DEEP_PURPLE_0500
import khelp.ui.utilities.COLOR_DEEP_PURPLE_0600
import khelp.ui.utilities.COLOR_DEEP_PURPLE_0700
import khelp.ui.utilities.COLOR_DEEP_PURPLE_0800
import khelp.ui.utilities.COLOR_DEEP_PURPLE_0900
import khelp.ui.utilities.COLOR_DEEP_PURPLE_A100
import khelp.ui.utilities.COLOR_DEEP_PURPLE_A200
import khelp.ui.utilities.COLOR_DEEP_PURPLE_A400
import khelp.ui.utilities.COLOR_DEEP_PURPLE_A700
import kotlin.math.max
import kotlin.math.min

enum class DeepPurple(private val color : Int) : BaseColor<DeepPurple>
{
    /** Deep_purple 50 */
    DEEP_PURPLE_0050(COLOR_DEEP_PURPLE_0050),

    /** Deep_purple 100 */
    DEEP_PURPLE_0100(COLOR_DEEP_PURPLE_0100),

    /** Deep_purple 200 */
    DEEP_PURPLE_0200(COLOR_DEEP_PURPLE_0200),

    /** Deep_purple 300 */
    DEEP_PURPLE_0300(COLOR_DEEP_PURPLE_0300),

    /** Deep_purple 400 */
    DEEP_PURPLE_0400(COLOR_DEEP_PURPLE_0400),

    /** Deep_purple 500 : Reference */
    DEEP_PURPLE_0500(COLOR_DEEP_PURPLE_0500),

    /** Deep_purple 600 */
    DEEP_PURPLE_0600(COLOR_DEEP_PURPLE_0600),

    /** Deep_purple 700 */
    DEEP_PURPLE_0700(COLOR_DEEP_PURPLE_0700),

    /** Deep_purple 800 */
    DEEP_PURPLE_0800(COLOR_DEEP_PURPLE_0800),

    /** Deep_purple 900 */
    DEEP_PURPLE_0900(COLOR_DEEP_PURPLE_0900),

    /** Deep_purple A100 */
    DEEP_PURPLE_A100(COLOR_DEEP_PURPLE_A100),

    /** Deep_purple A200 */
    DEEP_PURPLE_A200(COLOR_DEEP_PURPLE_A200),

    /** Deep_purple A400 */
    DEEP_PURPLE_A400(COLOR_DEEP_PURPLE_A400),

    /** Deep_purple A700 */
    DEEP_PURPLE_A700(COLOR_DEEP_PURPLE_A700)

    ;

    override val argb : Int = this.color

    override val lighter : DeepPurple
        get() = DeepPurple.values()[max(0, this.ordinal - 1)]

    override val darker : DeepPurple
        get() = DeepPurple.values()[min(DeepPurple.values().size - 1, this.ordinal + 1)]

    override val lightest : DeepPurple get() = DeepPurple.DEEP_PURPLE_0050

    override val darkest : DeepPurple get() = DeepPurple.DEEP_PURPLE_A700

    override val representative : DeepPurple get() = DeepPurple.DEEP_PURPLE_0500
}
