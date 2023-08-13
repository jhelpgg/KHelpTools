package khelp.uno.model

class CardPlay(val color : CardColor, val value : CardValue) : Card(color.directoryPath + value.fileName, value.value) {
    override fun name() : String = "${this.value} ${this.color}"
}
