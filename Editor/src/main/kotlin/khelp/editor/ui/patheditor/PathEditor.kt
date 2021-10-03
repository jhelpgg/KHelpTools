package khelp.editor.ui.patheditor

import khelp.engine3d.render.Texture
import khelp.engine3d.render.prebuilt.Revolution
import khelp.engine3d.resource.TextureCache
import khelp.ui.dsl.borderLayout
import khelp.ui.dsl.tableLayout
import khelp.ui.game.GameImage
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JToggleButton
import javax.swing.SpinnerNumberModel

class PathEditor
{
    companion object
    {
        private val rock : Texture by lazy { TextureCache["images/TextureRock.png"] }
        private val spherical : Texture by lazy { TextureCache["images/emerald.jpg"] }
    }

    private val pathPrecisionSpinner = JSpinner(SpinnerNumberModel(7, 2, 12, 1))
    private val rotationPrecisionSpinner = JSpinner(SpinnerNumberModel(12, 3, 32, 1))
    private val buttonMore = JButton(" + ")
    private val toggleTexture = JToggleButton(GameImage.loadThumbnail("images/image.png", TextureCache.resources,32,32))
    private val pathView = PathView()
    val revolution : Revolution = this.pathView.revolution

    init
    {
        this.pathPrecisionSpinner.addChangeListener {
            this.pathView.pathPrecision = this.pathPrecisionSpinner.value as Int
        }
        this.rotationPrecisionSpinner.addChangeListener {
            this.pathView.rotationPrecision = this.rotationPrecisionSpinner.value as Int
        }
        this.buttonMore.addActionListener { this.pathView.addLine() }
        this.toggleTexture.isSelected = false
        this.toggleTexture.addChangeListener {
            if (this.toggleTexture.isSelected)
            {
                this.showTexture()
            }
            else
            {
                this.hideTexture()
            }
        }
    }

    fun applyInside(panel : JPanel)
    {
        panel.borderLayout {

            pageStart {
                tableLayout {
                    pathPrecisionSpinner.cell(0, 0)
                    rotationPrecisionSpinner.cell(1, 0)
                    buttonMore.cell(2, 0)
                    toggleTexture.cell(3, 0)
                }
            }
            center(pathView)
        }
    }

    private fun showTexture()
    {
        this.revolution.showWire = false
        this.revolution.material.textureDiffuse = PathEditor.rock
        this.revolution.material.textureSpheric = PathEditor.spherical
        this.revolution.material.sphericRate = 0.25f
    }

    private fun hideTexture()
    {
        this.revolution.showWire = true
        this.revolution.material.originalSettings()
    }
}