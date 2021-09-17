package khelp.engine3d.extensions

val ByteArray.rgba : IntArray
    get()
    {
        val size = this.size / 4
        val pixels = IntArray(size)
        var index = 0

        for (pixel in 0 until size)
        {
            pixels[pixel] =
                ((this[index + 3].toInt() and 0xFF) shl 24) or
                        ((this[index].toInt() and 0xFF) shl 16) or
                        ((this[index + 1].toInt() and 0xFF) shl 8) or
                        (this[index + 2].toInt() and 0xFF)

            index += 4
        }

        return pixels
    }