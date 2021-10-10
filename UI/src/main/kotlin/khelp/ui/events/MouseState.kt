package khelp.ui.events

data class MouseState(val mouseStatus : MouseStatus,
                      val x : Int, val y : Int,
                      val leftButtonDown : Boolean, val middleButtonDown : Boolean, val rightButtonDown : Boolean,
                      val shiftDown:Boolean,val controlDown:Boolean,val altDown:Boolean,
                      val clicked : Boolean)
