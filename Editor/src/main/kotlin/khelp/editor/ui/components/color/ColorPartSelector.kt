package khelp.editor.ui.components.color

import khelp.editor.ui.Editor
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.ui.components.JLabel
import khelp.ui.layout.table.TableLayoutConstraint
import khelp.utilities.extensions.bounds
import javax.swing.JPanel
import javax.swing.JSlider
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel

internal class ColorPartSelector(private val key : String)
{
    private val valueSlider = JSlider(0, 255, 128)
    private val valueSpinner = JSpinner(SpinnerNumberModel(128, 0, 255, 1))
    private val valueObservableData = ObservableData<Int>(128)
    val valueObservable : Observable<Int> = this.valueObservableData.observable
    var value : Int
        get() = this.valueObservableData.value()
        set(value)
        {
            val newValue = value.bounds(0, 255)

            if (newValue != this.valueObservableData.value())
            {
                this.valueObservableData.value(newValue)
                this.updateValue()
            }
        }

    init
    {
        this.valueSlider.addChangeListener { this.value = this.valueSlider.value }
        this.valueSpinner.addChangeListener { this.value = this.valueSpinner.value as? Int ?: this.value }
    }

    fun pushInside(panel : JPanel, y : Int)
    {
        panel.add(JLabel(this.key, Editor.resourcesText), TableLayoutConstraint(0, y, 2, 1))
        panel.add(this.valueSpinner, TableLayoutConstraint(2, y, 1, 1))
        panel.add(this.valueSlider, TableLayoutConstraint(3, y, 3, 1))
    }

    private fun updateValue()
    {
        this.valueSlider.value = this.value
        this.valueSpinner.value = this.value
    }
}