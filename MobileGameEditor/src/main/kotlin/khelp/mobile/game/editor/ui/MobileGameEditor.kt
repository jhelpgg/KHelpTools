package khelp.mobile.game.editor.ui

import khelp.engine3d.render.Node
import khelp.engine3d.render.window3DFull
import khelp.mobile.game.editor.EDITOR_TEXTS
import khelp.mobile.game.editor.MAIN_NODE
import khelp.mobile.game.editor.MENU_FONT
import khelp.mobile.game.editor.models.shared.NavigatorModel
import khelp.mobile.game.editor.ui.screens.ScreenManager
import khelp.thread.TaskContext
import khelp.ui.GenericAction
import khelp.utilities.provider.provided

object MobileGameEditor
{
    private val navigatorModel : NavigatorModel by provided<NavigatorModel>()

    fun show()
    {
        window3DFull("Mobile game editor") {
            this.gui.menuBar(resourcesText = EDITOR_TEXTS,
                             font = MENU_FONT) {
                "file" {
                    + GenericAction(EDITOR_TEXTS, "newProject", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::newProject)
                    + GenericAction(EDITOR_TEXTS, "openProject", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::openProject)
                    + GenericAction(EDITOR_TEXTS, "save", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::save)
                    + GenericAction(EDITOR_TEXTS, "saveAs", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::saveAs)
                    + GenericAction(EDITOR_TEXTS, "exit", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::exit)
                }

                "scene" {
                    + GenericAction(EDITOR_TEXTS, "addNode", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::addNode)
                    + GenericAction(EDITOR_TEXTS, "addBox", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::addBox)
                    + GenericAction(EDITOR_TEXTS, "addSphere", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::addSphere)
                    + GenericAction(EDITOR_TEXTS, "addRevolution", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::addRevolution)
                    + GenericAction(EDITOR_TEXTS, "addObject", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::addObject)
                }

                "resources" {
                    + GenericAction(EDITOR_TEXTS, "projectTextures", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::projectTextures)
                    + GenericAction(EDITOR_TEXTS, "importTexture", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::importTexture)
                }

                "editors" {
                    + GenericAction(EDITOR_TEXTS, "editMaterial", TaskContext.INDEPENDENT,
                                    this@MobileGameEditor.navigatorModel::editMaterial)
                }
            }

            this.scene.root.z = -2f
            this.scene.root.addChild(Node(MAIN_NODE))

            ScreenManager(this)
        }
    }
}
