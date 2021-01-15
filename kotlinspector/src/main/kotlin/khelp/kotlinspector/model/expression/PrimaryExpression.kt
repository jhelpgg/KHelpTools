package khelp.kotlinspector.model.expression

import khelp.grammar.GrammarNode
import khelp.grammar.prebuilt.KotlinGrammar
import khelp.kotlinspector.model.expression.`when`.WhenExpression
import khelp.kotlinspector.model.expression.literal.FunctionLiteral
import khelp.kotlinspector.model.expression.literal.LiteralConstant
import khelp.kotlinspector.model.expression.literal.ObjectLiteral
import khelp.kotlinspector.model.expression.literal.string.StringLiteral
import khelp.kotlinspector.model.expression.trycacth.TryExpression

class PrimaryExpression
{
    var parenthesizedExpression: ParenthesizedExpression? = null
        private set
    var identifier = ""
        private set
    var literalConstant: LiteralConstant? = null
        private set
    var stringLiteral: StringLiteral? = null
        private set
    var callableReference: CallableReference? = null
        private set
    var functionLiteral: FunctionLiteral? = null
        private set
    var objectLiteral: ObjectLiteral? = null
        private set
    var collectionLiteral: CollectionLiteral? = null
        private set
    var thisExpression: ThisExpression? = null
        private set
    var superExpression: SuperExpression? = null
        private set
    var ifExpression: IfExpression? = null
        private set
    var whenExpression: WhenExpression? = null
        private set
    var tryExpression: TryExpression? = null
        private set
    var jumpExpression: JumpExpression? = null
        private set

    internal fun parse(grammarNode: GrammarNode)
    {
        this.parenthesizedExpression = null
        this.identifier = ""
        this.literalConstant = null
        this.stringLiteral = null
        this.callableReference = null
        this.functionLiteral = null
        this.objectLiteral = null
        this.collectionLiteral = null
        this.thisExpression = null
        this.superExpression = null
        this.ifExpression = null
        this.whenExpression = null
        this.tryExpression = null
        this.jumpExpression = null
        val node = grammarNode[0]

        when (node.rule)
        {
            KotlinGrammar.parenthesizedExpression ->
            {
                this.parenthesizedExpression = ParenthesizedExpression()
                this.parenthesizedExpression?.parse(node)
            }
            KotlinGrammar.simpleIdentifier              -> this.identifier = node.text
            KotlinGrammar.literalConstant         ->
            {
                this.literalConstant = LiteralConstant()
                this.literalConstant?.parse(node)
            }
            KotlinGrammar.stringLiteral           ->
            {
                this.stringLiteral = StringLiteral()
                this.stringLiteral?.parse(node)
            }
            KotlinGrammar.callableReference       ->
            {
                this.callableReference = CallableReference()
                this.callableReference?.parse(node)
            }
            KotlinGrammar.functionLiteral         ->
            {
                this.functionLiteral = FunctionLiteral()
                this.functionLiteral?.parse(node)
            }
            KotlinGrammar.objectLiteral           ->
            {
                this.objectLiteral = ObjectLiteral()
                this.objectLiteral?.parse(node)
            }
            KotlinGrammar.collectionLiteral       ->
            {
                this.collectionLiteral = CollectionLiteral()
                this.collectionLiteral?.parse(node)
            }
            KotlinGrammar.thisExpression          ->
            {
                this.thisExpression = ThisExpression()
                this.thisExpression?.parse(node)
            }
            KotlinGrammar.superExpression         ->
            {
                this.superExpression = SuperExpression()
                this.superExpression?.parse(node)
            }
            KotlinGrammar.ifExpression            ->
            {
                this.ifExpression = IfExpression()
                this.ifExpression?.parse(node)
            }
            KotlinGrammar.whenExpression          ->
            {
                this.whenExpression = WhenExpression()
                this.whenExpression?.parse(node)
            }
            KotlinGrammar.tryExpression           ->
            {
                this.tryExpression = TryExpression()
                this.tryExpression?.parse(node)
            }
            KotlinGrammar.jumpExpression          ->
            {
                this.jumpExpression = JumpExpression()
                this.jumpExpression?.parse(node)
            }
        }
    }
}
