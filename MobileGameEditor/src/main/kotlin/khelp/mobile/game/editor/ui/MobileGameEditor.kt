package khelp.mobile.game.editor.ui

import khelp.engine3d.render.Node
import khelp.engine3d.render.window3DFull
import khelp.mobile.game.editor.EDITOR_TEXTS
import khelp.mobile.game.editor.KEY_TEXT_ADD_BOX
import khelp.mobile.game.editor.KEY_TEXT_ADD_NODE
import khelp.mobile.game.editor.KEY_TEXT_ADD_OBJECT
import khelp.mobile.game.editor.KEY_TEXT_ADD_REVOLUTION
import khelp.mobile.game.editor.KEY_TEXT_ADD_SPHERE
import khelp.mobile.game.editor.KEY_TEXT_EDITORS
import khelp.mobile.game.editor.KEY_TEXT_EDIT_MATERIAL
import khelp.mobile.game.editor.KEY_TEXT_EDIT_TEXTURE
import khelp.mobile.game.editor.KEY_TEXT_EXIT
import khelp.mobile.game.editor.KEY_TEXT_FILE
import khelp.mobile.game.editor.KEY_TEXT_IMPORT_OBJ
import khelp.mobile.game.editor.KEY_TEXT_IMPORT_TEXTURE
import khelp.mobile.game.editor.KEY_TEXT_NEW_PROJECT
import khelp.mobile.game.editor.KEY_TEXT_OPEN_PROJECT
import khelp.mobile.game.editor.KEY_TEXT_PROJECT_TEXTURES
import khelp.mobile.game.editor.KEY_TEXT_RESOURCES
import khelp.mobile.game.editor.KEY_TEXT_SAVE
import khelp.mobile.game.editor.KEY_TEXT_SAVE_AS
import khelp.mobile.game.editor.KEY_TEXT_SCENE
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
                KEY_TEXT_FILE {
                    add(KEY_TEXT_NEW_PROJECT, "new.png", this@MobileGameEditor.navigatorModel::newProject)
                    action(KEY_TEXT_OPEN_PROJECT, this@MobileGameEditor.navigatorModel::openProject)
                    action(KEY_TEXT_SAVE, this@MobileGameEditor.navigatorModel::save)
                    action(KEY_TEXT_SAVE_AS, this@MobileGameEditor.navigatorModel::saveAs)
                    action(KEY_TEXT_EXIT, this@MobileGameEditor.navigatorModel::exit)
                }

                KEY_TEXT_SCENE {
                    action(KEY_TEXT_ADD_NODE, this@MobileGameEditor.navigatorModel::addNode)
                    action(KEY_TEXT_ADD_BOX, this@MobileGameEditor.navigatorModel::addBox)
                    action(KEY_TEXT_ADD_SPHERE, this@MobileGameEditor.navigatorModel::addSphere)
                    action(KEY_TEXT_ADD_REVOLUTION, this@MobileGameEditor.navigatorModel::addRevolution)
                    action(KEY_TEXT_ADD_OBJECT, this@MobileGameEditor.navigatorModel::addObject)
                }

                KEY_TEXT_RESOURCES {
                    action(KEY_TEXT_PROJECT_TEXTURES, this@MobileGameEditor.navigatorModel::projectTextures)
                    action(KEY_TEXT_IMPORT_TEXTURE, this@MobileGameEditor.navigatorModel::importTexture)
                    action(KEY_TEXT_IMPORT_OBJ, this@MobileGameEditor.navigatorModel::importOBJ)
                }

                KEY_TEXT_EDITORS {
                    action(KEY_TEXT_EDIT_MATERIAL, this@MobileGameEditor.navigatorModel::editMaterial)
                    action(KEY_TEXT_EDIT_TEXTURE, this@MobileGameEditor.navigatorModel::editTexture)
                }
            }

            this.scene.root.z = - 2f
            this.scene.root.addChild(Node(MAIN_NODE))

            ScreenManager(this)
        }
    }
}
