package khelp.engine3d.render

import khelp.engine3d.utils.ThreadOpenGL
import org.lwjgl.opengl.GL11

class Object3D(id:String) : NodeWithMaterial(id)
{
    @ThreadOpenGL
    override fun renderSpecific()
    {
        GL11.glDisable(GL11.GL_CULL_FACE)
        RED.glColor4f()
        GL11.glBegin(GL11.GL_POLYGON)
        GL11.glVertex3f(-1f,-1f,-1f)
        GL11.glVertex3f(1f,0f,-1f)
        GL11.glVertex3f(-1f,1f,-1f)
        GL11.glEnd()
    }
}
