package khelp.engine3d.sound3d

object DummySound : Sound()
{
    override fun duration() : Long = 0L

    override fun transferToBuffer(buffer : Int) : Boolean = true
}