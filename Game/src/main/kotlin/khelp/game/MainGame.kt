package khelp.game

import khelp.game.resources.AutoTile
import khelp.game.resources.AutoTileType
import khelp.game.resources.BackgroundImage
import khelp.game.resources.TileSet
import khelp.game.screen.GameFrame
import khelp.game.screen.TransitionScreen
import khelp.game.screen.play.AutoTileDescription
import khelp.game.screen.play.PlayScreen
import khelp.game.screen.play.TileSetDescription

fun main()
{
    val game = GameFrame("ZeGailleMe")
    val natureScreen = PlayScreen(BackgroundImage.GRASSLAND, 32, 32)

    val tileGrass = TileSetDescription(TileSet("images/tilesets/001-Grassland01.png"), 0, 0)
    val autoTile = AutoTile("images/autotiles/033-Waterfall01.png")
    val autoTileOcean = AutoTile("images/autotiles/026-Ocean03.png", 4)

    natureScreen.modifyLayer(0) { playScreenLayer ->
        for (y in 0 until 32)
        {
            for (x in 0 until 32)
            {
                playScreenLayer[x, y] = tileGrass
            }
        }

        playScreenLayer[3, 3] = AutoTileDescription(autoTile, AutoTileType.TOP_LEFT)
        playScreenLayer[4, 3] = AutoTileDescription(autoTile, AutoTileType.TOP)
        playScreenLayer[5, 3] = AutoTileDescription(autoTile, AutoTileType.TOP)
        playScreenLayer[6, 3] = AutoTileDescription(autoTile, AutoTileType.TOP)
        playScreenLayer[7, 3] = AutoTileDescription(autoTile, AutoTileType.TOP_RIGHT)

        for (k in 4 until 10)
        {
            playScreenLayer[3, k] = AutoTileDescription(autoTile, AutoTileType.LEFT)
            playScreenLayer[4, k] = AutoTileDescription(autoTile, AutoTileType.CENTER)
            playScreenLayer[5, k] = AutoTileDescription(autoTile, AutoTileType.CENTER)
            playScreenLayer[6, k] = AutoTileDescription(autoTile, AutoTileType.CENTER)
            playScreenLayer[7, k] = AutoTileDescription(autoTile, AutoTileType.RIGHT)
        }

        playScreenLayer[3, 10] = AutoTileDescription(autoTile, AutoTileType.BOTTOM_LEFT)
        playScreenLayer[4, 10] = AutoTileDescription(autoTile, AutoTileType.BOTTOM)
        playScreenLayer[5, 10] = AutoTileDescription(autoTile, AutoTileType.BOTTOM)
        playScreenLayer[6, 10] = AutoTileDescription(autoTile, AutoTileType.BOTTOM)
        playScreenLayer[7, 10] = AutoTileDescription(autoTile, AutoTileType.BOTTOM_RIGHT)

        playScreenLayer[10, 1] = AutoTileDescription(autoTileOcean, AutoTileType.TOP_LEFT)
        playScreenLayer[11, 1] = AutoTileDescription(autoTileOcean, AutoTileType.TOP)
        playScreenLayer[12, 1] = AutoTileDescription(autoTileOcean, AutoTileType.TOP)
        playScreenLayer[13, 1] = AutoTileDescription(autoTileOcean, AutoTileType.TOP_RIGHT)

        for (k in 2 until 5)
        {
            playScreenLayer[10, k] = AutoTileDescription(autoTileOcean, AutoTileType.LEFT)
            playScreenLayer[11, k] = AutoTileDescription(autoTileOcean, AutoTileType.CENTER)
            playScreenLayer[12, k] = AutoTileDescription(autoTileOcean, AutoTileType.CENTER)
            playScreenLayer[13, k] = AutoTileDescription(autoTileOcean, AutoTileType.RIGHT)
        }

        playScreenLayer[10, 5] = AutoTileDescription(autoTileOcean, AutoTileType.BOTTOM_LEFT)
        playScreenLayer[11, 5] = AutoTileDescription(autoTileOcean, AutoTileType.BOTTOM)
        playScreenLayer[12, 5] = AutoTileDescription(autoTileOcean, AutoTileType.BOTTOM)
        playScreenLayer[13, 5] = AutoTileDescription(autoTileOcean, AutoTileType.BOTTOM_RIGHT)
    }

    game.transition(natureScreen, TransitionScreen.GRAY)

    natureScreen.showDialog("Hello world !\nTo have [long] <text>, <we [can] try> use the long way home song or something else. The content is not really important, it is a test for see if cut long text work that's all ;)")
}