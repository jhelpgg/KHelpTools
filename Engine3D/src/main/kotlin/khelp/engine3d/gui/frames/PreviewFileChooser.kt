package khelp.engine3d.gui.frames

import khelp.engine3d.format.obj.objLoader
import khelp.engine3d.render.LIGHT_GRAY
import khelp.engine3d.render.Material
import khelp.engine3d.render.Node
import khelp.engine3d.render.Scene
import khelp.engine3d.render.Texture
import khelp.engine3d.render.Window3D
import khelp.engine3d.render.prebuilt.Box
import khelp.engine3d.render.window3DFix
import khelp.engine3d.resource.Textures
import khelp.thread.TaskContext
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.thread.parallel
import khelp.ui.events.MouseState
import khelp.ui.events.MouseStatus
import khelp.ui.game.GameImage
import khelp.utilities.log.exception
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.geom.Point2D
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JComponent
import kotlin.math.roundToInt

internal object PreviewFileChooser : JComponent()
{
    private const val TRANSLATE_X_Y = 0.02f
    private const val TRANSLATE_Z = 0.2f
    private const val ROTATE = 0.1f
    private val alreadyCreated = AtomicBoolean(false)
    private val promise = Promise<Window3D>()
    private val window3D : FutureResult<Window3D> = this.promise.futureResult
    private val materialObj : Material by lazy {
        val material = Material()
        material.textureDiffuse = Texture(Textures.ROCK.image)
        material.textureSpheric = Texture(Textures.EMERALD.image)
        material.sphericRate = 0.5f
        material
    }
    private val imagePreview = GameImage(512, 512)
    private val materialImage : Material by lazy {
        val material = Material()
        material.colorDiffuse = LIGHT_GRAY
        material.textureDiffuse = Texture(this.imagePreview)
        material
    }
    private var mouseX : Int = 0
    private var mouseY : Int = 0

    init
    {
        val dimension = Dimension(512, 512)
        this.size = dimension
        this.preferredSize = dimension
        this.minimumSize = dimension
    }

    override fun paintComponent(graphics : Graphics)
    {
        val graphics2D = graphics as Graphics2D
        graphics2D.color = Color.BLACK
        graphics2D.paint = GameImage.DARK_LIGHT_PAINT
        graphics2D.fillRect(0, 0, this.width, this.height)

        if (this.alreadyCreated.compareAndSet(false, true))
        {
            val transform = (graphics as Graphics2D).transform
            val start = transform.transform(Point2D.Double(), Point2D.Double())
            val end = transform.transform(Point2D.Double(this.width.toDouble(), this.height.toDouble()),
                                          Point2D.Double())
            val x = start.x.roundToInt()
            val y = start.y.roundToInt()
            val width = (end.x - start.x).roundToInt()
            val height = (end.y - start.y).roundToInt()

            parallel(TaskContext.INDEPENDENT, this.promise, Rectangle(x, y, width, height)) { promise, bounds ->
                window3DFix(bounds.x, bounds.y, bounds.width, bounds.height,
                            "Preview") {
                    this.mouseManager.mouseStateObservable.observedBy(TaskContext.INDEPENDENT,
                                                                      this@PreviewFileChooser::mouseState)
                    promise.result(this)
                }
            }
        }
    }

    fun changeVisibility(visible : Boolean)
    {
        this.window3D.and { window3D -> window3D.visible = visible }
    }

    fun close()
    {
        this.window3D.and { window3D -> window3D.close() }
    }

    fun preview(file : File?)
    {
        this.window3D.and { window3D ->
            try
            {
                when
                {
                    file == null            ->
                    {
                        this.showNothing(window3D.scene)
                    }
                    file.extension == "obj" ->
                    {
                        this.showObj(objLoader("obj", FileInputStream(file)), window3D.scene)
                    }
                    else                    ->
                    {
                        this.showImage(GameImage.load(FileInputStream(file)), window3D.scene)
                    }
                }
            }
            catch (exception : Exception)
            {
                exception(exception, "Failed to create preview : ", file?.absolutePath)
                this.showNothing(window3D.scene)
            }
        }
    }

    private fun showImage(image : GameImage, scene : Scene)
    {
        this.imagePreview.draw { graphics2D ->
            image.drawOn(graphics2D, 0, 0, 512, 512)
        }

        scene.root.removeAllChildren()
        val box = Box("box")
        box.material = this.materialImage
        box.materialForSelection = this.materialImage
        scene.root.addChild(box)
        scene.root.x = 0f
        scene.root.y = 0f
        scene.root.z = - 2f
    }

    private fun showObj(node : Node, scene : Scene)
    {
        node.applyMaterialHierarchically(this.materialObj)
        scene.root.removeAllChildren()
        scene.root.addChild(node)
        val virtualBox = node.totalBox()
        scene.root.x = 0f
        scene.root.y = 0f
        scene.root.z = - 2.2f * (virtualBox.maxZ - virtualBox.minZ)
    }

    private fun showNothing(scene : Scene)
    {
        scene.root.removeAllChildren()
    }

    private fun mouseState(mouseState : MouseState)
    {
        if (mouseState.mouseStatus != MouseStatus.DRAG)
        {
            this.mouseX = mouseState.x
            this.mouseY = mouseState.y
            return
        }

        val moveX = mouseState.x - this.mouseX
        val moveY = mouseState.y - this.mouseY

        this.window3D.and { window3D ->
            val node = window3D.scene.root

            when
            {
                mouseState.leftButtonDown && mouseState.rightButtonDown ->
                {
                    node.x += PreviewFileChooser.TRANSLATE_X_Y * moveX
                    node.y -= PreviewFileChooser.TRANSLATE_X_Y * moveY
                }

                mouseState.leftButtonDown                               ->
                {
                    node.angleX += PreviewFileChooser.ROTATE * moveY
                    node.angleY += PreviewFileChooser.ROTATE * moveX
                }

                mouseState.rightButtonDown                              ->
                {
                    node.z += PreviewFileChooser.TRANSLATE_Z * moveY
                }
            }
        }

        this.mouseX = mouseState.x
        this.mouseY = mouseState.y
    }
}
