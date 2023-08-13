package khelp.uno.model

class CardStack : Iterable<Card>
{
    companion object
    {
        fun fullStack() : CardStack
        {
            val cardStack = CardStack()

            for (value in CardValue.values())
            {
                if (value.inFullStack)
                {
                    for (color in CardColor.values())
                    {
                        cardStack.add(CardPlay(color, value))
                        cardStack.add(CardPlay(color, value))
                    }
                }
            }

            cardStack.add(CardChangeColor())
            cardStack.add(CardMore4())

            cardStack.add(CardChangeColor())
            cardStack.add(CardMore4())

            cardStack.shuffle()
            return cardStack
        }
    }

    private val cards = ArrayList<Card>()

    val empty : Boolean get() = this.cards.isEmpty()
    val notEmpty : Boolean get() = this.cards.isNotEmpty()
    val size : Int get() = this.cards.size

    operator fun get(index : Int) : Card = this.cards[index]

    fun shuffle()
    {
        this.cards.shuffle()
    }

    fun clear()
    {
        this.cards.clear()
    }

    fun add(card : Card)
    {
        this.cards.add(card)
    }

    fun removeAt(index : Int) : Card = this.cards.removeAt(index)

    fun remove(cardToRemove : Card) : Boolean =
        this.cards.removeIf { card -> card.id == cardToRemove.id }

    override fun iterator() : Iterator<Card> = this.cards.iterator()
}
