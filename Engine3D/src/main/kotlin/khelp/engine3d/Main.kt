package khelp.engine3d

import khelp.engine3d.render.Material
import khelp.engine3d.render.Node
import khelp.engine3d.render.Texture
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
    val material = Material()
    material.textureDiffuse = texture
    window3D(800, 600, "Test") {
        scene.backgroundColor = YELLOW
        scene.root {
            text("HelloWorld", "Hello world") {
                z = -5f
                applyMaterialHierarchically(material)
                delay(4096) { rotate(this) }
            }
        }
    }
}

fun rotate(node : Node)
{
    for (time in 0 until 360)
    {
        node.angleX = time.toFloat()
        Thread.sleep(40)
    }
}
