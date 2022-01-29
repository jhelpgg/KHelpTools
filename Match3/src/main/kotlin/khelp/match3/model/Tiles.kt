package khelp.match3.model

val horizontalLine : Tile by lazy {
    val tile = Tile("Horizontal_0.png")
    tile.addImage("Horizontal_1.png")
    tile.addImage("Horizontal_2.png")
    tile.addImage("Horizontal_3.png")
    tile.pingPong = true
    tile
}

val verticalLine : Tile by lazy {
    val tile = Tile("Horizontal_0.png")
    tile.addImage("Horizontal_1.png")
    tile.addImage("Horizontal_2.png")
    tile.addImage("Horizontal_3.png")
    tile.pingPong = true
    tile.rotated = true
    tile
}

val bomb : Tile by lazy {
    val tile = Tile("Bomb-0.png")
    tile.addImage("Bomb-1.png")
    tile.addImage("Bomb-2.png")
    tile.addImage("Bomb-3.png")
    tile.pingPong = true
    tile
}
