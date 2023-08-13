package khelp.uno.game

import khelp.engine3d.render.Window3D
import java.util.concurrent.atomic.AtomicReference

class GameManager(private val window3D : Window3D)
{
    private val gameStep = AtomicReference<GameStep>(GameStep.INTRODUCTION)

    fun launch()
    {
        this.gameStep(this.gameStep.get())
    }

    private fun gameStep(gameStep : GameStep)
    {
        val scene = this.window3D.scene
        scene.root.removeAllChildren()
        gameStep.gameScreen
            .play(this.window3D)
            .and(task = this::gameAction)
    }

    private fun gameAction(gameAction : GameAction)
    {
        when (gameAction)
        {
            GameAction.NEXT_STEP -> this.next()
            GameAction.EXIT      -> this.window3D.close()
        }
    }

    private fun next()
    {
        val gameStep = this.gameStep.get() !!

        when (gameStep)
        {
            GameStep.INTRODUCTION -> this.gameStep.set(GameStep.PLAY_GAME)
            GameStep.PLAY_GAME    -> this.gameStep.set(GameStep.END_GAME)
            GameStep.END_GAME     -> this.gameStep.set(GameStep.INTRODUCTION)
        }

        this.gameStep(this.gameStep.get())
    }
}
