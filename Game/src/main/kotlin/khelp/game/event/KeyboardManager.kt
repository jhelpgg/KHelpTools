/**
 * @file KeyboardManager.kt
 * Manages keyboard input and key-to-action mapping for the game.
 * Provides configurable controls with preference persistence.
 */
package khelp.game.event

import khelp.game.resources.GameResources
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.utilities.extensions.grabElementIf
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.TreeSet

/**
 * Singleton object that manages all keyboard input for the game.
 *
 * This manager:
 * - Maps physical keyboard keys to logical game actions
 * - Tracks currently pressed actions
 * - Allows runtime key remapping
 * - Persists key configurations to preferences
 * - Supports key capture for configuration screens
 *
 * Default key mappings:
 * - Arrow keys: Movement
 * - Space: Action
 * - Escape: Cancel
 * - Tab: Menu
 * - M: Map
 * - Page Up/Down: Previous/Next
 *
 * @see KeyAction for available actions
 */
internal object KeyboardManager : KeyListener
{
    /** Maps key codes to their associated actions */
    private val keyAssociation = HashMap<Int, KeyAction>()

    /** Set of currently active (pressed) actions */
    private val actions = TreeSet<KeyAction>()

    /** Promise used for capturing the next key press */
    private var promiseNextKeyCode : Promise<Int>? = null

    /** Synchronization lock for thread safety */
    private val lock = Object()

    init
    {
        // Load key mappings from preferences or use defaults
        val preferences = GameResources.preferences
        this.keyAssociation[preferences[KeyAction.UP.name, KeyEvent.VK_UP]] = KeyAction.UP
        this.keyAssociation[preferences[KeyAction.DOWN.name, KeyEvent.VK_DOWN]] = KeyAction.DOWN
        this.keyAssociation[preferences[KeyAction.LEFT.name, KeyEvent.VK_LEFT]] = KeyAction.LEFT
        this.keyAssociation[preferences[KeyAction.RIGHT.name, KeyEvent.VK_RIGHT]] = KeyAction.RIGHT
        this.keyAssociation[preferences[KeyAction.ACTION.name, KeyEvent.VK_SPACE]] = KeyAction.ACTION
        this.keyAssociation[preferences[KeyAction.CANCEL.name, KeyEvent.VK_ESCAPE]] = KeyAction.CANCEL
        this.keyAssociation[preferences[KeyAction.MENU.name, KeyEvent.VK_TAB]] = KeyAction.MENU
        this.keyAssociation[preferences[KeyAction.MAP.name, KeyEvent.VK_M]] = KeyAction.MAP
        this.keyAssociation[preferences[KeyAction.NEXT.name, KeyEvent.VK_PAGE_DOWN]] = KeyAction.NEXT
        this.keyAssociation[preferences[KeyAction.PREVIOUS.name, KeyEvent.VK_PAGE_UP]] = KeyAction.PREVIOUS
    }

    /**
     * Gets the currently active actions.
     *
     * @param consume If true, clears the action set after returning (useful for one-time actions)
     * @return Array of currently pressed actions
     */
    fun currentActions(consume : Boolean = false) : Array<KeyAction> =
        synchronized(this.actions)
        {
            val array = this.actions.toTypedArray()

            if (consume)
            {
                this.actions.clear()
            }

            array
        }

    /**
     * Associates a key code with a game action.
     *
     * This method handles key remapping, ensuring no conflicts:
     * - If the key is already mapped to the same action, returns the key code
     * - If the key is mapped to a different action, returns -1 (conflict)
     * - If successful, removes any previous key mapping for the action
     *
     * @param keyCode The keyboard key code to map
     * @param keyAction The action to map to the key
     * @return The previous key code for this action, or -1 if mapping failed
     */
    fun associate(keyCode : Int, keyAction : KeyAction) : Int
    {
        if (this.keyAssociation[keyCode] == keyAction)
        {
            return keyCode
        }

        if (this.keyAssociation[keyCode] != null)
        {
            return - 1
        }

        val oldCode = this.keyAssociation.grabElementIf { _, action -> action == keyAction }?.first ?: return - 1
        this.keyAssociation.remove(oldCode)
        this.keyAssociation[keyCode] = keyAction
        GameResources.preferences.edit { keyAction.name IS keyCode }
        return oldCode
    }

    /**
     * Gets the action associated with a key code.
     *
     * @param keyCode The keyboard key code
     * @return The associated action, or null if not mapped
     */
    fun association(keyCode : Int) : KeyAction? = this.keyAssociation[keyCode]

    /**
     * Gets the key code associated with an action.
     *
     * @param keyAction The game action
     * @return The associated key code, or -1 if not mapped
     */
    fun association(keyAction : KeyAction) : Int =
        this.keyAssociation.grabElementIf { _, action -> action == keyAction }?.first ?: - 1

    /**
     * Starts capturing the next key press.
     *
     * Useful for key configuration screens where the user needs to press
     * a key to assign it to an action.
     *
     * @return A FutureResult that will contain the key code of the next pressed key
     */
    fun captureNextKeyCode() : FutureResult<Int> =
        synchronized(this.lock)
        {
            if (this.promiseNextKeyCode == null)
            {
                this.promiseNextKeyCode = Promise()
            }

            this.promiseNextKeyCode !!.futureResult
        }

    /**
     * Handles key typed events.
     * Used for key capture functionality.
     */
    override fun keyTyped(keyEvent : KeyEvent)
    {
        synchronized(this.lock)
        {
            this.promiseNextKeyCode?.result(keyEvent.keyCode)
            this.promiseNextKeyCode = null
        }
    }

    /**
     * Handles key pressed events.
     * Adds the associated action to the active actions set.
     */
    override fun keyPressed(keyEvent : KeyEvent)
    {
        val action = this.keyAssociation[keyEvent.keyCode] ?: return

        synchronized(this.actions)
        {
            this.actions.add(action)
        }
    }

    /**
     * Handles key released events.
     * Removes the associated action from the active actions set.
     */
    override fun keyReleased(keyEvent : KeyEvent)
    {
        val action = this.keyAssociation[keyEvent.keyCode] ?: return

        synchronized(this.actions)
        {
            this.actions.remove(action)
        }
    }
}