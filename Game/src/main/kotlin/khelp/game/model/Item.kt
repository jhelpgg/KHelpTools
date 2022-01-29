package khelp.game.model

import khelp.game.resources.GameResources
import khelp.ui.game.GameImage

class Item(val itemType : ItemType, val name:String, val description:String, val iconPath:String,val sellable:Boolean, val price:Int)
{
    val image : GameImage by lazy { GameImage.load(this.iconPath, GameResources.resources) }
}