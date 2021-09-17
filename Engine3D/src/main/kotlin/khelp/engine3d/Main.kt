package khelp.engine3d

import khelp.engine3d.render.Texture
import khelp.engine3d.render.YELLOW
import khelp.engine3d.render.window3D
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
    window3D(800, 600, "Test") {
        scene.backgroundColor = YELLOW
        scene.root {
            sphere("sphere",slice=16,stack = 16) {
                material.textureDiffuse = Texture(gameImage)
            }
        }
    }
}
