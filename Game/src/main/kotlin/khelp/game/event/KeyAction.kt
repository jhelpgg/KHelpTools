/**
 * @file KeyAction.kt
 * Defines the available game actions that can be triggered by keyboard input.
 * These actions represent logical game commands rather than physical keys.
 */
package khelp.game.event

/**
 * Enumeration of all possible game actions that can be triggered by the player.
 *
 * These actions are mapped to physical keyboard keys through [KeyboardManager].
 * The separation of actions from keys allows for customizable controls.
 *
 * @see KeyboardManager for key mapping configuration
 */
enum class KeyAction
{
    /** UP Movement upward or menu navigation up */
    UP,

    /** Movement downward or menu navigation down */
    DOWN,

    /** Movement left or menu navigation left */
    LEFT,

    /** Movement right or menu navigation right */
    RIGHT,

    /** Navigate to next page/item in lists or dialogs */
    NEXT,

    /** Navigate to previous page/item in lists or dialogs */
    PREVIOUS,

    /** Primary action button (interact, confirm, select) */
    ACTION,

    /** Cancel current action or close menu/dialog */
    CANCEL,

    /** Open/close the game menu */
    MENU,

    /** Open/close the game map */
    MAP
}