package khelp.game.model

sealed class Hurt

object MissHurt : Hurt()

class HitHurt(hit : Int) : Hurt()
