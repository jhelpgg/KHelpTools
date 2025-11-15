/**
 * @file Rules.kt
 * Defines the game's combat rules and element interactions.
 */
package khelp.game.model

/**
 * Calculates the damage modifier when one element attacks another.
 *
 * Element triangle: WATER beats FIRE, FIRE beats WOOD, WOOD beats WATER
 * NO_ELEMENT is neutral against everything.
 *
 * Usage:
 * ```kotlin
 * val modifier = AttackElement.WATER ATTACK AttackElement.FIRE
 * // modifier is BonusMalus.BONUS (1.5x damage)
 * ```
 *
 * @receiver The attacking element
 * @param other The defending element
 * @return BonusMalus indicating damage modifier
 */
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