package khelp.ui.dsl

import khelp.ui.layout.constraints.ConstraintsSize

class ConstraintsLayoutConstraintCreator
{
    companion object
    {
        internal const val PARENT = "#*-<{PARENT}>-*#"
        internal const val FREE = "#*-<{FREE}>-*#"
    }

    var horizontalSize = ConstraintsSize.EXPANDED
    var verticalSize = ConstraintsSize.EXPANDED
    var marginTop : Int = 0
    var marginLeft : Int = 0
    var marginRight : Int = 0
    var marginBottom : Int = 0

    internal var top =
        ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.PARENT,
                                                    ConstraintsLayoutConstraintCreatorAnchor.TOP_TOP)
    internal var bottom =
        ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.PARENT,
                                                    ConstraintsLayoutConstraintCreatorAnchor.BOTTOM_BOTTOM)
    internal var left =
        ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.PARENT,
                                                    ConstraintsLayoutConstraintCreatorAnchor.LEFT_LEFT)
    internal var right =
        ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.PARENT,
                                                    ConstraintsLayoutConstraintCreatorAnchor.RIGHT_RIGHT)
    val topAtParent : Unit
        get()
        {
            this.top =
                ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.PARENT,
                                                            ConstraintsLayoutConstraintCreatorAnchor.TOP_TOP)
        }

    val topFree : Unit
        get()
        {
            this.top =
                ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.FREE,
                                                            ConstraintsLayoutConstraintCreatorAnchor.TOP_TOP)
        }

    var topAtTop : String
        get() = this.top.reference
        set(value)
        {
            this.top =
                ConstraintsLayoutConstraintCreatorReference(value, ConstraintsLayoutConstraintCreatorAnchor.TOP_TOP)
        }

    var topAtCenter : String
        get() = this.top.reference
        set(value)
        {
            this.top =
                ConstraintsLayoutConstraintCreatorReference(value, ConstraintsLayoutConstraintCreatorAnchor.TOP_CENTER)
        }

    var topAtBottom : String
        get() = this.top.reference
        set(value)
        {
            this.top =
                ConstraintsLayoutConstraintCreatorReference(value, ConstraintsLayoutConstraintCreatorAnchor.TOP_BOTTOM)
        }

    val bottomAtParent : Unit
        get()
        {
            this.bottom =
                ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.PARENT,
                                                            ConstraintsLayoutConstraintCreatorAnchor.BOTTOM_BOTTOM)
        }

    val bottomFree : Unit
        get()
        {
            this.bottom =
                ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.FREE,
                                                            ConstraintsLayoutConstraintCreatorAnchor.BOTTOM_BOTTOM)
        }

    var bottomAtTop : String
        get() = this.bottom.reference
        set(value)
        {
            this.bottom =
                ConstraintsLayoutConstraintCreatorReference(value, ConstraintsLayoutConstraintCreatorAnchor.BOTTOM_TOP)
        }

    var bottomAtCenter : String
        get() = this.bottom.reference
        set(value)
        {
            this.bottom =
                ConstraintsLayoutConstraintCreatorReference(value,
                                                            ConstraintsLayoutConstraintCreatorAnchor.BOTTOM_CENTER)
        }

    var bottomAtBottom : String
        get() = this.bottom.reference
        set(value)
        {
            this.bottom =
                ConstraintsLayoutConstraintCreatorReference(value,
                                                            ConstraintsLayoutConstraintCreatorAnchor.BOTTOM_BOTTOM)
        }

    val leftAtParent : Unit
        get()
        {
            this.left =
                ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.PARENT,
                                                            ConstraintsLayoutConstraintCreatorAnchor.LEFT_LEFT)
        }

    val leftFree : Unit
        get()
        {
            this.left =
                ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.FREE,
                                                            ConstraintsLayoutConstraintCreatorAnchor.LEFT_LEFT)
        }

    var leftAtLeft : String
        get() = this.left.reference
        set(value)
        {
            this.left =
                ConstraintsLayoutConstraintCreatorReference(value, ConstraintsLayoutConstraintCreatorAnchor.LEFT_LEFT)
        }

    var leftAtCenter : String
        get() = this.left.reference
        set(value)
        {
            this.left =
                ConstraintsLayoutConstraintCreatorReference(value, ConstraintsLayoutConstraintCreatorAnchor.LEFT_CENTER)
        }

    var leftAtRight : String
        get() = this.left.reference
        set(value)
        {
            this.left =
                ConstraintsLayoutConstraintCreatorReference(value, ConstraintsLayoutConstraintCreatorAnchor.LEFT_RIGHT)
        }

    val rightAtParent : Unit
        get()
        {
            this.right =
                ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.PARENT,
                                                            ConstraintsLayoutConstraintCreatorAnchor.RIGHT_RIGHT)
        }

    val rightFree : Unit
        get()
        {
            this.right =
                ConstraintsLayoutConstraintCreatorReference(ConstraintsLayoutConstraintCreator.FREE,
                                                            ConstraintsLayoutConstraintCreatorAnchor.RIGHT_RIGHT)
        }

    var rightAtLeft : String
        get() = this.right.reference
        set(value)
        {
            this.right =
                ConstraintsLayoutConstraintCreatorReference(value, ConstraintsLayoutConstraintCreatorAnchor.RIGHT_LEFT)
        }

    var rightAtCenter : String
        get() = this.right.reference
        set(value)
        {
            this.right =
                ConstraintsLayoutConstraintCreatorReference(value,
                                                            ConstraintsLayoutConstraintCreatorAnchor.RIGHT_CENTER)
        }

    var rightAtRight : String
        get() = this.right.reference
        set(value)
        {
            this.right =
                ConstraintsLayoutConstraintCreatorReference(value, ConstraintsLayoutConstraintCreatorAnchor.RIGHT_RIGHT)
        }
}