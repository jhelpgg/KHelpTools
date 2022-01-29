package khelp.game.resources

import khelp.ui.game.GameImage

enum class Transition(val path : String)
{
    BLIND("images/transitions/Blind.png"),
    BRICK("images/transitions/Brick.png"),
    RANDOM("images/transitions/Random.png"),
    WHORL("images/transitions/Whorl.png")
    ;

    val image : GameImage get() = GameImage.load(this.path, GameResources.resources)
}