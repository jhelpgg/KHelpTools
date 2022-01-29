package khelp.game.screen

import khelp.game.resources.CharacterImage
import khelp.game.resources.CharacterPosition

class Character(private val characterImage : CharacterImage)
{

    var position : CharacterPosition = CharacterPosition.FACE
    set(value)
    {
        field = value
    }
}