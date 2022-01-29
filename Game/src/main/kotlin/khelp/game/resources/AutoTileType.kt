package khelp.game.resources

enum class AutoTileType(internal val tileX : Int, internal val tileY : Int)
{
    POINT(0, 0),
    EMPTY(1, 0),
    SMALL(2, 0),
    TOP_LEFT(0, 1),
    TOP(1, 1),
    TOP_RIGHT(2, 1),
    LEFT(0, 2),
    CENTER(1, 2),
    RIGHT(2, 2),
    BOTTOM_LEFT(0, 3),
    BOTTOM(1, 3),
    BOTTOM_RIGHT(2, 3)
}
