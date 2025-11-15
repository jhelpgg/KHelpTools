package khelp.engine3d

import java.util.concurrent.atomic.AtomicBoolean
import khelp.engine3d.animation.AccelerationInterpolation
import khelp.engine3d.animation.AnimationGroup
import khelp.engine3d.animation.AnimationManager
import khelp.engine3d.event.ActionCode
import khelp.engine3d.font.Font3D
import khelp.engine3d.format.obj.options.ObjUseNormalMap
import khelp.engine3d.render.Material
import khelp.engine3d.render.Texture
import khelp.engine3d.render.TwoSidedRule
import khelp.engine3d.render.WHITE
import khelp.engine3d.render.prebuilt.FaceUV
import khelp.engine3d.render.prebuilt.Plane
import khelp.engine3d.render.window3D
import khelp.engine3d.render.window3DFull
import khelp.engine3d.resource.Resources3D
import khelp.thread.TaskContext
import khelp.ui.game.AnimatedImage
import khelp.ui.game.GameImage

fun main()
{
    // animation()
    font3DTest()
}

private fun font3DTest()
{
    val node = Font3D.text("Hello world !")
    val image = GameImage.load("textures/PostTown.jpg", Resources3D.resources)
    val texture = Texture(image)
    val material = Material()
    material.textureDiffuse = texture
    node.applyMaterialHierarchically(material)

    window3DFull("Font3D") {
        scene.backgroundColor = WHITE
        scene.root {
            addChild(node)
        }

        actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT) { list ->
            for (actionCode in list)
            {
                when (actionCode)
                {
                    ActionCode.ACTION_UP       -> node.y += 0.1f
                    ActionCode.ACTION_DOWN     -> node.y -= 0.1f
                    ActionCode.ACTION_LEFT     -> node.x -= 0.1f
                    ActionCode.ACTION_RIGHT    -> node.x += 0.1f

                    ActionCode.ACTION_BUTTON_1 -> node.z -= 0.1f
                    ActionCode.ACTION_BUTTON_2 -> node.z += 0.1f

                    ActionCode.ACTION_BUTTON_3 -> node.angleY += 5f
                    ActionCode.ACTION_BUTTON_4 -> node.angleY -= 5f

                    ActionCode.ACTION_BUTTON_5 -> node.angleX += 5f
                    ActionCode.ACTION_BUTTON_6 -> node.angleX -= 5f

                    ActionCode.ACTION_EXIT     -> close()
                    else                       -> Unit
                }
            }
        }
    }
}

private fun animation()
{
    val toy = "sheep"
    val animationTime = 512L
    val animationDivideByZero =
        AnimatedImage(512, 512, GameImage.load("divideBy0/DivideBy0_0.png", Resources3D.resources))

    for (index in 1..10)
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

    for (y in 2 downTo -2)
    {
        var uMin = 0f
        var uMax = 0.2f
        val yy = y.toFloat()

        for (x in -2..2)
        {
            val plane = Plane("plane ($x,$y)", FaceUV(uMin, uMax, vMin, vMax))
            plane.material = material
            plane.x = x.toFloat()
            plane.y = yy
            plane.z = -1f
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
    val materialBear = Material()
    materialBear.textureDiffuse = Texture(Resources3D.loadImage("textures/toy${toy}_Default_color.jpg"))
    materialBear.colorDiffuse = WHITE
    materialBear.twoSided = false
    window3D(800, 600, "Test") {
        scene.backgroundColor = WHITE
        scene.root {
            for (plane in planes)
            {
                addChild(plane)
            }

            val nodeBear = obj("bear", "obj/toy${toy}.obj", Resources3D.resources,
                               ObjUseNormalMap("textures/toy${toy}_Default_nmap.jpg", Resources3D.resources))
            nodeBear.applyMaterialHierarchically(materialBear)

            //            val colors = arrayOf(BLACK, BLUE, RED, GREEN, YELLOW, ORANGE)
            //
            //            for (childIndex in 0 until nodeGate.numberOfChild)
            //            {
            //                val materialChild = Material()
            //                materialChild.colorEmissive = GRAY
            //                materialChild.colorDiffuse = colors[childIndex % colors.size]
            //                materialChild.textureSpheric = Texture(Resources3D.loadImage("textures/emerald.jpg"))
            //                materialChild.sphericRate = 0.5f
            //                nodeGate.child(childIndex)
            //                    .applyMaterialHierarchically(materialChild)
            //            }

            nodeBear.y = -34.5f
            nodeBear.z = -275f

            /*
                bunny :
                nodeBunny.y = -34.5f
                nodeBunny.z = -275f

                bear
                nodeBear.y = -34.5f
                nodeBear.z = -275f

                gate :
                nodeGate.y = 0f
                nodeGate.z = -40f
             */


            actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT) { list ->
                for (actionCode in list)
                {
                    when (actionCode)
                    {
                        ActionCode.ACTION_UP       -> nodeBear.z -= 0.1f
                        ActionCode.ACTION_DOWN     -> nodeBear.z += 0.1f
                        ActionCode.ACTION_LEFT     -> nodeBear.angleY -= 1f
                        ActionCode.ACTION_RIGHT    -> nodeBear.angleY += 1f

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
