package  khelp.engine3d.extensions

val IntArray.rgba : ByteArray
    get()
    {
        val byteArray = ByteArray(this.size * 4)
        var index = 0

        for (pixel in this)
        {
            byteArray[index ++] = ((pixel shr 16) and 0xFF).toByte()
            byteArray[index ++] = ((pixel shr 8) and 0xFF).toByte()
            byteArray[index ++] = (pixel and 0xFF).toByte()
            byteArray[index ++] = ((pixel shr 24) and 0xFF).toByte()
        }

        return byteArray
    }