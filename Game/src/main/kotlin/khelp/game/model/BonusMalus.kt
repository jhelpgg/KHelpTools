package khelp.game.model

enum class BonusMalus(val factor:Double)
{
    NEUTRAL(1.0),
    BONUS(1.5),
    MALUS(0.5)
}