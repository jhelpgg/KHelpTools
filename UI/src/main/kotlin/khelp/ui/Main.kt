package khelp.ui

import khelp.io.ClassSource
import khelp.resources.Resources
import khelp.thread.TaskContext
import khelp.ui.components.ColorComponent
import khelp.ui.components.JHelpFrame
import khelp.ui.components.JLabel
import khelp.ui.dsl.borderLayout
import khelp.ui.dsl.frame
import khelp.ui.dsl.tableLayout
import khelp.ui.extensions.addSubTitle
import khelp.ui.extensions.addTitle
import khelp.ui.game.GameImage
import khelp.ui.layout.constraints.BottomAtParent
import khelp.ui.layout.constraints.BottomFree
import khelp.ui.layout.constraints.BottomToTopOf
import khelp.ui.layout.constraints.ConstraintsLayout
import khelp.ui.layout.constraints.ConstraintsLayoutConstraint
import khelp.ui.layout.constraints.ConstraintsSize
import khelp.ui.layout.constraints.LeftAtParent
import khelp.ui.layout.constraints.LeftFree
import khelp.ui.layout.constraints.LeftToRightOf
import khelp.ui.layout.constraints.RightAtParent
import khelp.ui.layout.constraints.RightFree
import khelp.ui.layout.constraints.RightToLeftOf
import khelp.ui.layout.constraints.TopAtParent
import khelp.ui.layout.constraints.TopFree
import khelp.ui.layout.constraints.TopToBottomOf
import khelp.ui.utilities.CHARACTER_ESCAPE
import khelp.ui.utilities.createKeyStroke
import khelp.ui.utilities.initializeGUI
import java.awt.Color
import java.util.Locale
import javax.swing.JLabel

class Main

fun main()
{
    initializeGUI()

    val resources = Resources(ClassSource(Main::class.java))
    val resourcesText = resources.resourcesText("exampleTexts")
    val exitImage = GameImage(32, 32)
    exitImage.clear(Color.RED)
    exitImage.drawPercent { percentGraphics ->
        percentGraphics.color = Color.BLUE
        percentGraphics.fillRectangle(0.0, 0.0, 0.5, 0.5)
        percentGraphics.fillRectangle(0.5, 0.5, 0.5, 0.5)
    }

    val frame = frame {
        dialog("DialogTest") {
            title = "Dialog"
            borderLayout {
                center(JLabel("Dialog Center", JLabel.CENTER))
                pageStart(JLabel("Dialog Page Start", JLabel.CENTER))
                pageEnd(JLabel("Dialog Page End", JLabel.CENTER))
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
                + action("ENGLISH")
                + action("FRENCH")
                separator
                + action("EXIT")
            }
        }

        borderLayout(8, 8) {
            center {
                tableLayout {
                    khelp.ui.components.JLabel("textExample", resourcesText)
                        .cell(1, 1)
                    ColorComponent(Color.BLUE).cell(2, 2, 2, 2)
                    panel(1, 4, 3, 3) {
                        borderLayout {
                            pageStart(ColorComponent(Color.BLACK).addTitle("black", resourcesText))
                            lineStart(ColorComponent(Color.GREEN))
                            center(ColorComponent(Color.YELLOW).addSubTitle("yellow", resourcesText))
                            lineEnd(ColorComponent(Color.GREEN))
                            pageEnd(ColorComponent(Color.ORANGE))
                        }
                    }
                }
            }

            pageStart(JLabel("- Page Start -", JLabel.CENTER))
            pageEnd(JLabel("- Page End -", JLabel.CENTER))
            lineStart(JLabel("textExample", resourcesText))
            lineEnd(JLabel("htmlExample", resourcesText))
        }
    }


    val black = ColorComponent(Color.BLACK)
    val red = ColorComponent(Color.RED)
    val green = ColorComponent(Color.GREEN)
    val yellow = ColorComponent(Color.YELLOW)
    val blue = ColorComponent(Color.BLUE)
    val frame2 = JHelpFrame()
    frame2.layout = ConstraintsLayout()

    var constraintsLayoutConstraint = ConstraintsLayoutConstraint()
    constraintsLayoutConstraint.horizontalSize = ConstraintsSize.EXPANDED
    constraintsLayoutConstraint.verticalSize = ConstraintsSize.WRAPPED
    constraintsLayoutConstraint.leftConstraint = LeftAtParent
    constraintsLayoutConstraint.rightConstraint = RightToLeftOf(green)
    constraintsLayoutConstraint.topConstraint = TopAtParent
    constraintsLayoutConstraint.bottomConstraint = BottomFree
    frame2.add(blue, constraintsLayoutConstraint)

    constraintsLayoutConstraint = ConstraintsLayoutConstraint()
    constraintsLayoutConstraint.horizontalSize = ConstraintsSize.WRAPPED
    constraintsLayoutConstraint.verticalSize = ConstraintsSize.EXPANDED
    constraintsLayoutConstraint.leftConstraint = LeftFree
    constraintsLayoutConstraint.rightConstraint = RightAtParent
    constraintsLayoutConstraint.topConstraint = TopAtParent
    constraintsLayoutConstraint.bottomConstraint = BottomToTopOf(yellow)
    frame2.add(green, constraintsLayoutConstraint)

    constraintsLayoutConstraint = ConstraintsLayoutConstraint()
    constraintsLayoutConstraint.horizontalSize = ConstraintsSize.EXPANDED
    constraintsLayoutConstraint.verticalSize = ConstraintsSize.WRAPPED
    constraintsLayoutConstraint.leftConstraint = LeftAtParent
    constraintsLayoutConstraint.rightConstraint = RightAtParent
    constraintsLayoutConstraint.topConstraint = TopFree
    constraintsLayoutConstraint.bottomConstraint = BottomAtParent
    frame2.add(yellow, constraintsLayoutConstraint)

    constraintsLayoutConstraint = ConstraintsLayoutConstraint()
    constraintsLayoutConstraint.horizontalSize = ConstraintsSize.WRAPPED
    constraintsLayoutConstraint.verticalSize = ConstraintsSize.EXPANDED
    constraintsLayoutConstraint.leftConstraint = LeftAtParent
    constraintsLayoutConstraint.rightConstraint = RightFree
    constraintsLayoutConstraint.topConstraint = TopToBottomOf(blue)
    constraintsLayoutConstraint.bottomConstraint = BottomToTopOf(yellow)
    frame2.add(red, constraintsLayoutConstraint)

    constraintsLayoutConstraint = ConstraintsLayoutConstraint()
    constraintsLayoutConstraint.horizontalSize = ConstraintsSize.EXPANDED
    constraintsLayoutConstraint.verticalSize = ConstraintsSize.EXPANDED
    constraintsLayoutConstraint.topConstraint = TopToBottomOf(blue)
    constraintsLayoutConstraint.bottomConstraint = BottomToTopOf(yellow)
    constraintsLayoutConstraint.leftConstraint = LeftToRightOf(red)
    constraintsLayoutConstraint.rightConstraint = RightToLeftOf(green)
    frame2.add(black, constraintsLayoutConstraint)

    frame2.showFrame()


/*
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
    percentGraphics.color = Color(128,128,128,128)
    percentGraphics.fillRectangle(0.25,0.25,0.5,0.5)
}
 */
}