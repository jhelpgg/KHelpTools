package khelp.game.model

infix fun AttackElement.ATTACK(other : AttackElement) : BonusMalus =
    if (this == other || this == AttackElement.NO_ELEMENT || other == AttackElement.NO_ELEMENT)
    {
        BonusMalus.NEUTRAL
    }
    else
    {
        when (this)
        {
            AttackElement.WATER ->
                if (other == AttackElement.FIRE)
                {
                    BonusMalus.BONUS
                }
                else
                {
                    BonusMalus.MALUS
                }
            AttackElement.WOOD  ->
                if (other == AttackElement.WATER)
                {
                    BonusMalus.BONUS
                }
                else
                {
                    BonusMalus.MALUS
                }
            AttackElement.FIRE  ->
                if (other == AttackElement.WOOD)
                {
                    BonusMalus.BONUS
                }
                else
                {
                    BonusMalus.MALUS
                }
            else                -> BonusMalus.NEUTRAL
        }
    }
