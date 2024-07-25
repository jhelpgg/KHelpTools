package khelp.engine3d.utils.delaunay.adjacent

import khelp.engine3d.utils.delaunay.TriangleIndexed

fun adjacent(triangleFirst : TriangleIndexed, triangleSecond : TriangleIndexed) : Adjacent =
    when
    {
        triangleFirst.point1 == triangleSecond.point1 ->
            when
            {
                triangleFirst.point2 == triangleSecond.point2 ->
                    AdjacentSide(triangleFirst.point3,
                                 triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleSecond.point3)

                triangleFirst.point2 == triangleSecond.point3 ->
                    AdjacentSide(triangleFirst.point3,
                                 triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleSecond.point2)

                triangleFirst.point3 == triangleSecond.point2 ->
                    AdjacentSide(triangleFirst.point2,
                                 triangleFirst.point1,
                                 triangleFirst.point3,
                                 triangleSecond.point3)

                triangleFirst.point3 == triangleSecond.point3 ->
                    AdjacentSide(triangleFirst.point2,
                                 triangleFirst.point1,
                                 triangleFirst.point3,
                                 triangleSecond.point2)

                else                                          -> AdjacentEmpty
            }

        triangleFirst.point1 == triangleSecond.point2 ->
            when
            {
                triangleFirst.point2 == triangleSecond.point1 ->
                    AdjacentSide(triangleFirst.point3,
                                 triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleSecond.point3)

                triangleFirst.point2 == triangleSecond.point3 ->
                    AdjacentSide(triangleFirst.point3,
                                 triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleSecond.point1)

                triangleFirst.point3 == triangleSecond.point1 ->
                    AdjacentSide(triangleFirst.point2,
                                 triangleFirst.point1,
                                 triangleFirst.point3,
                                 triangleSecond.point3)

                triangleFirst.point3 == triangleSecond.point3 ->
                    AdjacentSide(triangleFirst.point2,
                                 triangleFirst.point1,
                                 triangleFirst.point3,
                                 triangleSecond.point1)

                else                                          -> AdjacentEmpty
            }

        triangleFirst.point1 == triangleSecond.point3 ->
            when
            {
                triangleFirst.point2 == triangleSecond.point1 ->
                    AdjacentSide(triangleFirst.point3,
                                 triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleSecond.point2)

                triangleFirst.point2 == triangleSecond.point2 ->
                    AdjacentSide(triangleFirst.point3,
                                 triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleSecond.point1)

                triangleFirst.point3 == triangleSecond.point1 ->
                    AdjacentSide(triangleFirst.point2,
                                 triangleFirst.point1,
                                 triangleFirst.point3,
                                 triangleSecond.point2)

                triangleFirst.point3 == triangleSecond.point2 ->
                    AdjacentSide(triangleFirst.point2,
                                 triangleFirst.point1,
                                 triangleFirst.point3,
                                 triangleSecond.point1)

                else                                          -> AdjacentEmpty
            }

        //

        triangleFirst.point2 == triangleSecond.point1 ->
            when
            {
                triangleFirst.point3 == triangleSecond.point2 ->
                    AdjacentSide(triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleFirst.point3,
                                 triangleSecond.point3)

                triangleFirst.point3 == triangleSecond.point3 ->
                    AdjacentSide(triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleFirst.point3,
                                 triangleSecond.point2)

                else                                          -> AdjacentEmpty
            }

        triangleFirst.point2 == triangleSecond.point2 ->
            when
            {
                triangleFirst.point3 == triangleSecond.point1 ->
                    AdjacentSide(triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleFirst.point3,
                                 triangleSecond.point3)

                triangleFirst.point3 == triangleSecond.point3 ->
                    AdjacentSide(triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleFirst.point3,
                                 triangleSecond.point1)

                else                                          -> AdjacentEmpty
            }

        triangleFirst.point2 == triangleSecond.point3 ->
            when
            {
                triangleFirst.point3 == triangleSecond.point1 ->
                    AdjacentSide(triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleFirst.point3,
                                 triangleSecond.point2)

                triangleFirst.point3 == triangleSecond.point2 ->
                    AdjacentSide(triangleFirst.point1,
                                 triangleFirst.point2,
                                 triangleFirst.point3,
                                 triangleSecond.point1)

                else                                          -> AdjacentEmpty
            }

        else                                          -> AdjacentEmpty
    }