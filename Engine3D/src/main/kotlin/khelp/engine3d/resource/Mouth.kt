package khelp.engine3d.resource

import khelp.ui.game.GameImage

enum class Mouth(private val path : String)
{
    ANNOY_1("mouth/annoy1.png"),
    ANNOY_2("mouth/annoy2.png"),
    ANNOY_3("mouth/annoy3.png"),
    ANNOY_BIG_1("mouth/annoyBig1.png"),
    ANNOY_BIG_2("mouth/annoyBig2.png"),
    ANNOY_BIG_3("mouth/annoyBig3.png"),
    ANNOY_SEMI_1("mouth/annoySemi1.png"),
    ANNOY_SEMI_2("mouth/annoySemi2.png"),
    ANNOY_SEMI_3("mouth/annoySemi3.png"),
    SAD_1("mouth/sad1.png"),
    SAD_2("mouth/sad2.png"),
    SAD_3("mouth/sad3.png"),
    SERIOUS_1("mouth/serious1.png"),
    SERIOUS_2("mouth/serious2.png"),
    SERIOUS_3("mouth/serious3.png"),
    SMILE_1("mouth/smile1.png"),
    SMILE_2("mouth/smile2.png"),
    SMILE_3("mouth/smile3.png"),
    SMILE_BIG_1("mouth/smileBig1.png"),
    SMILE_BIG_2("mouth/smileBig2.png"),
    SMILE_BIG_3("mouth/smileBig3.png"),
    SMILE_OTHER_1("mouth/smileOther1.png"),
    SMILE_OTHER_2("mouth/smileOther2.png"),
    SMILE_OTHER_3("mouth/smileOther3.png"),
    WHISPER_1("mouth/whisper1.png"),
    WHISPER_2("mouth/whisper2.png"),
    WHISPER_3("mouth/whisper3.png")
    ;

    val image : GameImage get() = Resources3D.loadImage(this.path)
}
