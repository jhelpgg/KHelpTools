package khelp.engine3d.utils.delaunay.adjacent

import khelp.engine3d.utils.delaunay.PointIndexed

data class AdjacentSide(val firstFree : PointIndexed,
                        val side1 : PointIndexed,
                        val side2 : PointIndexed,
                        val secondFree : PointIndexed) : Adjacent
