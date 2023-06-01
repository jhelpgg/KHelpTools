package khelp.mobile.game.editor.ui

import khelp.engine3d.render.Node
import khelp.engine3d.render.window3DFull
import khelp.mobile.game.editor.EDITOR_TEXTS
import khelp.mobile.game.editor.MAIN_NODE
import khelp.mobile.game.editor.MENU_FONT
import khelp.mobile.game.editor.extensions.add
import khelp.mobile.game.editor.models.shared.NavigatorModel
import khelp.mobile.game.editor.ui.screens.ScreenManager
import khelp.utilities.provider.provided

object MobileGameEditor
{
    private val navigatorModel : NavigatorModel by provided<NavigatorModel>()

    fun show()
    {
        window3DFull("Mobile game editor") {
            this@MobileGameEditor.navigatorModel.window3D(this)

            this.gui.menuBar(resourcesText = EDITOR_TEXTS,
                             font = MENU_FONT) {
                "file" {
                    add("newProject", "new.png", this@MobileGameEditor.navigatorModel::newProject)
                    action("openProject", this@MobileGameEditor.navigatorModel::openProject)
                    action("save", this@MobileGameEditor.navigatorModel::save)
                    action("saveAs", this@MobileGameEditor.navigatorModel::saveAs)
                    action("exit", this@MobileGameEditor.navigatorModel::exit)
                }

                "scene" {
                    action("addNode", this@MobileGameEditor.navigatorModel::addNode)
                    action("addBox", this@MobileGameEditor.navigatorModel::addBox)
                    action("addSphere", this@MobileGameEditor.navigatorModel::addSphere)
                    action("addRevolution", this@MobileGameEditor.navigatorModel::addRevolution)
                    action("addObject", this@MobileGameEditor.navigatorModel::addObject)
                }

                "resources" {
                    action("projectTextures", this@MobileGameEditor.navigatorModel::projectTextures)
                    action("importTexture", this@MobileGameEditor.navigatorModel::importTexture)
                }

                "editors" {
                    action("editMaterial", this@MobileGameEditor.navigatorModel::editMaterial)
                    action("editTexture", this@MobileGameEditor.navigatorModel::editTexture)
                }
            }

            this.scene.root.z = - 2f
            this.scene.root.addChild(Node(MAIN_NODE))

            ScreenManager(this)
        }
    }
}
