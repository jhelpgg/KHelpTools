package khelp.kotlinspector.model.expression.literal.string

class LineStringContentOrExpression
{
    val lineStringContent: LineStringContent?
    val lineStringExpression: LineStringExpression?

    constructor(lineStringContent: LineStringContent)
    {
        this.lineStringContent = lineStringContent
        this.lineStringExpression = null
    }

    constructor(lineStringExpression: LineStringExpression)
    {
        this.lineStringContent = null
        this.lineStringExpression = lineStringExpression
    }
}
