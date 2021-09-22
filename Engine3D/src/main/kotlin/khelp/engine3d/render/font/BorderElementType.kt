package khelp.engine3d.render.font

import java.awt.geom.PathIterator

enum class BorderElementType(val type:Int)
{
    CLOSE(PathIterator.SEG_CLOSE),
    MOVE(PathIterator.SEG_MOVETO),
    LINE(PathIterator.SEG_LINETO)
}

fun borderElementType(type:Int) : BorderElementType
{
    for(borderElementType in BorderElementType.values())
    {
        if(borderElementType.type == type)
        {
            return borderElementType
        }
    }

    return BorderElementType.CLOSE
}
