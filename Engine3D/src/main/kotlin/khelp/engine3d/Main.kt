package khelp.engine3d

import khelp.engine3d.render.Texture
import khelp.engine3d.render.TwoSidedRule
import khelp.engine3d.render.YELLOW
import khelp.engine3d.render.window3D
import khelp.thread.delay
import khelp.ui.game.GameImage
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
    val texture = Texture(gameImage)
    window3D(800, 600, "Test") {
        scene.backgroundColor = YELLOW
        scene.root {
            revolution("revolution") {
                material.textureDiffuse = texture
                twoSidedRule = TwoSidedRule.FORCE_TWO_SIDE

                path(precision = 12, multiplierU = 3f, rotationPrecision = 32) {
                    move(0.25f, 1f)
                    line(0.25f, 0.5f)
                    quadratic(1f, -0.5f, 0.25f, -1f)
                    line(0f,-1f)
                }

                delay(4096) {
                    for(t in 0 until 1024)
                    {
                        angleX += 1f
                        Thread.sleep(32)
                    }
                }
            }
        }
    }
}
