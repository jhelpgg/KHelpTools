package khelp.game.screen

import khelp.utilities.math.random
import khelp.game.resources.GameResources
import khelp.game.resources.Transition
import khelp.ui.game.interpolation.GameImageInterpolationGoesDown
import khelp.ui.game.interpolation.GameImageInterpolationGoesUp
import khelp.ui.game.interpolation.GameImageInterpolationGrayTransition
import khelp.ui.game.interpolation.GameImageInterpolationType

enum class TransitionScreen
{
    UP
    {
        override fun invoke() : GameImageInterpolationType = GameImageInterpolationGoesUp
    },
    DOWN
    {
        override fun invoke() : GameImageInterpolationType = GameImageInterpolationGoesDown
    },
    GRAY
    {
        override fun invoke() : GameImageInterpolationType =
            GameImageInterpolationGrayTransition(random<Transition>().path, GameResources.resources)
    }
    ;

    abstract operator fun invoke() : GameImageInterpolationType
}