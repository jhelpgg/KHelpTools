package khelp.editor.ui

import khelp.editor.ui.patheditor.PathEditor
import khelp.engine3d.event.ActionCode
import khelp.engine3d.render.Node
import khelp.engine3d.render.Scene
import khelp.engine3d.render.Window3D
import khelp.engine3d.resource.TextureCache
import khelp.io.ClassSource
import khelp.resources.Resources
import khelp.thread.TaskContext
import khelp.ui.components.JHelpFrame
import khelp.ui.dsl.borderLayout
import khelp.ui.dsl.constraintLayout
import khelp.ui.dsl.frame
import khelp.ui.dsl.tableLayout
import khelp.ui.game.GameImage
import khelp.ui.layout.constraints.ConstraintsSize
import khelp.ui.utilities.CHARACTER_ESCAPE
import khelp.ui.utilities.createKeyStroke
import java.awt.BorderLayout
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

object Editor
{
    private const val NUMBER_CELL_WIDTH = 10
    private const val NUMBER_CELL_HEIGHT = 10
    private const val NUMBER_CELL_LEFT = 2
    private const val NUMBER_CELL_BOTTOM = 3
    private const val NUMBER_CELL_3D_WIDTH = NUMBER_CELL_WIDTH - NUMBER_CELL_LEFT
    private const val NUMBER_CELL_3D_HEIGHT = NUMBER_CELL_HEIGHT - 1 - NUMBER_CELL_BOTTOM

    private val allowToClose = AtomicBoolean(false)
    private val resources = Resources(ClassSource(Editor::class.java))
    private val resourcesText = this.resources.resourcesText("texts/texts")
    private val exitIcon = GameImage.loadThumbnail("images/exit.png", this.resources, 32, 32)
    private val frame : JHelpFrame
    private lateinit var panelMenuBar : JPanel
    private lateinit var panelToolLeft : JPanel
    private lateinit var panelBottom : JPanel
    private lateinit var scene : Scene
    private var manipulatedNode : Node? = null
    val informationText = JTextArea()

    init
    {
        TextureCache.resources = this.resources
        this.frame = frame("Editor", decorated = false, full = true) {
            action("exit", "exit", resourcesText) {
                keyToolTip = "exitToolTip"
                shortcut = createKeyStroke(CHARACTER_ESCAPE)
                image = exitIcon
                this.onClick(TaskContext.INDEPENDENT) {
                    allowToClose.set(true)
                    close()
                }
            }

            menuBar {
                menu("File") {
                    + action("exit")
                }
            }

            tableLayout {
                marginTop = 2
                marginBottom = 2
                marginLeft = 2
                marginRight = 2
                panel(0, 0, NUMBER_CELL_WIDTH, 1) {
                    panelMenuBar = this
                    constraintLayout {
                        JButton(action("exit"))("buttonExit") {
                            verticalSize = ConstraintsSize.WRAPPED
                            horizontalSize = ConstraintsSize.WRAPPED
                            marginRight = 8
                            topAtParent
                            bottomAtParent
                            leftFree
                            rightAtParent
                        }
                    }
                }
                panel(0, 1, NUMBER_CELL_LEFT, NUMBER_CELL_3D_HEIGHT) {
                    panelToolLeft = this
                    layout = BorderLayout()
                }
                panel(0, 1 + NUMBER_CELL_3D_HEIGHT, NUMBER_CELL_WIDTH, NUMBER_CELL_BOTTOM) {
                    panelBottom = this
                    borderLayout {
                        center(JScrollPane(informationText,
                                           JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                           JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS))
                    }
                }
                Component3D.cell(NUMBER_CELL_LEFT, 1, NUMBER_CELL_3D_WIDTH, NUMBER_CELL_3D_HEIGHT)
            }
        }

        Component3D.window3D.and { window3D -> this.manipulate3D(window3D) }
    }

    private fun manipulate3D(window3D : Window3D)
    {
        this.frame.canCloseNow = {
            if (this.allowToClose.get())
            {
                window3D.close()
                true
            }
            else
            {
                false
            }
        }

        window3D.canCloseNow = this.allowToClose::get
        this.scene = window3D.scene

        window3D.actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT, this::actionOn3D)

        val pathEditor = PathEditor()
        pathEditor.applyInside(this.panelToolLeft)
        val revolution = pathEditor.revolution
        this.manipulatedNode = revolution
        this.scene.root.addChild(revolution)

        this.frame.isVisible = false
        this.frame.isVisible = true
    }

    private fun actionOn3D(actionsCode : List<ActionCode>)
    {
        for (actionCode in actionsCode)
        {
            when (actionCode)
            {
                ActionCode.ACTION_EXIT  ->
                {
                    this.allowToClose.set(true)
                    this.frame.close()
                }
                ActionCode.ACTION_UP    -> manipulatedNode?.let { node -> node.angleX += 1f }
                ActionCode.ACTION_DOWN  -> manipulatedNode?.let { node -> node.angleX -= 1f }
                ActionCode.ACTION_LEFT  -> manipulatedNode?.let { node -> node.angleY += 1f }
                ActionCode.ACTION_RIGHT -> manipulatedNode?.let { node -> node.angleY -= 1f }
                else                    -> Unit
            }
        }
    }
}