package khelp.ui

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dialog
import java.awt.FlowLayout
import java.awt.Font
import java.awt.LayoutManager
import java.awt.geom.Ellipse2D
import java.util.Locale
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTree
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import javax.swing.tree.TreePath
import khelp.io.ClassSource
import khelp.resources.Resources
import khelp.thread.TaskContext
import khelp.ui.components.ColorComponent
import khelp.ui.components.EmptyComponent
import khelp.ui.components.JLabel
import khelp.ui.components.message.MessageButtons
import khelp.ui.components.message.MessageType
import khelp.ui.components.style.StyledButton
import khelp.ui.components.style.StyledLabel
import khelp.ui.dsl.borderLayout
import khelp.ui.dsl.constraintLayout
import khelp.ui.dsl.frame
import khelp.ui.dsl.tableLayout
import khelp.ui.extensions.addSubTitle
import khelp.ui.extensions.addTitle
import khelp.ui.extensions.drawText
import khelp.ui.game.GameFrame
import khelp.ui.game.GameImage
import khelp.ui.layout.constraints.ConstraintsSize
import khelp.ui.paint.CornerGradientPaint
import khelp.ui.pyramid.PyramidModel
import khelp.ui.pyramid.PyramidTreCellRenderer
import khelp.ui.style.ImageTextRelativePosition
import khelp.ui.style.StyleImageWithText
import khelp.ui.style.StyleImageWithTextClickable
import khelp.ui.style.background.StyleBackgroundPaint
import khelp.ui.style.shape.StyleShapeRoundRectangle
import khelp.ui.style.shape.StyleShapeSausage
import khelp.ui.utilities.CHARACTER_ESCAPE
import khelp.ui.utilities.TITLE_FONT
import khelp.ui.utilities.WARNING_IMAGE_16
import khelp.ui.utilities.createKeyStroke
import khelp.ui.utilities.initializeGUI
import khelp.utilities.collections.tree.Tree
import khelp.utilities.log.debug
import khelp.utilities.log.mark
import khelp.utilities.math.random

class Main

fun main()
{
    initializeGUI()
    testTree2()
}

private fun test1()
{
    val resources = Resources(ClassSource(Main::class.java))
    val resourcesText = resources.resourcesText("exampleTexts")

    val exitImage = GameImage(32, 32)
    exitImage.clear(Color.RED)
    exitImage.drawPercent { percentGraphics ->
        percentGraphics.color = Color.BLUE
        percentGraphics.fillRectangle(0.0, 0.0, 0.5, 0.5)
        percentGraphics.fillRectangle(0.5, 0.5, 0.5, 0.5)
    }

    val styleImageWithText = StyleImageWithText()
    val styledLabel = StyledLabel("textExample", resourcesText, styleImageWithText)
    styledLabel.image = exitImage
    styleImageWithText.shape = StyleShapeSausage
    styleImageWithText.textAlignment = TextAlignment.LEFT
    styleImageWithText.imageTextRelativePosition = ImageTextRelativePosition.IMAGE_LEFT_OF_TEXT

    val styleImageWithTextClickable = StyleImageWithTextClickable()
    val styledButton = StyledButton("titleExample", resourcesText, styleImageWithTextClickable)
    styledButton.onClick(TaskContext.INDEPENDENT) { mark("CLICK") }
    styleImageWithTextClickable.shape = StyleShapeRoundRectangle
    styleImageWithTextClickable.textAlignment = TextAlignment.CENTER
    styleImageWithText.background =
        StyleBackgroundPaint(CornerGradientPaint(Color.BLUE, Color.RED, Color.GREEN, Color.WHITE))

    frame {
        message("alert", resourcesText) {
            keyTitle = "titleExample"
            keyText = "textExample"
            messageType = MessageType.WARNING
            messageButtons = MessageButtons.OK
            clickOn = { action -> debug("Click on : ", action) }
        }

        message("information", resourcesText) {
            keyTitle = "titleExample"
            keyText = "textExample"
            messageType = MessageType.INFORMATION
            messageButtons = MessageButtons.YES_NO
            clickOn = { action -> debug("Click on : ", action) }
        }

        message("save", resourcesText) {
            keyTitle = "titleExample"
            keyText = "textExample"
            messageType = MessageType.QUESTION
            messageButtons = MessageButtons.SAVE_SAVE_AS_CANCEL
            clickOn = { action -> debug("Click on : ", action) }
        }

        action("closeDialog", "ok", resourcesText) {
            image = WARNING_IMAGE_16
            onClick(TaskContext.INDEPENDENT) { hideDialog("DialogTest") }
        }

        dialog("DialogTest") {
            isUndecorated = true
            modalityType = Dialog.ModalityType.APPLICATION_MODAL
            title = "Dialog"
            borderLayout {
                center(JLabel("Dialog Center", JLabel.CENTER))
                pageStart(JLabel("Dialog Page Start", JLabel.CENTER))
                pageEnd(JButton(action("closeDialog")))
                lineStart(JLabel("Dialog Line Start", JLabel.CENTER))
                lineEnd(JLabel("Dialog Line End", JLabel.CENTER))
            }
        }

        action("EXIT", "keyExit", resourcesText) {
            shortcut = createKeyStroke(CHARACTER_ESCAPE)
            image = exitImage
            keyToolTip = "keyExitToolTip"
            onClick(TaskContext.INDEPENDENT) { close() }
        }

        action("ENGLISH", "english", resourcesText) {
            onClick(TaskContext.INDEPENDENT) { Resources.languageObservableData.value(Locale.ENGLISH) }
        }

        action("FRENCH", "french", resourcesText) {
            onClick(TaskContext.INDEPENDENT) { Resources.languageObservableData.value(Locale.FRENCH) }
        }

        menuBar {
            menu("Menu") {
                "Show" { showDialog("DialogTest") }
                "Hide" { hideDialog("DialogTest") }
                separator
                +action("ENGLISH")
                +action("FRENCH")
                separator
                "Alert" { showMessage("alert") }
                "Information" { showMessage("information") }
                "Save" { showMessage("save") }
                separator
                +action("EXIT")
            }
        }

        borderLayout(8, 8) {
            center {
                tableLayout {
                    khelp.ui.components.JLabel("textExample", resourcesText)
                        .cell(1, 1)
                    ColorComponent(Color.BLUE, "color", resourcesText).cell(2, 2, 2, 2)
                    panel(1, 4, 3, 3) {
                        borderLayout {
                            pageStart(
                                ColorComponent(Color.BLACK, "color", resourcesText).addTitle("black", resourcesText))
                            lineStart(ColorComponent(Color.GREEN, "color", resourcesText))
                            center(ColorComponent(Color.YELLOW, "color", resourcesText).addSubTitle("yellow",
                                                                                                    resourcesText))
                            lineEnd(ColorComponent(Color.GREEN, "color", resourcesText))
                            pageEnd(styledLabel)
                        }
                    }
                }
            }

            pageStart(JLabel("- Page Start -", JLabel.CENTER))
            pageEnd(styledButton)
            lineStart(JLabel("textExample", resourcesText))
            lineEnd(JLabel("htmlExample", resourcesText))
        }
    }

    val black = ColorComponent(Color.BLACK, "color", resourcesText)
    val red = ColorComponent(Color.RED, "color", resourcesText)
    val green = ColorComponent(Color.GREEN, "color", resourcesText)

    frame {
        constraintLayout {
            EmptyComponent()("middle") {
                horizontalSize = ConstraintsSize.WRAPPED
                verticalSize = ConstraintsSize.WRAPPED
                leftAtParent
                rightAtParent
                topAtParent
                bottomAtParent
            }

            EmptyComponent()("quart") {
                horizontalSize = ConstraintsSize.WRAPPED
                verticalSize = ConstraintsSize.WRAPPED
                leftAtParent
                rightAtLeft = "middle"
                topAtParent
                bottomAtTop = "middle"
            }

            ColorComponent(Color.BLUE, "color", resourcesText)("blue") {
                horizontalSize = ConstraintsSize.EXPANDED
                verticalSize = ConstraintsSize.WRAPPED
                leftAtRight = "quart"
                rightAtLeft = "green"
                topAtParent
                bottomFree
            }

            green("green") {
                horizontalSize = ConstraintsSize.WRAPPED
                verticalSize = ConstraintsSize.EXPANDED
                leftFree
                rightAtParent
                topAtParent
                bottomAtTop = "yellow"
            }

            ColorComponent(Color.YELLOW, "color", resourcesText)("yellow") {
                horizontalSize = ConstraintsSize.EXPANDED
                verticalSize = ConstraintsSize.WRAPPED
                leftAtParent
                rightAtParent
                topFree
                bottomAtParent
            }

            red("red") {
                horizontalSize = ConstraintsSize.WRAPPED
                verticalSize = ConstraintsSize.EXPANDED
                leftAtParent
                rightFree
                topAtBottom = "blue"
                bottomAtTop = "yellow"
            }

            black("black") {
                horizontalSize = ConstraintsSize.EXPANDED
                verticalSize = ConstraintsSize.EXPANDED
                topAtBottom = "blue"
                bottomAtTop = "yellow"
                leftAtRight = "red"
                rightAtLeft = "green"
            }
        }
    }
}

private fun test2()
{
    val frame = GameFrame.fullScreenFrame()
    frame.isVisible = true

    val image = frame.gameComponent.gameImage
    image.clear(Color.WHITE)

    image.draw { graphics ->
        graphics.color = Color.GREEN
        graphics.fillRect(0, 0, image.width, image.height)
        graphics.color = Color.BLACK
        graphics.drawRect(10, 10, 500, 500)
        graphics.color = Color.CYAN
        graphics.fillOval(50, 50, 450, 450)
        graphics.color = Color.BLACK
        graphics.draw(Ellipse2D.Double(50.0, 50.0, 450.0, 450.0))
        graphics.font = Font("Arial", Font.BOLD, 24)
        graphics.drawText(11, 11, "Hello!\nThanks for the fish!", TextAlignment.RIGHT)
    }

    image.drawPercent { percentGraphics ->
        percentGraphics.color = Color(128, 128, 128, 128)
        percentGraphics.fillRectangle(0.25, 0.25, 0.5, 0.5)
    }
}

private fun testTree()
{
    val resources = Resources(ClassSource(Main::class.java))
    val resourcesText = resources.resourcesText("exampleTexts")
    val pyramidModel = PyramidModel()
    val tree = JTree(pyramidModel)
    val cellRenderer = PyramidTreCellRenderer()
    tree.setCellRenderer(cellRenderer)
    val button = JButton("Add child")
    button.font = TITLE_FONT.font
    button.requestFocus()
    button.addActionListener {
        button.isEnabled = false
        pyramidModel.addRandomChild().and {
            button.isEnabled = true
            button.requestFocus()
        }
    }
    pyramidModel.addTreeModelListener(object : TreeModelListener
                                      {
                                          override fun treeNodesChanged(treeModelEvent : TreeModelEvent)
                                          {
                                              var path = treeModelEvent.treePath?.path

                                              if (path != null)
                                              {
                                                  val child = treeModelEvent.children?.get(0)

                                                  if (child != null)
                                                  {
                                                      path += child
                                                  }

                                                  tree.selectionPath = TreePath(path)
                                                  tree.scrollPathToVisible(TreePath(path))
                                              }
                                          }

                                          override fun treeNodesInserted(treeModelEvent : TreeModelEvent)
                                          {
                                              treeModelEvent.treePath?.let { path ->
                                                  tree.expandPath(path)
                                                  val pathToChild = TreePath(path.path + treeModelEvent.children[0])
                                                  tree.selectionPath = pathToChild
                                                  tree.scrollPathToVisible(pathToChild)
                                              }
                                          }

                                          override fun treeNodesRemoved(treeModelEvent : TreeModelEvent) = Unit

                                          override fun treeStructureChanged(treeModelEvent : TreeModelEvent) = Unit
                                      })

    frame(title = "Tree", decorated = true, full = true) {
        borderLayout {
            center(JScrollPane(tree))
            pageEnd(button)
        }
    }
}

private fun testTree2()
{
    val tree = Tree<Int>(0)

    for (count in 0 until 10_000)
    {
        var branch = randomBranch(tree)
        branch.addBranch(-1000)
        var sum = 1000
        var distributeIndex = 0

        while (branch.root.not() && distributeIndex < PyramidModel.distribute.size)
        {
            branch.value += PyramidModel.distribute[distributeIndex]
            sum -= PyramidModel.distribute[distributeIndex]
            branch = branch.parent!!
            distributeIndex++
        }

        tree.value += sum
    }

    frame(title = "Tree", decorated = true, full = true) {
        borderLayout {
            center(JScrollPane(createPanel(tree) { value ->
                when
                {
                    value < 0  -> Color.RED
                    value == 0 -> Color.BLUE
                    else       -> Color.BLACK
                }
            }))
        }
    }
}

class MyPanel(layoutManager : LayoutManager) : JPanel(layoutManager)
{
    override fun getBaseline(width : Int, height : Int) : Int
    {
        return 1
    }
}

private fun <T : Any> createPanel(branch : Tree<T>, color : (T) -> Color = { Color.BLACK }) : JPanel
{
    val panel = MyPanel(BorderLayout())
    val label = JLabel(branch.value.toString(), JLabel.LEFT)
    label.foreground = color(branch.value)
    label.font = TITLE_FONT.font
    panel.add(label, BorderLayout.PAGE_START)

    if (branch.leaf.not())
    {
        val layout = FlowLayout(FlowLayout.CENTER, 0, 0)
        layout.alignOnBaseline = true
        val branchPanel = JPanel(layout)

        for (child in branch)
        {
            branchPanel.add(createPanel(child, color))
        }

        panel.add(branchPanel, BorderLayout.CENTER)
    }

    panel.border = BorderFactory.createLineBorder(Color.BLACK) // RoundedBorder()
    return panel
}

private fun <T : Any> randomBranch(root : Tree<T>) : Tree<T>
{
    var branch = root

    while (true)
    {
        if (branch.leaf)
        {
            return branch
        }

        val index = random(0, branch.numberBranches)

        if (index == branch.numberBranches)
        {
            return branch
        }

        branch = branch[index]
    }
}