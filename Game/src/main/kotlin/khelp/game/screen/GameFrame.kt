package khelp.game.screen

import khelp.game.event.KeyboardManager
import khelp.thread.parallel
import khelp.ui.dsl.borderLayout
import khelp.ui.dsl.frame
import khelp.ui.extensions.drawImage
import khelp.ui.game.GameComponent
import khelp.ui.game.interpolation.GameImageInterpolation
import khelp.utilities.math.Percent

class GameFrame(title : String)
{
    companion object
    {
        private const val TRANSITION_STEP = 5
    }

    private var screen : GameScreen
    private lateinit var transition : GameImageInterpolation
    private var transitionPercent = - 1
    private val mainComponent = GameComponent(640, 320)

    init
    {
        frame(title, decorated = false, full = true) { borderLayout { center(mainComponent) } }
        this.mainComponent.isRequestFocusEnabled = true
        this.mainComponent.isFocusable = true
        this.mainComponent.requestFocus()
        this.mainComponent.requestFocusInWindow()
        this.mainComponent.grabFocus()
        this.mainComponent.addKeyListener(KeyboardManager)
        this.screen = StartScreen
        this.screen.startScreen()
        parallel { this.updateScreen() }
    }

    internal fun transition(screen : GameScreen, transitionScreen : TransitionScreen)
    {
        this.screen.pauseScreen()
        screen.startScreen()
        this.transition = GameImageInterpolation(this.screen.image, screen.image, transitionScreen())
        this.transitionPercent = 0
        this.screen = screen
    }

    private fun updateScreen()
    {
        while (true)
        {
            if (this.transitionPercent >= 0)
            {
                this.transition.percent(Percent(this.transitionPercent))
                this.mainComponent.gameImage.draw { graphics2D -> graphics2D.drawImage(0, 0, this.transition.result) }
                this.transitionPercent += GameFrame.TRANSITION_STEP

                if ( this.transitionPercent > 100)
                {
                    this.transitionPercent = - 1
                }
            }
            else
            {
                this.screen.refresh()
                this.mainComponent.gameImage.draw { graphics2D -> graphics2D.drawImage(0, 0, this.screen.image) }
            }

            Thread.sleep(64)
        }
    }
}