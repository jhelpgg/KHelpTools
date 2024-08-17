package khelp.algorithm.delaunay.adjacent

import khelp.algorithm.delaunay.PointIndexed

data class AdjacentSide(val firstFree : PointIndexed,
                        val side1 : PointIndexed,
                        val side2 : PointIndexed,
                        val secondFree : PointIndexed) : Adjacent
