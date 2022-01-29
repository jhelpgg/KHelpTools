package khelp.game.screen.play

sealed class TileType

object EmptyTileType : TileType()

object WallTileType : TileType()

class EventTileType(val event:TileEvent) : TileType()

class ShopTileType(val shop:Shop) : TileType()
