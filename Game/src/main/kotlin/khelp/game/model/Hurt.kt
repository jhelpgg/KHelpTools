package khelp.game.model

sealed class Hurt

object MissHurt : Hurt()

class HitHurt(val hit : Int) : Hurt()
