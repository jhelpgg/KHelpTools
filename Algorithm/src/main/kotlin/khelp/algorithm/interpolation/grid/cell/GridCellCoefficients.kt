package khelp.algorithm.interpolation.grid.cell

data class GridCellCoefficients(val pointIndex1 : Int, val coefficient1 : Float,
                                val pointIndex2 : Int, val coefficient2 : Float,
                                val pointIndex3 : Int, val coefficient3 : Float) : GridCell
