package khelp.uno.ui

import khelp.engine3d.render.Texture
import khelp.ui.game.GameImage
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0050
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0500
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0700
import khelp.ui.utilities.COLOR_LIGHT_BLUE_0900
import khelp.ui.utilities.COLOR_RED_0050
import khelp.ui.utilities.COLOR_RED_0300
import khelp.ui.utilities.COLOR_RED_0500
import khelp.uno.resourcesUno
import java.awt.Color

object UnoTextures
{
    private val textures = HashMap<String, Texture>()

    operator fun get(path : String) : Texture =
        this.textures.getOrPut(path) { Texture(GameImage.load(path, resourcesUno)) }

    private val trigonometricWayImage : GameImage by lazy {
        val image = GameImage.load("images/recycler.png", resourcesUno)
        image.colorize(Color(COLOR_LIGHT_BLUE_0700))
        image
    }

    private val clockWayImage : GameImage by lazy {
        val image = this.trigonometricWayImage.copy()
        image.flipHorizontal()
        image
    }

    val trigonometricWayTexture : Texture by lazy { Texture(this.trigonometricWayImage) }

    val clockWayTexture : Texture by lazy { Texture(this.clockWayImage) }

    val forbiddenTexture : Texture by lazy {
        val image = GameImage.load("images/interdit.png", resourcesUno)
        image.colorize(Color(COLOR_RED_0300))
        Texture(image)
    }

    val logoTexture : Texture by lazy { this["images/logo.png"] }
}
