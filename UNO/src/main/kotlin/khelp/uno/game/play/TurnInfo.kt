package khelp.uno.game.play

import khelp.engine3d.render.Node


class TurnInfo(val player : Player, val node : Node, val update : () -> Unit)
