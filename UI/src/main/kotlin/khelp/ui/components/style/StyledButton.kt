package khelp.ui.components.style

import khelp.resources.ResourcesText
import khelp.thread.TaskContext
import khelp.thread.observable.Observable
import khelp.thread.observable.ObservableData
import khelp.ui.style.ComponentHighLevel
import khelp.ui.style.StyleImageWithTextClickable
import khelp.utilities.log.verbose
import java.awt.event.MouseEvent

class StyledButton(keyText : String, resourcesText : ResourcesText, style : StyleImageWithTextClickable)
    : StyledLabel<StyleImageWithTextClickable>(keyText, resourcesText, style)
{
    private val styledButtonStateObservableData = ObservableData<StyledButtonState>(StyledButtonState.OUT_OF)
    val styledButtonStateObservable : Observable<StyledButtonState> = this.styledButtonStateObservableData.observable
    private var taskContext : TaskContext = TaskContext.INDEPENDENT
    private var onClick : (StyledButton) -> Unit = {}

    init
    {
        val styledComponentMouseManager = StyledComponentMouseManager()
        styledComponentMouseManager.mouseEvent = this::mouseEvent
        this.addMouseListener(styledComponentMouseManager)

        this.style.background = this.style.outOfBackground
        this.style.componentHighLevel = ComponentHighLevel.HIGHEST
        this.style.changeStyleObservable.observedBy(TaskContext.INDEPENDENT) { this.styleChanged() }
    }

    fun onClick(taskContext : TaskContext, action : (StyledButton) -> Unit)
    {
        this.taskContext = taskContext
        this.onClick = action
    }

    private fun mouseEvent(mouseEvent : MouseEvent)
    {
        when (mouseEvent.id)
        {
            MouseEvent.MOUSE_PRESSED                            ->
            {
                this.styledButtonStateObservableData.value(StyledButtonState.CLICK)
                this.styleChanged()
                this.taskContext.parallel { this.onClick(this) }
            }
            MouseEvent.MOUSE_ENTERED, MouseEvent.MOUSE_RELEASED ->
            {
                this.styledButtonStateObservableData.value(StyledButtonState.OVER)
                this.styleChanged()
            }
            MouseEvent.MOUSE_EXITED                             ->
            {
                this.styledButtonStateObservableData.value(StyledButtonState.OUT_OF)
                this.styleChanged()
            }
        }
    }

    private fun styleChanged()
    {
        when (this.styledButtonStateObservableData.value())
        {
            StyledButtonState.OUT_OF ->
            {
                this.style.background = this.style.outOfBackground
                this.style.componentHighLevel = ComponentHighLevel.HIGHEST
            }
            StyledButtonState.OVER   ->
            {
                this.style.background = this.style.overBackground
                this.style.componentHighLevel = ComponentHighLevel.FLY
            }
            StyledButtonState.CLICK  ->
            {
                this.style.background = this.style.clickBackground
                this.style.componentHighLevel = ComponentHighLevel.NEAR_GROUND
            }
        }
    }
}
