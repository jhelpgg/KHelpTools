package khelp.match3.model

enum class GemType(imageName:String)
{
    CANDY("2.png"),
    FLAN("3.png"),
    CAKE("4.png"),
    TRIANGLE("5.png"),
    SQUARE("10.png"),
    PENTAGON("16.png"),
    HEXAGON("21.png"),
    STAR_BALL("32.png"),
    PEAR("43.png"),
    APPLE("44.png"),
    ORANGE("45.png"),
    TOMATO("46.png"),
    CARROT("48.png"),
    BANANA("50.png"),
    GRAPE("51.png"),
    BERRY("52.png"),
    LEMON("53.png")
    ;
    val tile : Tile = Tile(imageName)
}
