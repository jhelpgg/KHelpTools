package khelp.uno.ui

import khelp.engine3d.gui.dsl.constraintLayout
import khelp.engine3d.render.window3DFull
import khelp.thread.extensions.doWhen
import khelp.thread.future.FutureResult
import khelp.uno.game.GameManager
import khelp.uno.model.CardColor
import khelp.uno.model.CardPlay
import khelp.uno.model.CardValue
import khelp.uno.model.ColorSelectionStatus

object Uno
{
    private val cardColorSelectorComponent = CardColorSelectorComponent()

    fun show()
    {
        this.cardColorSelectorComponent.visible = false

        window3DFull("UNO") {
            this.gui.constraintLayout {
                this@Uno.cardColorSelectorComponent.with {
                    this.horizontalWrapped
                    this.verticalWrapped

                    this.topAtParent
                    this.bottomAtParent
                    this.leftAtParent
                    this.rightAtParent
                }
            }

            this.scene.backgroundTexture = UnoTextures["images/background.png"]
            GameManager(this).launch()
            UnoScreenManager(this)
        }
    }

    fun selectColorForChangeColor() : FutureResult<CardPlay> =
        this.selectColor()
            .and { color -> CardPlay(color, CardValue.COLOR) }

    fun selectColorForMore4() : FutureResult<CardPlay> =
        this.selectColor()
            .and { color -> CardPlay(color, CardValue.MORE_4) }

    private fun selectColor() : FutureResult<CardColor>
    {
        this.cardColorSelectorComponent.visible = true
        return this.cardColorSelectorComponent
            .colorSelected
            .doWhen(condition = { status -> status != ColorSelectionStatus.NO_SELECTION },
                    action = this::colorSelected)
    }

    private fun colorSelected(status : ColorSelectionStatus) : CardColor
    {
        this.cardColorSelectorComponent.visible = false

        return when (status)
        {
            ColorSelectionStatus.SELECT_BLUE   -> CardColor.BLUE
            ColorSelectionStatus.SELECT_GREEN  -> CardColor.GREEN
            ColorSelectionStatus.SELECT_RED    -> CardColor.RED
            ColorSelectionStatus.SELECT_YELLOW -> CardColor.YELLOW
            else                               -> throw RuntimeException("Should never happen")
        }
    }
}
