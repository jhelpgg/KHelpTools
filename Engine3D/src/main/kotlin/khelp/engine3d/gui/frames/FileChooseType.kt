package khelp.engine3d.gui.frames

import javax.swing.filechooser.FileFilter
import javax.swing.filechooser.FileNameExtensionFilter

enum class FileChooseType(val fileFilter : FileFilter)
{
    IMAGE_AND_OBJ(FileNameExtensionFilter("Image/Obj", "jpg", "jpeg", "png", "obj")),
    IMAGE_ONLY(FileNameExtensionFilter("Image", "jpg", "jpeg", "png")),
    OBJ_ONLY(FileNameExtensionFilter("Obj", "obj"))
}
