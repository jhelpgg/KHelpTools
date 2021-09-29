package khelp.engine3d.render.complex

import khelp.engine3d.render.LIGHT_GRAY
import khelp.engine3d.render.Material
import khelp.engine3d.render.Node
import khelp.engine3d.render.ORANGE
import khelp.engine3d.render.Object3D
import khelp.engine3d.render.ObjectClone
import khelp.engine3d.render.prebuilt.Revolution
import khelp.engine3d.render.prebuilt.Sphere
import khelp.utilities.extensions.bounds
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.min

/**
 * A sword
 * @param size Sword size in `[2, 5]`
 */
class Sword(id : String, size : Float = 3.3f) : Node(id)
{
    companion object
    {
        /**Next sword ID*/
        internal val NEXT_ID = AtomicInteger(0)
    }

    /**Material on base*/
    val baseMaterial = Material()

    /**Material on blade*/
    val bladeMaterial = Material()

    init
    {
        val sizeLimited = size.bounds(2f, 5f)
        this.baseMaterial.colorDiffuse = ORANGE
        this.bladeMaterial.colorDiffuse = LIGHT_GRAY

        val base = Revolution("${id}.base")
        base.path {
            move(0.1f, 0.4f)
            line(0.1f, - 0.4f)
        }
        base.material = this.baseMaterial
        this.addChild(base)

        val sphere = Sphere("${id}.sphere", 5, 5)
        sphere.scaleX = 0.12f
        sphere.scaleY = 0.12f
        sphere.scaleZ = 0.12f
        sphere.x = 0f
        sphere.y = - 0.4f
        sphere.z = 0f
        sphere.material = this.baseMaterial
        this.addChild(sphere)

        val guard = Revolution("${id}.guard")
        guard.path {
            move(0.1f, 0.4f)
            quadratic(0.2f, 0f, 0.1f, - 0.4f)
        }

        guard.angleX = 90f
        guard.x = 0f
        guard.y = 0.4f
        guard.z = 0f
        guard.material = this.baseMaterial
        this.addChild(guard)

        var clone = ObjectClone("${id}.clone1", sphere)
        clone.scaleX = 0.12f
        clone.scaleY = 0.12f
        clone.scaleZ = 0.12f
        clone.x = 0f
        clone.y = 0.4f
        clone.z = - 0.4f
        clone.material = this.baseMaterial
        this.addChild(clone)

        clone = ObjectClone("${id}.clone2", sphere)
        clone.scaleX = 0.12f
        clone.scaleY = 0.12f
        clone.scaleZ = 0.12f
        clone.x = 0f
        clone.x = 0.4f
        clone.x = 0.4f
        clone.material = this.baseMaterial
        this.addChild(clone)

        val z = min(sizeLimited / 10f, 0.39f)
        val blade = Object3D("${id}.blade")
        blade.mesh {
            face {
                add(0.1f, 0.4f, 0f,
                    0f, 0f,
                    - 1f, 0.5f, 1f)
                add(0f, 0.4f, z,
                    0f, 0f,
                    - 1f, 1f, 1f)
                add(0f, sizeLimited, 0f,
                    0f, 0f,
                    - 1f, 0.5f, 0f)
            }

            face {
                add(0f, sizeLimited, 0f,
                    0f, 0f,
                    - 1f, 0.5f, 0f)
                add(0f, 0.4f, z,
                    0f, 0f, - 1f,
                    1f, 1f)
                add(- 0.1f, 0.4f, 0f,
                    0f, 0f,
                    - 1f, 0.5f, 1f)
            }

            face {
                add(0f, sizeLimited, 0f,
                    0f, 0f,
                    1f, 0.5f, 0f)
                add(0f, 0.4f, - z,
                    0f, 0f,
                    1f, 0f, 1f)
                add(0.1f, 0.4f, 0f,
                    0f, 0f,
                    1f, 0.5f, 1f)
            }

            face {
                add(- 0.1f, 0.4f, 0f,
                    0f, 0f,
                    1f, 0.5f, 1f)
                add(0f, 0.4f, - z,
                    0f, 0f,
                    1f, 0f, 1f)
                add(0f, sizeLimited, 0f,
                    0f, 0f,
                    1f, 0.5f, 0f)
            }
        }

        blade.material = this.bladeMaterial
        this.addChild(blade)
    }


    /**
     * Position sword to be attach in a [Robot] back
     *
     * @see Robot.putOnBack
     */
    fun positionForBack()
    {
        this.angleX = 180f
        this.angleY = 90f
        this.angleZ = 0f
        this.x = 0f
        this.y = - 0.4f
        this.z - 0.05f
    }

    /**
     * Position sword to be take by a [Robot] hand
     *
     * @see Robot.putOnLeftHand
     * @see Robot.putOnRightHand
     */
    fun positionForHand()
    {
        this.angleX = - 90f
        this.angleY = 90f
        this.angleZ = 0f
        this.x = 0f
        this.y = 0f
        this.z = 0f
    }
}