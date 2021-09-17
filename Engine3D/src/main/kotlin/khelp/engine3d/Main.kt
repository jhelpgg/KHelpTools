package khelp.engine3d

import khelp.engine3d.render.Scene
import khelp.engine3d.render.YELLOW
import khelp.engine3d.render.window3D
import khelp.thread.delay

fun main()
{
    window3D(800, 600, "Test") {
        scene.backgroundColor = YELLOW
        scene.root {
            object3D("object") {

            }
        }
    }
}
