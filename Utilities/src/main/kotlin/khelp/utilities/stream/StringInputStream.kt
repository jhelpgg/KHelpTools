package khelp.utilities.stream

import java.io.ByteArrayInputStream
import khelp.utilities.extensions.utf8

class StringInputStream(string: String) : ByteArrayInputStream(string.utf8)
