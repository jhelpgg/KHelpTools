package khelp.game.resources

import khelp.ui.extensions.drawPart
import khelp.ui.game.GameImage
import java.awt.Dimension
import java.awt.Graphics2D
import kotlin.math.max

enum class CharacterImage(val path : String)
{
    Cleric06("images/characters/030-Cleric06.png"),
    Cleric07("images/characters/031-Cleric07.png"),
    King01("images/characters/042-King01.png"),
    Grappler01("images/characters/046-Grappler01.png"),
    Angel04("images/characters/082-Angel04.png"),
    Monster13("images/characters/099-Monster13.png"),
    Civilian14("images/characters/114-Civilian14.png"),
    Civilian18("images/characters/118-Civilian18.png"),
    Animal08("images/characters/158-Animal08.png")
    ;

    private val image by lazy { GameImage.load(this.path, GameResources.resources) }
    val dimension by lazy {
        val image = this.image
        Dimension(image.width / 4, image.height / 4)
    }
    val dimensionInTiles by lazy {
        val dimension = this.dimension
        Dimension(dimension.width / 32, dimension.height / 32)
    }

    fun draw(x : Int, y : Int, graphics2D : Graphics2D, characterPosition : CharacterPosition, animation : Int)
    {
        val size = this.dimension
        val indexX = max(0, animation) % 4
        graphics2D.drawPart(x, y,
                            indexX * size.width, characterPosition.indexY * size.height,
                            size.width, size.height,
                            this.image)
    }
}