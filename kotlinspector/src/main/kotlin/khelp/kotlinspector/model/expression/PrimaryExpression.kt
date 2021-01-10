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

        when (grammarNode.rule)
        {
            KotlinGrammar.parenthesizedExpression ->
            {
                this.parenthesizedExpression = ParenthesizedExpression()
                this.parenthesizedExpression?.parse(grammarNode)
            }
            KotlinGrammar.identifier              -> this.identifier = grammarNode.text
            KotlinGrammar.literalConstant         ->
            {
                this.literalConstant = LiteralConstant()
                this.literalConstant?.parse(grammarNode)
            }
            KotlinGrammar.stringLiteral           ->
            {
                this.stringLiteral = StringLiteral()
                this.stringLiteral?.parse(grammarNode)
            }
            KotlinGrammar.callableReference       ->
            {
                this.callableReference = CallableReference()
                this.callableReference?.parse(grammarNode)
            }
            KotlinGrammar.functionLiteral         ->
            {
                this.functionLiteral = FunctionLiteral()
                this.functionLiteral?.parse(grammarNode)
            }
            KotlinGrammar.objectLiteral           ->
            {
                this.objectLiteral = ObjectLiteral()
                this.objectLiteral?.parse(grammarNode)
            }
            KotlinGrammar.collectionLiteral       ->
            {
                this.collectionLiteral = CollectionLiteral()
                this.collectionLiteral?.parse(grammarNode)
            }
            KotlinGrammar.thisExpression          ->
            {
                this.thisExpression = ThisExpression()
                this.thisExpression?.parse(grammarNode)
            }
            KotlinGrammar.superExpression         ->
            {
                this.superExpression = SuperExpression()
                this.superExpression?.parse(grammarNode)
            }
            KotlinGrammar.ifExpression            ->
            {
                this.ifExpression = IfExpression()
                this.ifExpression?.parse(grammarNode)
            }
            KotlinGrammar.whenExpression          ->
            {
                this.whenExpression = WhenExpression()
                this.whenExpression?.parse(grammarNode)
            }
            KotlinGrammar.tryExpression           ->
            {
                this.tryExpression = TryExpression()
                this.tryExpression?.parse(grammarNode)
            }
            KotlinGrammar.jumpExpression          ->
            {
                this.jumpExpression = JumpExpression()
                this.jumpExpression?.parse(grammarNode)
            }
        }
    }
}
