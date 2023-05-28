package khelp.engine3d.gui.component

import khelp.engine3d.gui.GUI
import khelp.engine3d.gui.layout.GUIConstraints
import khelp.engine3d.gui.layout.GUILayout
import khelp.thread.observable.ObservableData

class GUIDialog<C : GUIConstraints, L : GUILayout<C>> internal constructor(internal val panel : GUIComponentPanel<C, L>,
                                                                           private val gui : GUI)
{
    private val showObservableData = ObservableData<Boolean>(false)
    val showing = this.showObservableData.observable

    fun show()
    {
        if (this.showObservableData.valueIf(value = true) { value -> ! value })
        {
            this.gui.show(this)
        }
    }

    fun close()
    {
        if (this.showObservableData.valueIf(value = false) { value -> value })
        {
            this.gui.hide(this)
        }
    }

}
