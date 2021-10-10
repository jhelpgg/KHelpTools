package khelp.editor.ui.materialeditor

import khelp.editor.ui.Editor
import khelp.editor.ui.ScreenEditor
import khelp.editor.ui.components.TextureSelector
import khelp.editor.ui.components.color.Color4fPreview
import khelp.engine3d.render.Material
import khelp.engine3d.render.Node
import khelp.engine3d.render.prebuilt.Box
import khelp.engine3d.render.prebuilt.BoxUV
import khelp.engine3d.render.prebuilt.CrossUV
import khelp.engine3d.render.prebuilt.Sphere
import khelp.thread.TaskContext
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.ui.GenericAction
import khelp.ui.components.JLabel
import khelp.ui.dsl.borderLayout
import khelp.ui.dsl.tableLayout
import khelp.ui.events.MouseManager
import khelp.ui.extensions.addTitle
import java.awt.FlowLayout
import javax.swing.JButton
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

    private val diffuseColor = Color4fPreview(true)
    private val textureDiffuseCheckBox = JCheckBox()
    private val textureDiffuse = TextureSelector()

    private val transparencySlider = JSlider(0, 100, 100)

    private val ambientColor = Color4fPreview(true)
    private val emissiveColor = Color4fPreview(true)

    private val specularColor = Color4fPreview(true)
    private val specularSlider = JSlider(0, 100, 10)

    private val shininessSlider = JSlider(0, 128, 12)

    private val sphericRateSlider = JSlider(0, 100, 100)
    private val textureSphericCheckBox = JCheckBox()
    private val textureSpheric = TextureSelector()

    private val objectsPanel = JPanel()
    private val scrollPane : JScrollPane

    override val manipulatedNode : Node get() = this.mainNode

    private fun attachColorComponent(color4fPreview : Color4fPreview)
    {
        MouseManager.attachTo(color4fPreview)
            .mouseStateObservable
            .observedBy(TaskContext.INDEPENDENT) { mouseState ->
                if (mouseState.clicked)
                {
                    Editor.chooseColor(color4fPreview.color)
                        .and { color4f ->
                            color4fPreview.color = color4f
                            this.updateMaterial()
                        }
                }
            }
    }

    init
    {
        val actionSphere = GenericAction(Editor.resourcesText, "sphere", TaskContext.INDEPENDENT) { this.showSphere() }
        val actionBox =
            GenericAction(Editor.resourcesText, "box", TaskContext.INDEPENDENT) { this.showBoxRepeatOnFace() }
        val actionCross = GenericAction(Editor.resourcesText, "cross", TaskContext.INDEPENDENT) { this.showBoxCross() }

        this.objectsPanel.layout = FlowLayout()
        this.objectsPanel.add(JButton(actionSphere))
        this.objectsPanel.add(JButton(actionBox))
        this.objectsPanel.add(JButton(actionCross))

        val material = this.materialObservableData.value()
        this.diffuseColor.color = material.colorDiffuse
        this.ambientColor.color = material.colorAmbient
        this.emissiveColor.color = material.colorEmissive
        this.specularColor.color = material.colorSpecular

        this.attachColorComponent(this.diffuseColor)

        this.textureDiffuseCheckBox.addChangeListener { this.updateMaterial() }
        this.textureDiffuse.textureObservable.observedBy(TaskContext.INDEPENDENT) { this.updateMaterial() }

        val transparencyLabel = javax.swing.JLabel("100", javax.swing.JLabel.CENTER)
        this.transparencySlider.addChangeListener {
            transparencyLabel.text = this.transparencySlider.value.toString()
            this.updateMaterial()
        }

        this.attachColorComponent(this.ambientColor)
        this.attachColorComponent(this.emissiveColor)
        this.attachColorComponent(this.specularColor)

        val specularLabel = javax.swing.JLabel("10", javax.swing.JLabel.CENTER)
        this.specularSlider.addChangeListener {
            specularLabel.text = this.specularSlider.value.toString()
            this.updateMaterial()
        }

        val shininessLabel = javax.swing.JLabel("12", javax.swing.JLabel.CENTER)
        this.shininessSlider.addChangeListener {
            shininessLabel.text = this.shininessSlider.value.toString()
            this.updateMaterial()
        }

        val sphericRateLabel = javax.swing.JLabel("100", javax.swing.JLabel.CENTER)
        this.sphericRateSlider.addChangeListener {
            sphericRateLabel.text = this.sphericRateSlider.value.toString()
            this.updateMaterial()
        }
        this.textureSphericCheckBox.addChangeListener { this.updateMaterial() }
        this.textureSpheric.textureObservable.observedBy(TaskContext.INDEPENDENT) { this.updateMaterial() }


        // ...

        val diffusePanel = JPanel(FlowLayout())
        diffusePanel.add(this.diffuseColor)
        diffusePanel.add(this.textureDiffuseCheckBox)
        diffusePanel.add(this.textureDiffuse)
        diffusePanel.addTitle("diffuse", Editor.resourcesText)

        val transparencyPanel = JPanel()
        transparencyPanel.borderLayout {
            pageStart(transparencyLabel)
            center(transparencySlider)
        }
        transparencyPanel.addTitle("transparency", Editor.resourcesText)

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
        specularPanel.addTitle("specular", Editor.resourcesText)

        val shininessPanel = JPanel()
        shininessPanel.borderLayout {
            pageStart(shininessLabel)
            center(shininessSlider)
        }
        shininessPanel.addTitle("shininess", Editor.resourcesText)

        val sphericPanel = JPanel()
        sphericPanel.borderLayout {
            lineStart(textureSphericCheckBox)
            pageEnd {
                borderLayout {
                    pageStart(sphericRateLabel)
                    pageEnd(sphericRateSlider)
                }
            }
            center(textureSpheric)
        }
        sphericPanel.addTitle("spheric", Editor.resourcesText)

        val panel = JPanel()
        panel.tableLayout {
            diffusePanel.cell(0, 0, 1, 3)
            transparencyPanel.cell(0, 3, 1, 2)
            ambientPanel.cell(0, 5, 1, 1)
            emissivePanel.cell(0, 5, 1, 1)
            specularPanel.cell(0, 6, 1, 2)
            shininessPanel.cell(0, 8, 1, 2)
            sphericPanel.cell(0, 10, 1, 3)
        }

        this.scrollPane =
            JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)

        this.showSphere()
    }

    override fun applyInside(panel : JPanel)
    {
        panel.borderLayout {
            pageStart(objectsPanel)
            center(scrollPane)
        }
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
        material.colorDiffuse = this.diffuseColor.color

        if (this.textureDiffuseCheckBox.isSelected)
        {
            material.textureDiffuse = this.textureDiffuse.textureObservable.value()
        }
        else
        {
            material.textureDiffuse = null
        }

        material.colorAmbient = this.ambientColor.color
        material.colorEmissive = this.emissiveColor.color
        material.colorSpecular = this.specularColor.color
        material.specularLevel = this.specularSlider.value.toFloat() / 100f
        material.shininess = this.shininessSlider.value
        material.sphericRate = this.sphericRateSlider.value.toFloat() / 100f

        if (this.textureSphericCheckBox.isSelected)
        {
            material.textureSpheric = this.textureSpheric.textureObservable.value()
        }
        else
        {
            material.textureSpheric = null
        }

        this.mainNode.applyMaterialHierarchically(material)
        this.materialObservableData.value(material)
    }
}
