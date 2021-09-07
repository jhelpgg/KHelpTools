package khelp.ui.dsl

import javax.swing.JButton

class JButtonCreator(text : String)
{
    internal val button = JButton(text)

    fun onClick(action : () -> Unit)
    {
        this.button.addActionListener { action() }
    }
}
