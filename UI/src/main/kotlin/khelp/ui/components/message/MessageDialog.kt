package khelp.ui.components.message

import khelp.thread.TaskContext
import khelp.ui.border.RoundedBorder
import khelp.ui.dsl.constraintLayout
import khelp.ui.extensions.addLineBorder
import khelp.ui.extensions.drawImage
import khelp.ui.game.GameComponent
import khelp.ui.layout.constraints.ConstraintsSize
import khelp.ui.utilities.BUTTON_FONT
import khelp.ui.utilities.SUB_TITLE_FONT
import khelp.ui.utilities.TITLE_FONT
import khelp.ui.utilities.TRANSPARENT
import khelp.ui.utilities.centerOnScreen
import khelp.ui.utilities.packedSize
import java.awt.Color
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JTextArea

internal object MessageDialog : JDialog()
{
    private const val TITLE_FILLER = "8888888888888888888888888888888"
    private const val TEXT_FILLER =
        "88888888888888888888888888888888888888888888888888888888888888\n" +
        "88888888888888888888888888888888888888888888888888888888888888\n" +
        "88888888888888888888888888888888888888888888888888888888888888\n" +
        "88888888888888888888888888888888888888888888888888888888888888"
    private const val BUTTON_FILLER = "8888888888888888"

    private val title = JLabel("", JLabel.CENTER)
    private val text = JTextArea(4, 64)
    private val image = GameComponent(64, 64)
    private val buttonRight = JButton("")
    private val buttonMiddle = JButton("")
    private val buttonLeft = JButton("")
    private var messageButtons : MessageButtons = MessageButtons.OK
    private var taskContext : TaskContext = TaskContext.INDEPENDENT
    private var clickOn : (MessageAction) -> Unit = {}

    init
    {
        this.isUndecorated = true
        this.modalityType = ModalityType.APPLICATION_MODAL

        this.title.font = TITLE_FONT.font

        this.text.font = SUB_TITLE_FONT.font
        this.text.isEditable = false

        this.buttonLeft.font = BUTTON_FONT.font
        this.buttonLeft.border = RoundedBorder(Color.BLACK, 1)
        this.buttonLeft.addActionListener { this.clickOnButton(0) }

        this.buttonMiddle.font = BUTTON_FONT.font
        this.buttonMiddle.border = RoundedBorder(Color.BLACK, 1)
        this.buttonMiddle.addActionListener { this.clickOnButton(1) }

        this.buttonRight.font = BUTTON_FONT.font
        this.buttonRight.border = RoundedBorder(Color.BLACK, 1)
        this.buttonRight.addActionListener { this.clickOnButton(2) }

        this.constraintLayout {
            panel("main",
                  {
                      addLineBorder()

                      constraintLayout {
                          this@MessageDialog.image("icon") {
                              horizontalSize = ConstraintsSize.WRAPPED
                              verticalSize = ConstraintsSize.WRAPPED
                              topAtParent
                              leftAtParent
                              bottomAtTop = "buttonMiddle"
                              rightFree
                              marginLeft = 32
                              marginRight = 32
                          }

                          this@MessageDialog.title("title") {
                              horizontalSize = ConstraintsSize.EXPANDED
                              verticalSize = ConstraintsSize.WRAPPED
                              topAtParent
                              leftAtRight = "icon"
                              bottomFree
                              rightAtParent
                              marginTop = 16
                              marginLeft = 16
                              marginRight = 16
                              marginBottom = 8
                          }

                          this@MessageDialog.text("text") {
                              horizontalSize = ConstraintsSize.EXPANDED
                              verticalSize = ConstraintsSize.WRAPPED
                              topAtBottom = "title"
                              leftAtRight = "icon"
                              bottomAtTop = "buttonMiddle"
                              rightAtParent
                              marginRight = 32
                          }

                          this@MessageDialog.buttonLeft("buttonLeft") {
                              horizontalSize = ConstraintsSize.WRAPPED
                              verticalSize = ConstraintsSize.WRAPPED
                              topFree
                              leftAtParent
                              bottomAtParent
                              rightFree
                              marginLeft = 64
                              marginBottom = 16
                          }

                          this@MessageDialog.buttonMiddle("buttonMiddle") {
                              horizontalSize = ConstraintsSize.WRAPPED
                              verticalSize = ConstraintsSize.WRAPPED
                              topFree
                              leftAtRight = "buttonLeft"
                              bottomAtParent
                              rightAtLeft = "buttonRight"
                              marginBottom = 16
                          }

                          this@MessageDialog.buttonRight("buttonRight") {
                              horizontalSize = ConstraintsSize.WRAPPED
                              verticalSize = ConstraintsSize.WRAPPED
                              topFree
                              leftFree
                              bottomAtParent
                              rightAtParent
                              marginRight = 64
                              marginBottom = 16
                          }
                      }
                  })
            {
                horizontalSize = ConstraintsSize.EXPANDED
                verticalSize = ConstraintsSize.EXPANDED
                topAtParent
                leftAtParent
                bottomAtParent
                rightAtParent
            }
        }
    }

    fun showMessage(message : Message)
    {
        this.title.text = TITLE_FILLER
        this.text.text = TEXT_FILLER
        this.buttonLeft.text = BUTTON_FILLER
        this.buttonMiddle.text = BUTTON_FILLER
        this.buttonRight.text = BUTTON_FILLER
        packedSize(this)

        this.messageButtons = message.messageButtons
        this.taskContext = message.taskContext
        this.clickOn = message.clickOn

        val actions = message.messageButtons.actions

        when (actions.size)
        {
            1 ->
            {
                this.buttonLeft.isVisible = false
                this.buttonMiddle.text = message.resourcesText[actions[0].keyText]
                this.buttonMiddle.isVisible = true
                this.buttonRight.isVisible = false
            }
            2 ->
            {
                this.buttonLeft.text = message.resourcesText[actions[0].keyText]
                this.buttonLeft.isVisible = true
                this.buttonMiddle.isVisible = false
                this.buttonRight.text = message.resourcesText[actions[1].keyText]
                this.buttonRight.isVisible = true
            }
            3 ->
            {
                this.buttonLeft.text = message.resourcesText[actions[0].keyText]
                this.buttonLeft.isVisible = true
                this.buttonMiddle.text = message.resourcesText[actions[1].keyText]
                this.buttonMiddle.isVisible = true
                this.buttonRight.text = message.resourcesText[actions[2].keyText]
                this.buttonRight.isVisible = true
            }
        }

        if (message.keyTitle.isEmpty())
        {
            this.title.isVisible = false
        }
        else
        {
            this.title.isVisible = true
            this.title.text = message.resourcesText[message.keyTitle]
        }

        if (message.keyText.isEmpty())
        {
            this.text.isVisible = false
        }
        else
        {
            this.text.isVisible = true
            this.text.text = message.resourcesText[message.keyText]
        }

        this.image.gameImage.clear(TRANSPARENT)
        this.image.gameImage.draw { graphics2D -> graphics2D.drawImage(0, 0, message.messageType.icon()) }

        centerOnScreen(this)
        this.isVisible = true
    }

    private fun clickOnButton(buttonIndex : Int)
    {
        val actions = this.messageButtons.actions

        val messageAction =
            when (actions.size)
            {
                1    -> actions[0]
                2    -> if (buttonIndex == 0) actions[0] else actions[1]
                3    -> actions[buttonIndex]
                else -> MessageAction.OK
            }

        this.taskContext.parallel(messageAction) { action -> this.clickOn(action) }
        this.isVisible = false
    }
}
