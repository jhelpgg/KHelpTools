package khelp.ui.layout.constraints

class ConstraintsLayoutConstraint
{
    var horizontalSize : ConstraintsSize = ConstraintsSize.EXPANDED
    var verticalSize : ConstraintsSize = ConstraintsSize.EXPANDED
    var leftConstraint : LeftConstraint = LeftAtParent
    var rightConstraint : RightConstraint = RightAtParent
    var topConstraint : TopConstraint = TopAtParent
    var bottomConstraint : BottomConstraint = BottomAtParent

    internal var x = 0
    internal var y = 0
    internal var xMax = 0
    internal var yMax = 0
    internal var computed = false
    internal var constraintsLeft : ConstraintsLayoutConstraint? = null
    internal var constraintsRight : ConstraintsLayoutConstraint? = null
    internal var constraintsTop : ConstraintsLayoutConstraint? = null
    internal var constraintsBottom : ConstraintsLayoutConstraint? = null
    internal val allDependencyComputed : Boolean
        get() =
            (this.constraintsLeft?.computed ?: true)
            && (this.constraintsRight?.computed ?: true)
            && (this.constraintsTop?.computed ?: true)
            && (this.constraintsBottom?.computed ?: true)
}
