package khelp.game.resources

import khelp.ui.game.GameImage

enum class BackgroundImage(val path : String)
{
    GRASSLAND("images/backgrounds/Grassland.jpg"),
    POST_TOWN("images/backgrounds/PostTown.jpg")
    ;

    val image : GameImage by lazy { GameImage.load(this.path, GameResources.resources) }
}