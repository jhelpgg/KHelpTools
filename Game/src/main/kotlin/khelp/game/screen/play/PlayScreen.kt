package khelp.game.screen.play

import khelp.game.event.KeyAction
import khelp.game.event.KeyboardManager
import khelp.game.resources.BackgroundImage
import khelp.game.resources.BackgroundSound
import khelp.game.resources.CHARACTER_SIZE
import khelp.game.resources.CharacterImage
import khelp.game.resources.CharacterPosition
import khelp.game.resources.EffectSound
import khelp.game.resources.SoundManager
import khelp.game.screen.AlphabetText
import khelp.game.screen.GameScreen
import khelp.ui.extensions.drawImage
import khelp.ui.utilities.TRANSPARENT
import khelp.utilities.math.random
import java.awt.BasicStroke
import java.awt.Color

class PlayScreen(private val backgroundImage : BackgroundImage,
                 val numberTileHorizontal : Int, val numberTileVertical : Int)
    : GameScreen()
{
    private val layers = ArrayList<PlayScreenLayer>()
    private var frame = 0
    private var x = 0
    private var y = 0
    private val dialog = AlphabetText(620, 3 * CHARACTER_SIZE + 8)
    private var dialogShow = false
    private var characterImage = random<CharacterImage>()
    private var characterPosition = CharacterPosition.FACE

    override fun startScreen()
    {
        SoundManager.playBackground(BackgroundSound.BATTLE_01)
        this.frame = 0
        this.update()
    }

    override fun pauseScreen()
    {
    }

    override fun refresh()
    {
        this.frame ++
        this.update()
    }

    fun modifyLayer(index : Int, playScreenLayer : (PlayScreenLayer) -> Unit)
    {
        if (index < 0)
        {
            return
        }

        synchronized(this.layers)
        {
            while (index >= this.layers.size)
            {
                this.layers.add(PlayScreenLayer(this.numberTileHorizontal, this.numberTileVertical))
            }

            playScreenLayer(this.layers[index])
        }

        this.update()
    }

    fun hideDialog()
    {
        if (this.dialogShow)
        {
            this.dialogShow = false
            this.update()
        }
    }

    fun showDialog(text : String)
    {
        this.dialog.setText(text)
        this.dialogShow = true
        this.update()
    }

    private fun update()
    {
        this.manageKeysAction()
        this.image.clear(TRANSPARENT)
        this.image.draw { graphics2D ->
            graphics2D.drawImage(0, 0, this.backgroundImage.image)

            synchronized(this.layers)
            {
                for (layer in this.layers)
                {
                    layer.draw(this.x, this.y, graphics2D, this.frame)
                }
            }

            val xx = (640 - this.characterImage.dimension.width) / 2
            val yy = (320 - this.characterImage.dimension.height) / 2
            this.characterImage.draw(xx, yy, graphics2D, this.characterPosition, this.frame / 4)

            if (this.frame > 100)
            {
                this.characterImage = random<CharacterImage>()
                this.frame = - 1
            }

            if (this.dialogShow)
            {
                graphics2D.color = Color(64, 128, 64, 192)
                graphics2D.fillRoundRect(4, 320 - this.dialog.height, 632, this.dialog.height, 16, 16)
                this.dialog.draw(10, 320 - this.dialog.height, graphics2D)
                graphics2D.color = Color.DARK_GRAY
                graphics2D.stroke = BasicStroke(5f)
                graphics2D.drawRoundRect(4, 320 - this.dialog.height, 632, this.dialog.height, 16, 16)
                graphics2D.color = Color.LIGHT_GRAY
                graphics2D.stroke = BasicStroke(3f)
                graphics2D.drawRoundRect(4, 320 - this.dialog.height, 632, this.dialog.height, 16, 16)
            }
        }
    }

    private fun manageKeysAction()
    {
        val actions = KeyboardManager.currentActions(this.dialogShow)

        if (this.dialogShow)
        {
            this.dialogKeyAction(actions)
        }
        else
        {
            this.playKeyAction(actions)
        }

    }

    private fun dialogKeyAction(actions : Array<KeyAction>)
    {
        when
        {
            KeyAction.UP in actions || KeyAction.PREVIOUS in actions -> this.dialog.previous()
            KeyAction.DOWN in actions || KeyAction.NEXT in actions   -> this.dialog.next()
            KeyAction.ACTION in actions                              ->
                if (this.dialog.hasNext)
                {
                    this.dialog.next()
                }
                else
                {
                    this.dialogShow = false
                }
        }
    }

    private fun playKeyAction(actions : Array<KeyAction>)
    {
        if (KeyAction.UP in actions)
        {
            this.characterPosition = CharacterPosition.BACK
            this.y += 4
        }

        if (KeyAction.DOWN in actions)
        {
            this.characterPosition = CharacterPosition.FACE
            this.y -= 4
        }

        if (KeyAction.LEFT in actions)
        {
            this.characterPosition = CharacterPosition.LEFT
            this.x += 4
        }

        if (KeyAction.RIGHT in actions)
        {
            this.characterPosition = CharacterPosition.RIGHT
            this.x -= 4
        }

        if(KeyAction.ACTION in actions)
        {
            SoundManager.playEffect(EffectSound.ELECTRICITY_SPARK_SWORD_ATTACK_01)
        }
    }
}