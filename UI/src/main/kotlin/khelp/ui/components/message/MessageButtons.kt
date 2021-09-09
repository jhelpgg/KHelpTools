package khelp.ui.components.message

enum class MessageButtons(val actions : List<MessageAction>)
{
    OK(listOf(MessageAction.OK)),
    OK_CANCEL(listOf(MessageAction.OK, MessageAction.CANCEL)),
    YES_NO(listOf(MessageAction.YES, MessageAction.NO)),
    YES_NO_CANCEL(listOf(MessageAction.YES, MessageAction.NO, MessageAction.CANCEL)),
    SAVE_CANCEL(listOf(MessageAction.SAVE, MessageAction.CANCEL)),
    SAVE_SAVE_AS_CANCEL(listOf(MessageAction.SAVE, MessageAction.SAVE_AS, MessageAction.CANCEL))
}
