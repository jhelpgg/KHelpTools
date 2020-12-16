package khelp.utilities.stream

import java.io.ByteArrayInputStream
import khelp.utilities.extensions.utf8

/**
 * Read a String as S*stream in UTF-8 format
 */
class StringInputStream(string: String) : ByteArrayInputStream(string.utf8)
