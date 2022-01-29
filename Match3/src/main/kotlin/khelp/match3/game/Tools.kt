package khelp.match3.game

import khelp.ui.utilities.simulateKeyPress
import java.awt.event.KeyEvent

fun closeGame()
{
    simulateKeyPress(KeyEvent.VK_ESCAPE)
}
