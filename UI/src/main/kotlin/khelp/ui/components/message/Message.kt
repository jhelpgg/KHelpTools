package khelp.ui.components.message

import khelp.resources.ResourcesText
import khelp.resources.defaultTexts
import khelp.thread.TaskContext

class Message(var resourcesText : ResourcesText = defaultTexts,
              var keyTitle : String = "",
              var keyText : String = "",
              var messageType : MessageType = MessageType.MESSAGE,
              var messageButtons : MessageButtons = MessageButtons.OK,
              var taskContext : TaskContext = TaskContext.INDEPENDENT,
              var clickOn : (MessageAction) -> Unit = {})
