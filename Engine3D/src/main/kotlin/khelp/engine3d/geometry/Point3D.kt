package khelp.engine3d.geometry

data class Point3D(val x : Float = 0f, val y : Float = 0f, val z : Float = 0f)
{
    constructor(vect3f : Vec3f) : this(vect3f.x, vect3f.y, vect3f.z)

    fun toVect3f() = Vec3f(this.x, this.y, this.z)

    operator fun plus(point3D : Point3D) : Point3D =
        Point3D(this.x + point3D.x, this.y + point3D.y, this.z + point3D.z)

    operator fun plus(vec3f : Vec3f) : Point3D =
        Point3D(this.x + vec3f.x, this.y + vec3f.y, this.z + vec3f.z)

    fun add(x : Float, y : Float, z : Float) : Point3D =
        Point3D(this.x + x, this.y + y, this.z + z)
}
