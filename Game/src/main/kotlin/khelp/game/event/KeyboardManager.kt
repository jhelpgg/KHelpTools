package khelp.game.event

import khelp.game.resources.GameResources
import khelp.thread.future.FutureResult
import khelp.thread.future.Promise
import khelp.utilities.extensions.grabElementIf
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.TreeSet

internal object KeyboardManager : KeyListener
{
    private val keyAssociation = HashMap<Int, KeyAction>()
    private val actions = TreeSet<KeyAction>()
    private var promiseNextKeyCode : Promise<Int>? = null
    private val lock = Object()

    init
    {
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

    fun association(keyCode : Int) : KeyAction? = this.keyAssociation[keyCode]

    fun association(keyAction : KeyAction) : Int =
        this.keyAssociation.grabElementIf { _, action -> action == keyAction }?.first ?: - 1

    fun captureNextKeyCode() : FutureResult<Int> =
        synchronized(this.lock)
        {
            if (this.promiseNextKeyCode == null)
            {
                this.promiseNextKeyCode = Promise()
            }

            this.promiseNextKeyCode !!.futureResult
        }

    override fun keyTyped(keyEvent : KeyEvent)
    {
        synchronized(this.lock)
        {
            this.promiseNextKeyCode?.result(keyEvent.keyCode)
            this.promiseNextKeyCode = null
        }
    }

    override fun keyPressed(keyEvent : KeyEvent)
    {
        val action = this.keyAssociation[keyEvent.keyCode] ?: return

        synchronized(this.actions)
        {
            this.actions.add(action)
        }
    }

    override fun keyReleased(keyEvent : KeyEvent)
    {
        val action = this.keyAssociation[keyEvent.keyCode] ?: return

        synchronized(this.actions)
        {
            this.actions.remove(action)
        }
    }
}
