package khelp.engine3d.shader

import org.lwjgl.opengl.GL32

class GeometryShader(code:String) : Shader(code, GL32.GL_GEOMETRY_SHADER,32)