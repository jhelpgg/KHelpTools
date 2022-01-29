package khelp.game.screen

import khelp.game.resources.BackgroundImage
import khelp.ui.extensions.drawImage

object StartScreen : GameScreen()
{
    init
    {
        this.image.draw { graphics2D -> graphics2D.drawImage(0, 0, BackgroundImage.POST_TOWN.image) }
    }

    override fun startScreen()
    {
    }

    override fun pauseScreen()
    {
    }

    override fun refresh()
    {
    }
}