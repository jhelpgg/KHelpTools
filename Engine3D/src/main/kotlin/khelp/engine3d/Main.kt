package khelp.engine3d

import khelp.engine3d.animation.AccelerationInterpolation
import khelp.engine3d.animation.AnimationManager
import khelp.engine3d.animation.AnticipateOvershootInterpolation
import khelp.engine3d.animation.BounceInterpolation
import khelp.engine3d.animation.DecelerationInterpolation
import khelp.engine3d.animation.HesitateInterpolation
import khelp.engine3d.render.Material
import khelp.engine3d.render.Node
import khelp.engine3d.render.YELLOW
import khelp.engine3d.render.window3DFull
import khelp.thread.TaskContext
import khelp.ui.game.GameImage
import khelp.ui.utilities.SHADOW
import khelp.utilities.log.debug
import java.awt.Color

fun main()
{
    val gameImage = GameImage(256, 256)
    gameImage.clear(Color.RED)
    gameImage.drawPercent { percentGraphics ->
        percentGraphics.color = Color.CYAN
        percentGraphics.fillRectangle(0.25, 0.25, 0.5, 0.5)
        percentGraphics.color = Color.ORANGE
        percentGraphics.fillRectangle(0.4, 0.6, 0.2, 0.4)
    }
    val gameImage2 = GameImage(256, 256)
    gameImage2.clear(SHADOW)
    gameImage2.drawPercent { percentGraphics ->
        percentGraphics.color = Color.PINK
        percentGraphics.fillOval(0.1, 0.2, 0.8, 0.6)
    }
    val material = Material()
    window3DFull("Test") {
        material.textureDiffuse = AnimationManager.animationTexture("textureAnime", gameImage2, gameImage, 20_000L)
        scene.backgroundColor = YELLOW

        scene.root {
            text("HelloWorld", "Hello world") {
                z = - 2f
                applyMaterialHierarchically(material)
            }
        }

        findById<Node>("HelloWorld")?.let { node ->
            AnimationManager.animationNodePositionElement("rotateHelloWorld", node) {
                add(10_000L, AccelerationInterpolation(3.21)) {
                    angleX = 360f
                }
                add(20_000L, DecelerationInterpolation(3.21)) {
                    angleX = 0f
                }
                add(40_000L, BounceInterpolation) {
                    angleY = 360f
                }
                add(50_000L, AnticipateOvershootInterpolation(1.23456789)) {
                    angleY = 0f
                }
                add(60_000L, HesitateInterpolation) {
                    angleZ = 360f
                }
            }
        }

        AnimationManager.play("rotateHelloWorld")
        AnimationManager.play("textureAnime")
        actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT) { list -> debug(list) }
    }
}
