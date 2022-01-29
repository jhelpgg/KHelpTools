package khelp.game.resources

import khelp.ui.extensions.drawPart
import khelp.ui.game.GameImage
import java.awt.Graphics2D
import java.awt.Point

const val CHARACTER_SIZE = 16
private const val ALPHABET_ORDER = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
private const val SYMBOL_ORDER = ".,!?:;()+-*/\\=è\"àâäéèêëîïôöùûüñ%@ç#§&"
private fun characterCoordinate(character : Char) : Point?
{
    val index = ALPHABET_ORDER.indexOf(character)

    if (index < 0)
    {
        return null
    }

    return Point((index % 8) * CHARACTER_SIZE, (index / 8) * CHARACTER_SIZE)
}

private fun symbolCoordinate(character : Char) : Point?
{
    val index = SYMBOL_ORDER.indexOf(character)

    if (index < 0)
    {
        return null
    }

    return Point((index % 8) * CHARACTER_SIZE, (index / 8) * CHARACTER_SIZE)
}

enum class AlphabetImage(private val pathLetter : String, private val pathSymbol : String)
{
    WOOD("images/alphabet/AlphabetWood.png", "images/alphabet/SymbolWood.png"),
    SILVER("images/alphabet/AlphabetSilver.png", "images/alphabet/SymbolSilver.png"),
    GOLD("images/alphabet/AlphabetGold.png", "images/alphabet/SymbolGold.png")
    ;

    private val imageLetter by lazy { GameImage.loadThumbnail(this.pathLetter, GameResources.resources, 128, 128) }
    private val imageSymbol by lazy { GameImage.loadThumbnail(this.pathSymbol, GameResources.resources, 128, 128) }

    fun draw(character : Char, x : Int, y : Int, graphics2D : Graphics2D)
    {
        var position = characterCoordinate(character)

        if (position != null)
        {
            graphics2D.drawPart(x, y, position.x, position.y, CHARACTER_SIZE, CHARACTER_SIZE, this.imageLetter)
            return
        }

        position = symbolCoordinate(character)

        if (position != null)
        {
            graphics2D.drawPart(x, y, position.x, position.y, CHARACTER_SIZE, CHARACTER_SIZE, this.imageSymbol)
        }
    }
}