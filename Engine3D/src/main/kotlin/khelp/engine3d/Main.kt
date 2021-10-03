package khelp.engine3d

import khelp.engine3d.animation.AccelerationInterpolation
import khelp.engine3d.animation.AnimationGroup
import khelp.engine3d.animation.AnimationManager
import khelp.engine3d.event.ActionCode
import khelp.engine3d.render.Material
import khelp.engine3d.render.Texture
import khelp.engine3d.render.TwoSidedRule
import khelp.engine3d.render.WHITE
import khelp.engine3d.render.prebuilt.FaceUV
import khelp.engine3d.render.prebuilt.Plane
import khelp.engine3d.render.window3D
import khelp.engine3d.resource.Resources3D
import khelp.thread.TaskContext
import khelp.ui.game.AnimatedImage
import khelp.ui.game.GameImage
import java.util.concurrent.atomic.AtomicBoolean

fun main()
{
    val animationTime = 512L
    val animationDivideByZero =
        AnimatedImage(512, 512, GameImage.load("divideBy0/DivideBy0_0.png", Resources3D.resources))

    for (index in 1 .. 10)
    {
        animationDivideByZero.appendImage(animationTime,
                                          GameImage.load("divideBy0/DivideBy0_$index.png", Resources3D.resources))
    }

    val material = Material()
    material.textureDiffuse = Texture(animationDivideByZero.imageAnimated)

    val planes = ArrayList<Plane>()
    var vMin = 0f
    var vMax = 0.2f
    val animationGroupTime = 4096L
    val animationGroup = AnimationGroup()

    for (y in 2 downTo - 2)
    {
        var uMin = 0f
        var uMax = 0.2f
        val yy = y.toFloat()

        for (x in - 2 .. 2)
        {
            val plane = Plane("plane ($x,$y)", FaceUV(uMin, uMax, vMin, vMax))
            plane.material = material
            plane.x = x.toFloat()
            plane.y = yy
            plane.z = - 1f
            plane.twoSidedRule = TwoSidedRule.FORCE_TWO_SIDE
            planes.add(plane)

            animationGroup.animationNodePositionElement(plane) {
                add(animationGroupTime, AccelerationInterpolation(1.25)) {
                    this.x *= 5f
                    this.y *= 5f
                    this.scaleX = 0f
                    this.scaleY = 0f
                    this.scaleZ = 0f
                }
            }

            uMin = uMax
            uMax += 0.2f
        }

        vMin = vMax
        vMax += 0.2f
    }

    AnimationManager.addAnimation("explode", animationGroup)
    animationDivideByZero.actionAtEnd = { AnimationManager.play("explode") }
    val running = AtomicBoolean(false)
    window3D(800, 600, "Test") {
        scene.backgroundColor = WHITE
        scene.root {
            for (plane in planes)
            {
                addChild(plane)
            }

            actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT) { list ->
                for (actionCode in list)
                {
                    when (actionCode)
                    {
                        ActionCode.ACTION_BUTTON_1 ->
                            if (running.compareAndSet(false, true))
                            {
                                animationDivideByZero.play(false)
                            }
                        else                       -> Unit
                    }
                }
            }
        }
    }
}
