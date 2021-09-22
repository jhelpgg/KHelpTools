package khelp.engine3d.render.font

import khelp.engine3d.render.Node
import khelp.engine3d.render.NodeWithMaterial
import khelp.engine3d.render.Object3D
import khelp.engine3d.render.ObjectClone
import khelp.engine3d.render.TwoSidedRule
import khelp.ui.font.JHelpFont
import khelp.ui.utilities.FONT_RENDER_CONTEXT
import khelp.utilities.math.square
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.sqrt

class Font3D private constructor(val family : String)
{
    companion object
    {
        private val fonts = HashMap<String, Font3D>()
        private val nextID = AtomicInteger(0)

        fun font3D(family : String) : Font3D =
            Font3D.fonts.getOrPut(family) { Font3D((family)) }

        private fun createName(family : String, letter : Char) : String =
            "Font3D_${family}_${letter.toInt()}_${Font3D.nextID.getAndIncrement()}"
    }

    private val letters = HashMap<Char, Object3D>()
    private val font = JHelpFont(this.family, 1)

    fun computeText(id : String, string : String) : Node
    {
        val node = Node(id)

        val characters = string.toCharArray()
        var x = - (characters.size.toFloat() / 2f)

        for (character in characters)
        {
            if (character > ' ')
            {
                val letter = this.obtainLetter(character)
                letter.x = x
                node.addChild(letter)
            }

            x += 1f
        }

        return node
    }

    fun obtainLetter(character : Char) : NodeWithMaterial
    {
        var object3D = this.letters[character]

        if (object3D != null)
        {
            return ObjectClone(Font3D.createName(this.family, character), object3D)
        }

        object3D = this.createObject(character)
        this.letters[character] = object3D
        return object3D
    }

    private fun createObject(character : Char) : Object3D
    {
        val object3D = Object3D(Font3D.createName(this.family, character))
        object3D.twoSidedRule = TwoSidedRule.FORCE_TWO_SIDE

        val z = font.font.size2D / 8f
        val borderIterator = BorderIterator(font.shape(character.toString()))

        val tx = (borderIterator.maxX - borderIterator.minX) / 2f
        val ty = (borderIterator.maxY - borderIterator.minY) / 2f - 0.5f
        val length = borderIterator.length
        var u = 0f
        var dx = 0f
        var dy = 0f
        var cx = 0f
        var cy = 0f
        var fx = 0f
        var fy = 0f
        var nx = 0f
        var ny = 0f
        val vz2 = z * 2f

        object3D.mesh {
            for (borderElement in borderIterator)
            {
                when (borderElement.borderElementType)
                {
                    BorderElementType.MOVE  ->
                    {
                        dx = borderElement.x
                        dy = borderIterator.maxY - borderElement.y
                        cx = borderElement.x
                        cy = borderIterator.maxY - borderElement.y
                        fx = borderElement.x
                        fy = borderIterator.maxY - borderElement.y
                    }
                    BorderElementType.LINE  ->
                    {
                        fx = borderElement.x
                        fy = borderIterator.maxY - borderElement.y
                        nx = (cy - fy) * vz2
                        ny = (fx - cx) * vz2

                        face {
                            add(tx + cx, ty + cy, - z,
                                u / length, 0f,
                                nx, ny, 0f)
                            add(tx + cx, ty + cy, z,
                                u / length, 1f,
                                nx, ny, 0f)

                            u += sqrt(square(cx - fx) + square(cy - fy))

                            add(tx + fx, ty + fy, z,
                                u / length, 1f,
                                nx, ny, 0f)
                            add(tx + fx, ty + fy, - z,
                                u / length, 0f,
                                nx, ny, 0f)
                        }

                        cx = fx
                        cy = fy
                    }
                    BorderElementType.CLOSE ->
                    {
                        nx = (cy - dy) * vz2
                        ny = (dx - cx) * vz2

                        face {
                            add(tx + cx, ty + cy, - z,
                                u / length, 0f,
                                nx, ny, 0f)
                            add(tx + cx, ty + cy, z,
                                u / length, 1f,
                                nx, ny, 0f)

                            u += sqrt(square(cx - dx) + square(cy - dy))

                            add(tx + dx, ty + dy, z,
                                u / length, 1f,
                                nx, ny, 0f)
                            add(tx + dx, ty + dy, - z,
                                u / length, 0f,
                                nx, ny, 0f)
                        }

                        dx = fx
                        dy = fy
                    }
                }
            }
        }

        return object3D
    }
}