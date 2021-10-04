package khelp.ui.dsl

import khelp.ui.layout.constraints.BottomAtParent
import khelp.ui.layout.constraints.BottomFree
import khelp.ui.layout.constraints.BottomToBottomOf
import khelp.ui.layout.constraints.BottomToCenterOf
import khelp.ui.layout.constraints.BottomToTopOf
import khelp.ui.layout.constraints.ConstraintsLayout
import khelp.ui.layout.constraints.ConstraintsLayoutConstraint
import khelp.ui.layout.constraints.LeftAtParent
import khelp.ui.layout.constraints.LeftFree
import khelp.ui.layout.constraints.LeftToCenterOf
import khelp.ui.layout.constraints.LeftToLeftOf
import khelp.ui.layout.constraints.LeftToRightOf
import khelp.ui.layout.constraints.RightAtParent
import khelp.ui.layout.constraints.RightFree
import khelp.ui.layout.constraints.RightToCenterOf
import khelp.ui.layout.constraints.RightToLeftOf
import khelp.ui.layout.constraints.RightToRightOf
import khelp.ui.layout.constraints.TopAtParent
import khelp.ui.layout.constraints.TopFree
import khelp.ui.layout.constraints.TopToBottomOf
import khelp.ui.layout.constraints.TopToCenterOf
import khelp.ui.layout.constraints.TopToTopOf
import java.awt.Component
import java.awt.Container
import javax.swing.JComponent
import javax.swing.JPanel


class ConstraintsLayoutCreator internal constructor(private val container : Container)
{
    private val components = HashMap<String, Pair<Component, ConstraintsLayoutConstraintCreator>>()

    init
    {
        this.container.layout = ConstraintsLayout()
    }

    operator fun Component.invoke(componentId : String, creator : ConstraintsLayoutConstraintCreator.() -> Unit)
    {
        this.name = componentId
        val constraintsLayoutConstraintCreator = ConstraintsLayoutConstraintCreator()
        creator(constraintsLayoutConstraintCreator)
        this@ConstraintsLayoutCreator.components[componentId] = Pair(this, constraintsLayoutConstraintCreator)
    }

    fun add(component : JComponent, componentId : String, creator : ConstraintsLayoutConstraintCreator.() -> Unit)
    {
        component.name = componentId
        val constraintsLayoutConstraintCreator = ConstraintsLayoutConstraintCreator()
        creator(constraintsLayoutConstraintCreator)
        this.components[componentId] = Pair(component, constraintsLayoutConstraintCreator)
    }

    fun panel(panelId : String,
              panelCreator : JPanel.() -> Unit,
              creator : ConstraintsLayoutConstraintCreator.() -> Unit)
    {
        val panel = JPanel()
        panel.name = panelId
        panelCreator(panel)
        val constraintsLayoutConstraintCreator = ConstraintsLayoutConstraintCreator()
        creator(constraintsLayoutConstraintCreator)
        this.components[panelId] = Pair(panel, constraintsLayoutConstraintCreator)
    }

    fun create()
    {
        for ((component, constraintCreator) in this.components.values)
        {
            val constraintsLayoutConstraint = ConstraintsLayoutConstraint()

            constraintsLayoutConstraint.horizontalSize = constraintCreator.horizontalSize
            constraintsLayoutConstraint.verticalSize = constraintCreator.verticalSize
            constraintsLayoutConstraint.marginTop = constraintCreator.marginTop
            constraintsLayoutConstraint.marginLeft = constraintCreator.marginLeft
            constraintsLayoutConstraint.marginBottom = constraintCreator.marginBottom
            constraintsLayoutConstraint.marginRight = constraintCreator.marginRight

            constraintsLayoutConstraint.topConstraint =
                when (constraintCreator.top.reference)
                {
                    ConstraintsLayoutConstraintCreator.PARENT -> TopAtParent
                    ConstraintsLayoutConstraintCreator.FREE   -> TopFree
                    else                                      ->
                        this.components[constraintCreator.top.reference]?.let { (componentTop, _) ->
                            when (constraintCreator.top.anchor)
                            {
                                ConstraintsLayoutConstraintCreatorAnchor.TOP_TOP    -> TopToTopOf(componentTop)
                                ConstraintsLayoutConstraintCreatorAnchor.TOP_CENTER -> TopToCenterOf(componentTop)
                                ConstraintsLayoutConstraintCreatorAnchor.TOP_BOTTOM -> TopToBottomOf(componentTop)
                                else                                                -> TopFree
                            }
                        } ?: TopAtParent
                }

            constraintsLayoutConstraint.leftConstraint =
                when (constraintCreator.left.reference)
                {
                    ConstraintsLayoutConstraintCreator.PARENT -> LeftAtParent
                    ConstraintsLayoutConstraintCreator.FREE   -> LeftFree
                    else                                      ->
                        this.components[constraintCreator.left.reference]?.let { (componentLeft, _) ->
                            when (constraintCreator.left.anchor)
                            {
                                ConstraintsLayoutConstraintCreatorAnchor.LEFT_LEFT   -> LeftToLeftOf(componentLeft)
                                ConstraintsLayoutConstraintCreatorAnchor.LEFT_CENTER -> LeftToCenterOf(componentLeft)
                                ConstraintsLayoutConstraintCreatorAnchor.LEFT_RIGHT  -> LeftToRightOf(componentLeft)
                                else                                                 -> LeftFree
                            }
                        } ?: LeftAtParent
                }

            constraintsLayoutConstraint.bottomConstraint =
                when (constraintCreator.bottom.reference)
                {
                    ConstraintsLayoutConstraintCreator.PARENT -> BottomAtParent
                    ConstraintsLayoutConstraintCreator.FREE   -> BottomFree
                    else                                      ->
                        this.components[constraintCreator.bottom.reference]?.let { (componentBottom, _) ->
                            when (constraintCreator.bottom.anchor)
                            {
                                ConstraintsLayoutConstraintCreatorAnchor.BOTTOM_TOP    ->
                                    BottomToTopOf(componentBottom)
                                ConstraintsLayoutConstraintCreatorAnchor.BOTTOM_CENTER ->
                                    BottomToCenterOf(componentBottom)
                                ConstraintsLayoutConstraintCreatorAnchor.BOTTOM_BOTTOM ->
                                    BottomToBottomOf(componentBottom)
                                else                                                   ->
                                    BottomFree
                            }
                        } ?: BottomAtParent
                }

            constraintsLayoutConstraint.rightConstraint =
                when (constraintCreator.right.reference)
                {
                    ConstraintsLayoutConstraintCreator.PARENT -> RightAtParent
                    ConstraintsLayoutConstraintCreator.FREE   -> RightFree
                    else                                      ->
                        this.components[constraintCreator.right.reference]?.let { (componentTop, _) ->
                            when (constraintCreator.right.anchor)
                            {
                                ConstraintsLayoutConstraintCreatorAnchor.RIGHT_LEFT   -> RightToLeftOf(componentTop)
                                ConstraintsLayoutConstraintCreatorAnchor.RIGHT_CENTER -> RightToCenterOf(componentTop)
                                ConstraintsLayoutConstraintCreatorAnchor.RIGHT_RIGHT  -> RightToRightOf(componentTop)
                                else                                                  -> RightFree
                            }
                        } ?: RightAtParent
                }

            this.container.add(component, constraintsLayoutConstraint)
        }
    }
}
