package khelp.engine3d.resource

import khelp.ui.game.GameImage

enum class Eye(private val path : String)
{
    BLUE_1("eyes/eyeBlue.png"),
    BLUE_2("eyes/eyeBlue2.png"),
    BLUE_3("eyes/eyeBlue3.png"),
    BROWN_1("eyes/eyeBrown.png"),
    BROWN_2("eyes/eyeBrown2.png"),
    GREEN_1("eyes/eyeGreen.png"),
    GREEN_2("eyes/eyeGreen2.png"),
    GREEN3("eyes/eyeGreen3.png"),
    GREEN_SHINE("eyes/eyeGreenShine.png"),
    LIGHT_RED("eyes/eyeLightRed.png"),
    PINK("eyes/eyePink.png"),
    PURPLE("eyes/eyePurple.png"),
    RED_1("eyes/eyeRed.png"),
    RED_2("eyes/eyeRed2.png"),
    RED_3("eyes/eyeRed3.png"),
    TONE_BLUE("eyes/eyeToneBlue.png"),
    TONE_RED("eyes/eyeToneRed.png")
    ;

    val image : GameImage get() = Resources3D.loadImage(this.path)
}
