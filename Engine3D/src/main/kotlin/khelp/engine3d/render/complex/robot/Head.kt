package khelp.engine3d.render.complex.robot

import khelp.engine3d.render.Texture
import khelp.engine3d.resource.Eye
import khelp.engine3d.resource.Mouth
import khelp.engine3d.resource.Textures
import khelp.ui.extensions.drawImage
import khelp.ui.game.GameImage
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Polygon
import kotlin.math.min

/**
 * [Robot] head parameters
 * @param leftEye Define the left eye
 * @param rightEye Define the right eye
 * @param mouth Define mouth expression
 */
class Head(leftEye : Eye = Eye.GREEN_2, rightEye : Eye = Eye.GREEN_2, mouth : Mouth = Mouth.SMILE_2)
{
    private val image = GameImage(512, 512)
    val texture = Texture(this.image)
    var leftEye : Eye = leftEye
        set(value)
        {
            field = value
            this.refresh()
        }
    var rightEye : Eye = rightEye
        set(value)
        {
            field = value
            this.refresh()
        }
    var mouth : Mouth = mouth
        set(value)
        {
            field = value
            this.refresh()
        }
    var hairColor : Color = Color(0xFFA0661C.toInt())
        set(value)
        {
            field = value
            this.refresh()
        }

    init
    {
        this.refresh()
    }

    private fun refresh()
    {
        this.image.clear(Color.WHITE)
        this.image.draw { graphics2D ->
            this.drawHair(graphics2D)
            graphics2D.drawImage(271, 192, this.leftEye.image)
            graphics2D.drawImage(177, 192, this.rightEye.image)
            graphics2D.drawImage(192, 300, this.mouth.image)
        }
    }

    private fun drawHair(graphics2D : Graphics2D)
    {
        val hairImage = Textures.HAIR.image.copy()
        hairImage.tint(this.hairColor)
        val xs = intArrayOf(0, 512, 512, 342, 333, 322, 342, 333, 321, 300, 250, 170, 150, 160, 170, 0)
        val ys = intArrayOf(0, 0, 512, 512, 256, 300, 170, 150, 160, 170, 155, 170, 250, 300, 512, 512)
        val size = min(xs.size, ys.size)
        val polygon = Polygon(xs, ys, size)
        val clip = graphics2D.clip
        graphics2D.clip(polygon)
        graphics2D.drawImage(0, 0, hairImage)
        graphics2D.clip = clip
    }
}
