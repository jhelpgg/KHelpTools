package khelp.engine3d.shader

import khelp.engine3d.render.versionOpenGL

class OpenGLVersionToOldException(requiredVersion : Int) : Exception("The function need at least openGL version $requiredVersion, but yours system have $versionOpenGL version")