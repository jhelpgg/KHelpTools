package khelp.image

import java.awt.geom.Arc2D

enum class ArcType(val arcTypeCode : Int)
{
    OPEN(Arc2D.OPEN),
    CHORD(Arc2D.CHORD),
    PIE(Arc2D.PIE)
}
