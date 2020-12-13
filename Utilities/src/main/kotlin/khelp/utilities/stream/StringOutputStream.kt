package khelp.utilities.stream

import java.io.ByteArrayOutputStream
import khelp.utilities.extensions.utf8

class StringOutputStream : ByteArrayOutputStream()
{
    val string get() = this.toByteArray().utf8
}