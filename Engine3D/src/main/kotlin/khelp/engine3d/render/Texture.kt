package khelp.engine3d.render

import khelp.engine3d.extensions.rgba
import khelp.engine3d.utils.ThreadOpenGL
import khelp.engine3d.utils.transferByte
import khelp.thread.TaskContext
import khelp.ui.game.GameImage
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack
import java.util.concurrent.atomic.AtomicBoolean

class Texture(val gameImage : GameImage)
{
    private val needRefresh = AtomicBoolean(true)
    private var videoMemoryId : Int = - 1
    val width : Int = this.gameImage.width
    val height : Int = this.gameImage.height

    init
    {
        this.gameImage.refreshFlow.then(TaskContext.INDEPENDENT) { this.needRefresh.set(true) }
    }

    @ThreadOpenGL
    internal fun bind()
    {
        // If no video memory ID, create it
        if (this.videoMemoryId < 0)
        {
            val stack = MemoryStack.stackPush()
            val textureID = stack.mallocInt(1)
            GL11.glGenTextures(textureID)
            this.videoMemoryId = textureID.get()
            MemoryStack.stackPop()
        }

        // If the texture need to be refresh
        if (this.needRefresh.compareAndSet(true, false))
        {
            // Push pixels in video memory
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.videoMemoryId)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT)
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA,
                              this.width, this.height,
                              0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
                              transferByte(this.gameImage.grabPixels().rgba))
        }

        // Draw the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.videoMemoryId)
    }
}
