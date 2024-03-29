package khelp.engine3d.resource

import khelp.ui.game.GameImage

enum class Textures(private val path : String)
{
    BODY_COSTUME("textures/BodyCostume.png"),
    DICE("textures/Dice.png"),
    HAIR("textures/hair.png"),
    EMERALD("textures/emerald.png"),
    ROCK("textures/TextureRock.png"),
    TILE("textures/tile.png")
    ;

    val image : GameImage get() = Resources3D.loadImage(this.path)
}
