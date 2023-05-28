package khelp.obj.viewer.ui

import khelp.engine3d.event.ActionCode
import khelp.engine3d.format.obj.objLoader
import khelp.engine3d.gui.dsl.buttonText
import khelp.engine3d.gui.dsl.colorChooserButton
import khelp.engine3d.gui.dsl.constraintLayout
import khelp.engine3d.gui.dsl.dialogProportion
import khelp.engine3d.gui.layout.proprtion.GUIProportionConstraint
import khelp.engine3d.render.Material
import khelp.engine3d.render.Node
import khelp.engine3d.render.Scene
import khelp.engine3d.render.Texture
import khelp.engine3d.render.Window3D
import khelp.engine3d.render.prebuilt.Box
import khelp.engine3d.render.window3DFull
import khelp.engine3d.resource.Textures
import khelp.resources.Resources
import khelp.thread.TaskContext
import khelp.thread.parallel
import khelp.ui.events.MouseState
import khelp.ui.events.MouseStatus
import khelp.utilities.extensions.percent
import java.io.File
import java.io.FileInputStream
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

object FrameObjViewer
{
    private const val TRANSLATE_X_Y = 0.01f
    private const val TRANSLATE_Z = 0.1f
    private const val ROTATE = 0.1f

    private var file : File? = null
    private val showing = AtomicBoolean(false)
    private lateinit var window3D : Window3D
    private lateinit var scene : Scene
    private var node : Node = Box("box")
    private var mouseX : Int = 0
    private var mouseY : Int = 0
    private val material : Material by lazy {
        val material = Material()
        material.textureDiffuse = Texture(Textures.ROCK.image)
        material.textureSpheric = Texture(Textures.EMERALD.image)
        material.sphericRate = 0.5f
        material
    }

    fun show(file : File?)
    {
        this.file = file

        if (this.showing.compareAndSet(false, true))
        {
            this.start3D()
        }
        else
        {
            parallel(task = this::update)
        }
    }

    private fun start3D()
    {
        window3DFull("Obj Viewer") {
            this@FrameObjViewer.window3D = this
            val buttonOpen = buttonText(keyText = "open")
            buttonOpen.click = {
                if (Resources.languageObservableData.value().language == Locale.ENGLISH.language)
                {
                    Resources.languageObservableData.value(Locale.FRENCH)
                }
                else
                {
                    Resources.languageObservableData.value(Locale.ENGLISH)
                }
            }
            val buttonSaveAs = buttonText(keyText = "saveAs")

            val buttonCancel = buttonText(keyText = "cancel")
            val dialog = this.gui.dialogProportion {
                buttonCancel with GUIProportionConstraint(0.percent, 0.percent, 100.percent, 100.percent)
            }

            buttonSaveAs.click = dialog::show
            buttonCancel.click = dialog::close

            val buttonColor = this.gui.colorChooserButton { color -> println("Color = $color") }

            this.gui.constraintLayout {
                buttonOpen with {
                    this.horizontalExpanded
                    this.verticalWrapped

                    this.marginTop = 8
                    this.marginLeft = 8
                    this.marginRight = 8

                    this.topAtParent
                    this.bottomFree
                    this.leftAtParent
                    this.rightAtParent
                }

                buttonSaveAs with {
                    this.horizontalWrapped
                    this.verticalWrapped

                    this.topAtBottom = buttonOpen
                    this.bottomAtParent
                    this.leftAtParent
                    this.rightFree
                }

                buttonColor with {
                    this.horizontalWrapped
                    this.verticalWrapped

                    this.topAtBottom = buttonOpen
                    this.bottomAtParent
                    this.leftAtRight = buttonSaveAs
                    this.rightAtParent
                }
            }
            this@FrameObjViewer.scene = this.scene
            this.mouseManager.mouseStateObservable.observedBy(TaskContext.INDEPENDENT, this@FrameObjViewer::mouseAction)
            this.actionManager.actionObservable.observedBy(TaskContext.INDEPENDENT, this@FrameObjViewer::actions)
            parallel(task = this@FrameObjViewer::update)
        }
    }

    private fun update()
    {
        this.file?.let { file -> this.node = objLoader("obj", FileInputStream(file)) }
        this.node.applyMaterialHierarchically(this.material)

        this.scene.root.removeAllChildren()
        this.scene.root.addChild(this.node)
        val virtualBox = this.node.totalBox()
        this.scene.root.x = 0f
        this.scene.root.y = 0f
        this.scene.root.z = - 2f * (virtualBox.maxZ - virtualBox.minZ)
    }

    private fun mouseAction(mouseState : MouseState)
    {
        if (mouseState.mouseStatus != MouseStatus.DRAG)
        {
            this.mouseX = mouseState.x
            this.mouseY = mouseState.y
            return
        }

        val moveX = mouseState.x - this.mouseX
        val moveY = mouseState.y - this.mouseY

        when
        {
            mouseState.leftButtonDown && mouseState.rightButtonDown ->
            {
                this.node.x += FrameObjViewer.TRANSLATE_X_Y * moveX
                this.node.y -= FrameObjViewer.TRANSLATE_X_Y * moveY
            }

            mouseState.leftButtonDown                               ->
            {
                this.node.angleX += FrameObjViewer.ROTATE * moveY
                this.node.angleY += FrameObjViewer.ROTATE * moveX
            }

            mouseState.rightButtonDown                              ->
            {
                this.node.z += FrameObjViewer.TRANSLATE_Z * moveY
            }
        }


        this.mouseX = mouseState.x
        this.mouseY = mouseState.y
    }

    private fun actions(actions : List<ActionCode>)
    {
        for (action in actions)
        {
            when (action)
            {
                ActionCode.ACTION_EXIT -> this.window3D.close()
                else                   -> Unit
            }
        }
    }
}
