package khelp.engine3d.gui

import java.util.Objects

class GUIMargin(val left : Int = 0, val right : Int = 0, val top : Int = 0, val bottom : Int = 0)
{
    val width = this.left + this.right
    val height = this.top + this.bottom

    override fun equals(other : Any?) : Boolean =
        when
        {
            this === other                       -> true
            other == null || other !is GUIMargin -> false
            else                                 -> this.left == other.left && this.right == other.right &&
                                                    this.top == other.top && this.bottom == other.bottom
        }

    fun left(left : Int) : GUIMargin =
        GUIMargin(left = left, right = this.right, top = this.top, bottom = this.bottom)

    fun right(right : Int) : GUIMargin =
        GUIMargin(left = this.left, right = right, top = this.top, bottom = this.bottom)

    fun top(top : Int) : GUIMargin =
        GUIMargin(left = this.left, right = this.right, top = top, bottom = this.bottom)

    fun bottom(bottom : Int) : GUIMargin =
        GUIMargin(left = this.left, right = this.right, top = this.top, bottom = bottom)

    override fun hashCode() : Int = Objects.hash(this.left, this.right, this.top, this.bottom)

    override fun toString() : String =
        "Margin : left=${this.left}, top=${this.top}, right=${this.right}, bottom=${this.bottom}"
}
