package khelp.editor.ui.materialeditor

import khelp.editor.ui.Editor
import khelp.editor.ui.ScreenEditor
import khelp.editor.ui.components.TextureSelector
import khelp.engine3d.render.Color4f
import khelp.engine3d.render.Material
import khelp.engine3d.render.Node
import khelp.engine3d.render.prebuilt.Box
import khelp.engine3d.render.prebuilt.BoxUV
import khelp.engine3d.render.prebuilt.CrossUV
import khelp.engine3d.render.prebuilt.Sphere
import khelp.thread.TaskContext
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.ui.components.ColorComponent
import khelp.ui.components.JLabel
import khelp.ui.dsl.borderLayout
import khelp.ui.dsl.verticalLayout
import khelp.ui.extensions.addTitle
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSlider

class MaterialEditor : ScreenEditor
{
    companion object
    {
        private val sphere : Sphere by lazy { Sphere("materialEditorSphere") }
        private val box : Box by lazy { Box("materialEditorBox") }
    }

    private val materialObservableData = ObservableData(Material())
    val materialObservable : Observable<Material> = this.materialObservableData.observable
    private val mainNode = Node("materialEditor")

    private val diffuseColor =
        ColorComponent(this.materialObservableData.value().colorDiffuse.color, "diffuse", Editor.resourcesText)
    private val textureDiffuseCheckBox = JCheckBox()
    private val textureDiffuse = TextureSelector()

    private val transparencySlider = JSlider(0, 100, 100)

    private val ambientColor =
        ColorComponent(this.materialObservableData.value().colorAmbient.color, "ambient", Editor.resourcesText)
    private val emissiveColor =
        ColorComponent(this.materialObservableData.value().colorEmissive.color, "emissive", Editor.resourcesText)

    private val specularColor =
        ColorComponent(this.materialObservableData.value().colorSpecular.color, "specular", Editor.resourcesText)
    private val specularSlider = JSlider(0, 100, 10)

    private val scrollPane = JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)

    override val manipulatedNode : Node get() = this.mainNode

    init
    {
        this.diffuseColor.colorObservable.observedBy(TaskContext.INDEPENDENT) { this.updateMaterial() }
        this.textureDiffuseCheckBox.addChangeListener { this.updateMaterial() }
        this.textureDiffuse.textureObservable.observedBy(TaskContext.INDEPENDENT) { this.updateMaterial() }

        val transparencyLabel = javax.swing.JLabel("100", javax.swing.JLabel.CENTER)
        this.transparencySlider.addChangeListener {
            transparencyLabel.text = this.transparencySlider.value.toString()
            this.updateMaterial()
        }

        this.ambientColor.colorObservable.observedBy(TaskContext.INDEPENDENT) { this.updateMaterial() }
        this.emissiveColor.colorObservable.observedBy(TaskContext.INDEPENDENT) { this.updateMaterial() }

        this.specularColor.colorObservable.observedBy(TaskContext.INDEPENDENT) { this.updateMaterial() }
        val specularLabel = javax.swing.JLabel("10", javax.swing.JLabel.CENTER)
        this.specularSlider.addChangeListener {
            specularLabel.text = this.specularSlider.value.toString()
            this.updateMaterial()
        }

        val diffusePanel = JPanel(FlowLayout())
        diffusePanel.add(this.diffuseColor)
        diffusePanel.add(this.textureDiffuseCheckBox)
        diffusePanel.add(this.textureDiffuse)
        diffusePanel.addTitle("diffuse", Editor.resourcesText)

        val ambientPanel = JPanel(FlowLayout())
        ambientPanel.add(JLabel("ambient", Editor.resourcesText))
        ambientPanel.add(this.ambientColor)

        val emissivePanel = JPanel(FlowLayout())
        emissivePanel.add(JLabel("emissive", Editor.resourcesText))
        emissivePanel.add(this.emissiveColor)

        val specularPanel = JPanel()
        specularPanel.borderLayout {
            lineStart(specularColor)
            pageStart(specularLabel)
            center(specularSlider)
        }
        specularPanel.addTitle("specular",Editor.resourcesText)

        val panel = JPanel()
        panel.verticalLayout {
            + diffusePanel
            - transparencyLabel
            + transparencySlider
            + ambientPanel
            + emissivePanel
            + specularPanel
        }

        this.scrollPane.setViewportView(panel)

        this.showSphere()
    }

    override fun applyInside(panel : JPanel)
    {
        panel.layout = GridLayout(0, 1)
        panel.add(this.scrollPane)


    }

    private fun showSphere()
    {
        this.showNode(MaterialEditor.sphere)
    }

    private fun showBoxRepeatOnFace()
    {
        MaterialEditor.box.boxUV(BoxUV())
        this.showNode(MaterialEditor.box)
    }

    private fun showBoxCross()
    {
        MaterialEditor.box.boxUV(CrossUV())
        this.showNode(MaterialEditor.box)
    }

    private fun showNode(node : Node)
    {
        this.mainNode.removeAllChildren()
        this.mainNode.addChild(node)
        this.mainNode.applyMaterialHierarchically(this.materialObservableData.value())
    }

    private fun updateMaterial()
    {
        val material = this.materialObservableData.value()
        material.transparency = this.transparencySlider.value.toFloat() / 100f
        material.colorDiffuse = Color4f(this.diffuseColor.color)

        if (this.textureDiffuseCheckBox.isSelected)
        {
            material.textureDiffuse = this.textureDiffuse.textureObservable.value()
        }
        else
        {
            material.textureDiffuse = null
        }

        material.colorAmbient = Color4f(this.ambientColor.color)
        material.colorEmissive = Color4f(this.emissiveColor.color)
        material.colorSpecular = Color4f(this.specularColor.color)
        material.specularLevel = this.specularSlider.value.toFloat() / 100f

        this.mainNode.applyMaterialHierarchically(material)
        this.materialObservableData.value(material)
    }
}
