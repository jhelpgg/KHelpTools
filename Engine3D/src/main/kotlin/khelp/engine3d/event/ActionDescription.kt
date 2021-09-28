package khelp.engine3d.event

internal class ActionDescription(val actionCode : ActionCode,
                                 var joystickCode : JoystickCode = actionCode.defaultJoystickCode,
                                 var keyCode : Int = actionCode.defaultKeyCode,
                                 var consumable : Boolean = false)
