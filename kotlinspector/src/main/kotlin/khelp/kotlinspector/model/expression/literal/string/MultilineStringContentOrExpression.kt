package khelp.kotlinspector.model.expression.literal.string

class MultilineStringContentOrExpression
{
    val multiLineStringContent: MultiLineStringContent?
    val multiLineStringExpression: MultiLineStringExpression?
    val isQuote : Boolean

    constructor(multiLineStringContent: MultiLineStringContent)
    {
        this.multiLineStringContent = multiLineStringContent
        this.multiLineStringExpression = null
        this.isQuote = false
    }

    constructor(multiLineStringExpression: MultiLineStringExpression)
    {
        this.multiLineStringContent = null
        this.multiLineStringExpression = multiLineStringExpression
        this.isQuote = false
    }

    constructor()
    {
        this.multiLineStringContent = null
        this.multiLineStringExpression = null
        this.isQuote = true
    }
}
