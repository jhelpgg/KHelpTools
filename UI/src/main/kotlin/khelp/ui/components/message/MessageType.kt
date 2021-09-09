package khelp.ui.components.message

import khelp.ui.game.GameImage

enum class MessageType(val icon : () -> GameImage)
{
    MESSAGE({ khelp.ui.utilities.MESSAGE_IMAGE_64 }),
    INFORMATION({ khelp.ui.utilities.INFORMATION_IMAGE_64 }),
    IDEA({ khelp.ui.utilities.IDEA_IMAGE_64 }),
    QUESTION({ khelp.ui.utilities.QUESTION_IMAGE_64 }),
    WARNING({ khelp.ui.utilities.WARNING_IMAGE_64 }),
    ERROR({ khelp.ui.utilities.ERROR_IMAGE_64 })
}
