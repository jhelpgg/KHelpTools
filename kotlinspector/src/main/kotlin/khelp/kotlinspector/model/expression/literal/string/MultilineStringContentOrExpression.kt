package khelp.kotlinspector.model.expression.literal.string

class MultilineStringContentOrExpression
{
    val multiLineStringContent: MultiLineStringContent?
    val multiLineStringExpression: MultiLineStringExpression?

    constructor(multiLineStringContent: MultiLineStringContent)
    {
        this.multiLineStringContent = multiLineStringContent
        this.multiLineStringExpression = null
    }

    constructor(multiLineStringExpression: MultiLineStringExpression)
    {
        this.multiLineStringContent = null
        this.multiLineStringExpression = multiLineStringExpression
    }
}
