package khelp.utilities.stream

import java.io.ByteArrayOutputStream
import khelp.utilities.extensions.utf8

/**
 * Stream that write in a String.
 *
 * Received data supposed to represents a String in UTF-8 format
 */
class StringOutputStream : ByteArrayOutputStream()
{
    /**Actual read string*/
    val string get() = this.toByteArray().utf8
}