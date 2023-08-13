package khelp.uno.game.introduction

import khelp.engine3d.animation.AnimationManager
import khelp.engine3d.animation.BounceInterpolation
import khelp.engine3d.animation.OvershootInterpolation
import khelp.engine3d.render.Window3D
import khelp.engine3d.render.prebuilt.Plane
import khelp.thread.extensions.doWhen
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.uno.game.GameAction
import khelp.uno.game.GameScreen
import khelp.uno.ui.UnoTextures

class GameIntroductionScreen : GameScreen
{
    companion object
    {
        private const val INTRODUCTION = "introduction"
        private const val SCALE = 4f
        private const val SLIDE_TIME = 2048L
        private const val ZOOM_TIME = 2048L
        private const val START_Z = - 20f
    }

    private val logePlane : Plane by lazy {
        val plane = Plane("logo")
        plane.x = - 100f
        plane.z = GameIntroductionScreen.START_Z
        plane.scaleX = GameIntroductionScreen.SCALE
        plane.scaleY = GameIntroductionScreen.SCALE
        plane.scaleZ = 1f
        plane.material.settingAsFor2D()
        plane.material.textureDiffuse = UnoTextures.logoTexture
        plane.materialForSelection = plane.material
        plane
    }

    init
    {
        AnimationManager.animationNodePositionElement(GameIntroductionScreen.INTRODUCTION, this.logePlane) {
            this.add(GameIntroductionScreen.SLIDE_TIME, OvershootInterpolation(2.0)) {
                this.x = 0f
                this.z = GameIntroductionScreen.START_Z
                this.scaleX = GameIntroductionScreen.SCALE
                this.scaleY = GameIntroductionScreen.SCALE
                this.scaleZ = 1f
            }

            this.add(GameIntroductionScreen.SLIDE_TIME + GameIntroductionScreen.ZOOM_TIME, BounceInterpolation) {
                this.x = 0f
                this.z = - 1.2f
                this.scaleX = GameIntroductionScreen.SCALE
                this.scaleY = GameIntroductionScreen.SCALE
                this.scaleZ = 1f
            }
        }
    }

    override fun play(window3D : Window3D) : FutureResult<GameAction>
    {
        val promise = Promise<GameAction>()
        window3D.scene.root.addChild(this.logePlane)
        window3D.mouseManager
            .mouseStateObservable
            .doWhen({ mouseState -> mouseState.clicked })
            { promise.result(GameAction.NEXT_STEP) }
        AnimationManager.play(GameIntroductionScreen.INTRODUCTION)
        return promise.futureResult
    }
}
