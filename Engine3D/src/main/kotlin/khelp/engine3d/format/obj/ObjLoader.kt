package khelp.engine3d.format.obj

import khelp.engine3d.format.obj.options.ObjAsIs
import khelp.engine3d.format.obj.options.ObjColorTexture
import khelp.engine3d.format.obj.options.ObjOption
import khelp.engine3d.format.obj.options.ObjUseNormalMap
import khelp.engine3d.geometry.Mesh
import khelp.engine3d.geometry.Point2D
import khelp.engine3d.geometry.Point3D
import khelp.engine3d.geometry.Vertex
import khelp.engine3d.render.Node
import khelp.engine3d.render.Object3D
import khelp.resources.Resources
import khelp.utilities.log.exception
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Pattern
import kotlin.math.sqrt

private var SPACES = Pattern.compile("\\s+")

fun objLoader(name : String, path : String, resources : Resources, objOption : ObjOption = ObjAsIs) : Node =
    objLoader(name, resources.inputStream(path), objOption)

fun objLoader(name : String, inputStream : InputStream, objOption : ObjOption = ObjAsIs) : Node
{
    val objRoot = Node(name)
    val description = createObjDescription(name, inputStream, objOption)
    val generateNormal = objOption == ObjColorTexture

    for (objObject in description.objects)
    {
        val object3D = Object3D(objObject.name, createMesh(objObject, generateNormal))
        objRoot.addChild(object3D)
    }

    return objRoot
}

private fun createObjDescription(name : String, inputStream : InputStream, objOption : ObjOption) : ObjDescription
{
    val objDescription = ObjDescription()
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))

    try
    {
        var line = bufferedReader.readLine()
            ?.trim()
        var objObject = ObjObject("")

        val optionNormalMap = objOption is ObjUseNormalMap

        while (line != null)
        {
            when
            {
                line.startsWith("o ")  ->
                {
                    if (objObject.name.isNotEmpty())
                    {
                        if (objObject.uvs.isEmpty())
                        {
                            objObject.uvs.add(Point2D())
                        }

                        if (objObject.normals.isEmpty())
                        {
                            objObject.normals.add(Point3D())
                        }

                        objDescription.objects.add(objObject)
                    }

                    objObject = ObjObject(line.substring(2)
                                              .trim())
                }

                line.startsWith("usemtl ") ->
                {
                    if (objObject.name.isNotEmpty())
                    {
                        if (objObject.uvs.isEmpty())
                        {
                            objObject.uvs.add(Point2D())
                        }

                        if (objObject.normals.isEmpty())
                        {
                            objObject.normals.add(Point3D())
                        }

                        objDescription.objects.add(objObject)
                    }

                    objObject = objObject.subObject(line.substring(7)
                                                        .trim())
                }

                line.startsWith("v ")  ->
                {
                    val coordinates = line.substring(2)
                        .trim()
                        .split(SPACES)
                        .map { text -> text.toFloat() }
                    objObject.points.add(Point3D(coordinates[0], coordinates[1], coordinates[2]))
                }

                line.startsWith("vt ") ->
                {
                    val coordinates = line.substring(3)
                        .trim()
                        .split(SPACES)
                        .map { text -> text.toFloat() }
                    objObject.uvs.add(Point2D(coordinates[0], 1f - coordinates[1]))

                    if (optionNormalMap)
                    {
                        objObject.normals.add(
                            (objOption as ObjUseNormalMap).normalMap[coordinates[0], 1f - coordinates[1]])
                    }
                }

                line.startsWith("vn ") ->
                {
                    if (! optionNormalMap)
                    {
                        val coordinates = line.substring(3)
                            .trim()
                            .split(SPACES)
                            .map { text -> text.toFloat() }
                        objObject.normals.add(Point3D(coordinates[0], coordinates[1], coordinates[2]))
                    }
                }

                line.startsWith("f ")  ->
                {
                    val objFace = ObjFace()

                    for (vertex in line.substring(2)
                        .trim()
                        .split(SPACES))
                    {
                        val indexes = vertex.split('/')

                        val point = indexes[0].toInt() - 1
                        var uv = 0
                        var normal = 0

                        if (indexes.size > 1)
                        {
                            if (indexes[1].isNotEmpty())
                            {
                                uv = indexes[1].toInt() - 1
                            }

                            if (optionNormalMap)
                            {
                                normal = uv
                            }
                            else if (indexes.size > 2)
                            {
                                normal = indexes[2].toInt() - 1
                            }
                        }

                        objFace.vertexs.add(ObjVertex(point, uv, normal))
                    }

                    objObject.faces.add(objFace)
                }
            }

            line = bufferedReader.readLine()
                ?.trim()
        }

        if (objObject.name.isNotEmpty())
        {
            if (objObject.uvs.isEmpty())
            {
                objObject.uvs.add(Point2D())
            }

            if (objObject.normals.isEmpty())
            {
                objObject.normals.add(Point3D())
            }

            objDescription.objects.add(objObject)
        }
    }
    catch (exception : Exception)
    {
        exception(exception, "Failed to load OBJ : $name")
    }
    finally
    {
        bufferedReader.close()
    }

    return objDescription
}

private fun createMesh(objObject : ObjObject, generateNormal : Boolean) : Mesh
{
    val mesh = Mesh()

    for (face in objObject.faces)
    {
        var normal = Point3D()
        val size = face.vertexs.size

        if (generateNormal && size > 2)
        {
            val point0 = objObject.points[face.vertexs[0].point]
            val point1 = objObject.points[face.vertexs[size - 1].point]
            val point2 = objObject.points[face.vertexs[size - 2].point]
            val vx10 = point0.x - point1.x
            val vy10 = point0.y - point1.y
            val vz10 = point0.z - point1.z
            val vx12 = point2.x - point1.x
            val vy12 = point2.y - point1.y
            val vz12 = point2.z - point1.z
            val nx = vy10 * vz12 - vz10 * vy12
            val ny = vx10 * vz12 - vz10 * vx12
            val nz = vx10 * vy12 - vy10 * vx12
            val norme = sqrt(nx * nx + ny * ny + nz * nz)

            normal =
                if (norme > 0.01f)
                {
                    Point3D(nx / norme, ny / norme, - nz / norme)
                }
                else
                {
                    Point3D(nx, ny, nz)
                }
        }

        mesh.face {
            for (indexes in face.vertexs.reversed())
            {
                if (generateNormal)
                {
                    this.add(Vertex(objObject.points[indexes.point],
                                    objObject.uvs[indexes.uv],
                                    normal))
                }
                else
                {
                    this.add(Vertex(objObject.points[indexes.point],
                                    objObject.uvs[indexes.uv],
                                    objObject.normals[indexes.normal]))
                }
            }
        }
    }

    return mesh
}
